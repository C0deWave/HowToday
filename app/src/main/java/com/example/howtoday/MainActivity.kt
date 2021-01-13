package com.example.howtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    //애니메이션을 만들어 줍니다.
    private val fromRightBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_right_bottom_anim) }
    private val toRightBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_right_bottom_anim) }
    private val fromleftBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_left_bottom_anim) }
    private val toleftBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_left_bottom_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    //fab의 클릭 여부를 확인합니다.
    private var clickedFab = false

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
            onFabButtonClicked()
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            supportFragmentManager.beginTransaction().replace(R.id.linearLayout, OrderDiaryFragment()).commitAllowingStateLoss()
        }

        smailFab.setOnClickListener{
            Toast.makeText(applicationContext,"smail Fab",Toast.LENGTH_LONG).show()
        }
        sadFab.setOnClickListener{
            Toast.makeText(applicationContext,"sad Fab",Toast.LENGTH_LONG).show()
        }
        angryFab.setOnClickListener{
            Toast.makeText(applicationContext,"angryFab",Toast.LENGTH_LONG).show()
        }

    }

    private fun onFabButtonClicked() {
        setVisibility(clickedFab)
        setAnimation(clickedFab)
        setClickable(clickedFab)
        clickedFab = !clickedFab
    }

    private fun setVisibility(clickedFab : Boolean) {
        if (!clickedFab){
            smailFab.visibility = View.VISIBLE
            sadFab.visibility = View.VISIBLE
            angryFab.visibility = View.VISIBLE
            smailFab.alpha = 1f
            sadFab.alpha = 1f
            angryFab.alpha = 1f
        }else{
            smailFab.visibility = View.INVISIBLE
            sadFab.visibility = View.INVISIBLE
            angryFab.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clickedFab : Boolean) {
        if (!clickedFab){
            smailFab.startAnimation(fromleftBottom)
            sadFab.startAnimation(fromBottom)
            angryFab.startAnimation(fromRightBottom)
        }else{
            smailFab.startAnimation(toleftBottom)
            sadFab.startAnimation(toBottom)
            angryFab.startAnimation(toRightBottom)
        }
    }

    private fun setClickable(clickedFab: Boolean){
        if(clickedFab){
            smailFab.isClickable = false
            sadFab.isClickable = false
            angryFab.isClickable = false
        }else{
            smailFab.isClickable = true
            sadFab.isClickable = true
            angryFab.isClickable = true
        }
    }

    override fun onPause() {
        super.onPause()
        //화면 전환시 투명 애니메이션이 풀리는 것을 대비해서 지정합니다.
        smailFab.alpha = 0f
        sadFab.alpha = 0f
        angryFab.alpha = 0f
        clickedFab = false
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