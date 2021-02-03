package com.example.howtoday.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.howtoday.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

// 메인 로그인 화면입니다.
class LoginActivity : AppCompatActivity() {

    // 계정 정보 변수입니다.
    private lateinit var auth: FirebaseAuth

    // 로그 태그입니다.
    var TAG = "login화면 : "

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 버튼을 눌렀을 경우
        // 아이디 비번칸이 입력이 되었는지 체크합니다.
        loginButton.setOnClickListener {
            if (loginEmailEditText.text.isEmpty() || loginPasswordEditText.text.isEmpty()) {
                Toast.makeText(applicationContext, "정보를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                auth = FirebaseAuth.getInstance()
                login()
            }
        }

        // 회원가입 버튼을 눌렀을때의 처리입니다.
        login_SignInButton.setOnClickListener {
            var intent: Intent = Intent(
                applicationContext,
                SignUpActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        // 비밀번호 찾기 버튼을 눌렀을때의 처리입니다.
        getPasswordButton.setOnClickListener {
            var intent: Intent = Intent(
                applicationContext,
                GetPasswordActivity::class.java
            )
            startActivity(intent)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 파이어베이스 auth에 로그인을 진행하는 함수입니다.
    private fun login() {
        var email = loginEmailEditText.text.toString()
        var password = loginPasswordEditText.text.toString()

        // 로그인을 진행합니다.
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                // 로그인에 성공한 경우
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth?.currentUser
                    // 메인 화면으로 화면전환을 합니다.
                    var intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                // 로그인에 실패했을때의 처리입니다.
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext, "아이디나 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}