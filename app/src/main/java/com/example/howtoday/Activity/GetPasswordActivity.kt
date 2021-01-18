package com.example.howtoday.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.howtoday.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_get_password.*

//비밓번호 찾기 화면의 액티비티입니다.
class GetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_password)

        getPasswordView_sendEmailButton.setOnClickListener {
            if (getPasswordView_EmailEditText.text.isEmpty()){
                Toast.makeText(applicationContext, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sendEmail()
        }
    }

    private fun sendEmail() {
        val emailAddress = getPasswordView_EmailEditText.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("getPasswordView", "Email sent.")
                } else {
                    Toast.makeText(applicationContext, "없는 이메일입니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}