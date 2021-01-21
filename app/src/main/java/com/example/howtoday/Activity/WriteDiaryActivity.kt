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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_write_diary.*
import java.util.*

class WriteDiaryActivity : AppCompatActivity() {

    var isWriteReady = false

    //안드로이드에서 이미지를 가져오기 상태 표시 위한 전역 변수
    val GET_GALLERY_IMAGE = 200;

    //선택된 이미지의 Uri
    var selectImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        saveDiaryButton.setOnClickListener {
            checkDiary()
            saveDiary()
        }

        imageView.setOnClickListener {
            getImage()
        }
    }

    private fun getImage() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        startActivityForResult(intent, GET_GALLERY_IMAGE)
    }

    //이미지를 선택해서 응답을 받았을때 처리할 일
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            selectImageUri = data.data!!
            imageView.setImageURI(selectImageUri)
        }
    }

    //뒤로 가기를 누르면 임시저장을 하고 나가게 만들었습니다.
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed();
    }

    private fun checkDiary() {
        val title = titleEditText_writeDiaryView.text.toString()
        val content = contentEditText_writeDiaryView.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(applicationContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            isWriteReady = false
        } else {
            isWriteReady = true
        }
    }


    private fun saveDiary() {
        if (isWriteReady) {

            val db = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser
            var writeInfo = WriteInfo(
                titleEditText_writeDiaryView.text.toString(),
                contentEditText_writeDiaryView.text.toString(),
                user?.displayName.toString(),
                user?.uid.toString(),
                Date()
            )

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
            val urlTask = upload?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storage.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    writeInfo.imageUri = task.result.toString()
                    Toast.makeText(applicationContext, "이미지 링크 추가해서 저장", Toast.LENGTH_LONG).show()

                    //이미지 uri를 받아오면 업로드를 합니다.
                    val diary = hashMapOf(
                        "title" to writeInfo.title,
                        "content" to writeInfo.content,
                        "imageUri" to writeInfo.imageUri,
                        "publisherName" to writeInfo.publisherName,
                        "publisherUid" to writeInfo.publisherUid,
                        "date" to writeInfo.date
                    )

                    // Add a new document with a generated ID
                    db.collection("diary")
                        .add(diary)
                        .addOnSuccessListener { documentReference ->
                            Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                            Toast.makeText(applicationContext, "일기를 적었습니다.!!!", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("", "Error adding document", e)
                        }

                }
            }


        }
    }
}