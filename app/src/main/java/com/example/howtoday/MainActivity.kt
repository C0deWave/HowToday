package com.example.howtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setSelectedItemId(R.id.view_my_diary)
        supportFragmentManager.beginTransaction().add(R.id.linearLayout, OrderDiaryFragment()).commit()

        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.background = null

        fab.setOnClickListener {
            // 메뉴 선택이 가운데로 하게 만듭니다.
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            supportFragmentManager.beginTransaction().replace(R.id.linearLayout, OrderDiaryFragment()).commitAllowingStateLoss()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.view_my_diary -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, MyDiaryFragment()).commitAllowingStateLoss()
                return true
            }

//            R.id.view_order_diary -> {
//                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, OrderDiaryFragment()).commitAllowingStateLoss()
//                return true
//            }

            R.id.write_diary->{
                //supportFragmentManager.beginTransaction().replace(R.id.linearLayout, WriteDiaryFragment()).commitAllowingStateLoss()
                // 화면전환을 할 수 있도록 만들자
                var intent : Intent = Intent(applicationContext,WriteDiaryActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}