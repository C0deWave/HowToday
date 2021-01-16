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


    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        auth = FirebaseAuth.getInstance()
        currentUser = auth?.currentUser

        //이미 로그인한적이 있는지 확인합니다.
        if (currentUser == null) {

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val intent: Intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2000)

        }else{

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2000)

        }
    }
}