package com.example.howtoday.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.howtoday.R
import com.example.howtoday.fragment.MyDiaryFragment
import com.example.howtoday.fragment.OrderDiaryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_write_diary.*

//메인 화면 창입니다.
class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    //애니메이션을 만들어 줍니다.
    private val fromRightBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_right_bottom_anim
    ) }
    private val toRightBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.to_right_bottom_anim
    ) }
    private val fromLeftBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_left_bottom_anim
    ) }
    private val toLeftBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.to_left_bottom_anim
    ) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_bottom_anim
    ) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.to_bottom_anim
    ) }

    //글씨기로 다녀옴을 알려주는 전역변수
    val STATUS_WRITE_DIARY = 200;

    //fab의 클릭 여부를 확인합니다.
    private var clickedFab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setSelectedItemId(R.id.view_my_diary)
        supportFragmentManager.beginTransaction().add(
            R.id.linearLayout,
            OrderDiaryFragment()
        ).commit()

        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.background = null

        fab.setOnClickListener {
            // 메뉴 선택이 가운데로 하게 만듭니다.
            onFabButtonClicked()
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            supportFragmentManager.beginTransaction().replace(
                R.id.linearLayout,
                OrderDiaryFragment()
            ).commitAllowingStateLoss()
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

        //스크롤시 fab을 사라지게 합니다.
        NestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val dy = oldScrollY - scrollY
            if (dy < 0) {
                bottomAppBar.performHide().run {
                    fab.hide()
                    angryFab.hide()
                    sadFab.hide()
                    smailFab.hide()
                }
            } else if (dy > 0) {
                bottomAppBar.performShow().run {
                    fab.show()
                    if (clickedFab){
                        angryFab.show()
                        sadFab.show()
                        smailFab.show()
                    }
                }
            }
        }
    }

    //뒤로 가기를 2번 연속으로 눌러야지만 뒤로가기가 작동하게 만듭니다.
    private var backBtnTime: Long = 0

    override fun onBackPressed() {
        //super.onBackPressed()
        var curTime = System.currentTimeMillis();
        var gapTime = curTime - backBtnTime
        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
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
            smailFab.startAnimation(fromLeftBottom)
            sadFab.startAnimation(fromBottom)
            angryFab.startAnimation(fromRightBottom)
        }else{
            smailFab.startAnimation(toLeftBottom)
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
                supportFragmentManager.beginTransaction().replace(
                    R.id.linearLayout,
                    MyDiaryFragment()
                ).commitAllowingStateLoss()
                if (clickedFab){
                    setVisibility(clickedFab)
                    setAnimation(clickedFab)
                    setClickable(clickedFab)
                    clickedFab = !clickedFab
                }
                return true
            }

//            R.id.view_order_diary -> {
//                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, OrderDiaryFragment()).commitAllowingStateLoss()
//                return true
//            }

            R.id.write_diary ->{

                var intent : Intent = Intent(applicationContext, WriteDiaryActivity::class.java)
                startActivityForResult(intent, STATUS_WRITE_DIARY)
                return true
            }
        }
        return false
    }

    //글쓰기에서 돌아올 때 처리할 일
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STATUS_WRITE_DIARY && resultCode == RESULT_OK) {
            bottomNavigationView.selectedItemId = R.id.placeholder
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            supportFragmentManager.beginTransaction().replace(
                R.id.linearLayout,
                OrderDiaryFragment()
            ).commitAllowingStateLoss()
            Log.d("글쓰기에서 돌아옴","글쓰기에서 돌아옴")
        }
    }
}