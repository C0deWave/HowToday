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
        bottomNavigationView.setSelectedItemId(R.id.view_order_diary)
        supportFragmentManager.beginTransaction().add(R.id.linearLayout, OrderDiaryFragment()).commit()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.view_my_diary -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, MyDiaryFragment()).commitAllowingStateLoss()
                return true
            }

            R.id.view_order_diary -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, OrderDiaryFragment()).commitAllowingStateLoss()
                return true
            }

            R.id.write_diary->{
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, WriteDiaryFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }
}