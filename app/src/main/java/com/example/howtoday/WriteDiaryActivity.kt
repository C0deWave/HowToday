package com.example.howtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_write_diary.*

class WriteDiaryActivity : AppCompatActivity() {

    var isWriteReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        saveDiaryButton.setOnClickListener {
            checkDiary()
            saveDiary()
        }
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
        if (isWriteReady){
            val db = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser
            var writeInfo = WriteInfo(titleEditText_writeDiaryView.text.toString() , contentEditText_writeDiaryView.text.toString())

            val diary = hashMapOf(
                "title" to writeInfo.title,
                "content" to writeInfo.content
            )

            // Add a new document with a generated ID
            db.collection("diary")
                .add(diary)
                .addOnSuccessListener { documentReference ->
                    Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(applicationContext, "일기를 적었습니다.!!!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("", "Error adding document", e)
                }

        }
    }
}