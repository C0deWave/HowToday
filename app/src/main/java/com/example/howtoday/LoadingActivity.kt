package com.example.howtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class LoadingActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    //이미 로그인한 적이 있는지 확인합니다.
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.// Initialize Firebase Auth

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        if (currentUser != null) {
            val intent: Intent = Intent(applicationContext, MainActivity::class.java)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    startActivity(intent)
                    finish()
                }
            }, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        val intent: Intent = Intent(applicationContext, LoginActivity::class.java)

        if (currentUser == null) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    startActivity(intent)
                    finish()
                }
            }, 2000)
        }
    }
}