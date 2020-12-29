package com.example.howtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val intent:Intent = Intent(applicationContext,MainActivity::class.java)

        Timer().schedule(object:TimerTask(){
            override fun run(){
                startActivity(intent)
                finish()
            }
        },2000)
    }
}