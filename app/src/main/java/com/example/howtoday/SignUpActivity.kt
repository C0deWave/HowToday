package com.example.howtoday

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.system.exitProcess

//회원 가입 창입니다.
class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null

    //이미 로그인한 적이 있는지 확인합니다.
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.// Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 회원가입 버튼을 눌렀을 경우
        signUpButton.setOnClickListener {
            //Log.d("회원 가입 버튼 클릭","회원 가입 버튼 클릭")
            //비밀 번호가 같을 경우에만 회원 가입을 합니다.
            if (passwordEditText.text.toString() != checkPasswordEditText.text.toString()) {
                Toast.makeText(applicationContext, "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordEditText.text.isEmpty() || emailEditText.text.isEmpty() || nicknameEditText.text.isEmpty()) {
                Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUp(emailEditText.text.toString(), passwordEditText.text.toString())

        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent: Intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun signUp(email: String, password: String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext, "회원가입 성공!!!", Toast.LENGTH_SHORT).show()
                    Log.d("회원가입 성공", "createUserWithEmail:success")
                    Log.d("이메일", email)
                    Log.d("패스워드", password)

                    user = auth?.currentUser
                    setNickname()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("회원 가입 실패", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //닉넴임을 지정합니다.
    private fun setNickname() {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "${nicknameEditText.text}"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("", "닉네임 지정에 성공했습니다.")

                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }
}