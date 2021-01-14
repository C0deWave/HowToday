package com.example.howtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

//회원 가입 창입니다.
class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    //이미 로그인한 적이 있는지 확인합니다.
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth?.currentUser
        //updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 회원가입 버튼을 눌렀을 경우
        signUpButton.setOnClickListener {
            //Log.d("회원 가입 버튼 클릭","회원 가입 버튼 클릭")
            //비밀 번호가 같을 경우에만 회원 가입을 합니다.
            if (passwordEditText.text.toString() == checkPasswordEditText.text.toString()){
                signUp(emailEditText.text.toString(), passwordEditText.text.toString())
            }else{
                Toast.makeText(applicationContext,"비밀 번호가 같지 않습니다.",Toast.LENGTH_SHORT).show()
            }
            // Initialize Firebase Auth
            auth = Firebase.auth
        }
    }

    private fun signUp(email: String, password: String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("회원가입 성공", "createUserWithEmail:success")
                    Log.d("이메일", email)
                    Log.d("패스워드", password)
                    val user = auth?.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("회원 가입 실패", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}