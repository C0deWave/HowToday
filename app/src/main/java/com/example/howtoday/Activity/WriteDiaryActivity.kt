package com.example.howtoday.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.howtoday.R
import com.example.howtoday.classFile.WriteInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_write_diary.*
import java.util.*

// 일기쓰기 화면의 기능 구현입니다.
class WriteDiaryActivity : AppCompatActivity() {

    var uploadImageUrl: Task<Uri>? = null // 서버에 올린 이미지의 uri이다.
    val GET_GALLERY_IMAGE = 200;    // 안드로이드에서 이미지를 가져오기 상태 표시 위한 전역 변수
    var selectImageUri: Uri? = null // 선택된 이미지의 Uri

    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        //저장 버튼을 눌렀을 때
        saveDiaryButton.setOnClickListener {
            saveDiary()
        }

        // 이미지 레이아웃 버튼을 눌렀을때
        imageView.setOnClickListener {
            getImage()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 안드로이드 에서 이미지를 가져옵니다.
    // 그림일기 느낌으로 갈 것이기 때문에 이미지만 받을 것입니다.
    private fun getImage() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        startActivityForResult(intent, GET_GALLERY_IMAGE)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //이미지를 선택해서 응답을 받았을때 처리할 일
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            //이미지 뷰를 해당 이미지로 교환합니다.
            selectImageUri = data.data!!
            imageView.setImageURI(selectImageUri)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //뒤로 가기를 누르면 임시저장을 하고 나가게 만들었습니다.
    // TODO: 2/2/21 임시저장 구현하기
    override fun onBackPressed() {

        setResult(Activity.RESULT_OK)
        super.onBackPressed();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 일기의 제목과 내용이 입력이 되었는지 확인하는 작업을 합니다.
    private fun checkDiary(): Boolean {
        val title = titleEditText_writeDiaryView.text.toString()
        val content = contentEditText_writeDiaryView.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(applicationContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 파이어베이스 데이터 베이스에 저장을 할 준비를 합니다.
    private fun saveDiary() {
        // 일기쓸 준비가 되었는지 확인합니다.
        if (checkDiary()) {

            val db = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser

            // 일기 내용을 토대로 일기 클래스를 하나 만듭니다.
            var writeInfo = WriteInfo(
                titleEditText_writeDiaryView.text.toString(),
                contentEditText_writeDiaryView.text.toString(),
                user?.displayName.toString(),
                user?.uid.toString(),
                Date()
            )

            //이미지를 업로드 합니다.
            uploadImageOnFireBaseStorage(writeInfo, db)
                .run {
                    // 글을 업로드합니다.
                    uploadDiaryOnFireBase(writeInfo, db)
                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 파이어베이스에 diary카테고리에 글을 업로드 합니다.
    private fun uploadDiaryOnFireBase(writeInfo: WriteInfo, db: FirebaseFirestore) {
        writeInfo.imageUri = uploadImageUrl?.result.toString()

        if (writeInfo.imageUri.isEmpty()) {
            Toast.makeText(applicationContext, "이미지 링크 추가해서 저장", Toast.LENGTH_LONG).show()
        }

        //이미지 uri를 받아오면 업로드를 합니다.
        val diary = hashMapOf(
            "title" to writeInfo.title,
            "content" to writeInfo.content,
            "imageUri" to writeInfo.imageUri,
            "publisherName" to writeInfo.publisherName,
            "publisherUid" to writeInfo.publisherUid,
            "date" to writeInfo.date
        )

        // 파이어베이스에 diary 카테고리에 업로드를 진행합니다.
        db.collection("diary")
            .add(diary)
            .addOnSuccessListener { documentReference ->
                Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("", "Error adding document", e)
            }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 파이어베이스 데이터베이스에 이미지를 업로드합니다.
    private fun uploadImageOnFireBaseStorage(
        writeInfo: WriteInfo,
        db: FirebaseFirestore
    ): Task<Uri>? {
        //게시글 ID로 제목삼아서 파이어베이스에 올립니다.
        val storage = FirebaseStorage.getInstance().getReference("/image/${writeInfo.postId}")

        //이미지의 Uri로 Storage에 파일을 업로드합니다.
        val upload = selectImageUri?.let { it1 -> storage.putFile(it1) }

        //업로드가 완료 되었을 때의 로그를 표시하게 합니다.
        upload?.addOnFailureListener {
            Log.d("image", "이미지 업로드 실패")
        }?.addOnSuccessListener {
            Log.d("image", "이미지 업로드 성공")
        }

        //파이어베이스에 올린 이미지의 Uri를 받아옵니다.
        //Uri를 받으면 이를 파이어베이스 실시간 데이터베이스에 넣어소 올려줍니다.
        uploadImageUrl = upload?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storage.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
            }
        }

        return uploadImageUrl
    }
}