package com.example.howtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login2.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var TAG = "login화면 : "

    //이미 로그인한 적이 있는지 확인합니다.
    public override fun onStart() {
        super.onStart()// Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        //로그인 버튼을 눌렀을 경우
        loginButton.setOnClickListener {
            if (loginEmailEditText.text.isEmpty() || loginPasswordEditText.text.isEmpty()){
                Toast.makeText(applicationContext, "정보를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            login()

        }

        login_SignInButton.setOnClickListener {
            var intent : Intent = Intent(applicationContext,SignUpActivity::class.java)
            startActivity(intent)
        }

        getPasswordButton.setOnClickListener {
            var intent : Intent = Intent(applicationContext,GetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        var email = loginEmailEditText.text.toString()
        var password = loginPasswordEditText.text.toString()

        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val user = auth?.currentUser

                    // 로그인에 성공한 경우
                    // 메인 화면으로 화면전환을 합니다.
                    var intent : Intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext, "없는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }
}