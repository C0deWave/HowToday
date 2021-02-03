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
import com.example.howtoday.R
import com.example.howtoday.fragment.MyDiaryFragment
import com.example.howtoday.fragment.OrderDiaryFragment
import com.example.howtoday.fragment.WriteDiaryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

//메인 화면 창입니다.
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    // 애니메이션을 지정하는 변수입니다.
    //애니메이션을 만들어 줍니다.
    private val fromRightBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_right_bottom_anim
        )
    }
    private val toRightBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_right_bottom_anim
        )
    }
    private val fromLeftBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_left_bottom_anim
        )
    }
    private val toLeftBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_left_bottom_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    //글쓰기로 다녀옴을 알려주는 플래그 전역변수
    // startActivityForResult 에서 플래그로 사용됩니다.
    val STATUS_WRITE_DIARY = 200;

    //fab의 클릭 여부를 확인합니다.
    // 활성화 여부를 지정하는 데에도 이용됩니다.
    private var clickedFab = false

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 화면이 생성 되었을때의 처리입니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bottom navi의 아이템과 리스너를 지정해 줍니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setSelectedItemId(R.id.view_my_diary)
        // 시작 프레그먼트를 지정합니다.
        supportFragmentManager.beginTransaction().add(
            R.id.linearLayout,
            OrderDiaryFragment()
        ).commit()

        // 가운데 bottomNavi의 활성화를 없엡니다.
        //fab이 작동해야 하기 때문입니다.
        bottomNavigationView.menu.getItem(1).isEnabled = false
        //디자인적 요소로 bottom navi의 배경을 제거합니다.
        bottomNavigationView.background = null

        //fab을 클릭했을때의 처리입니다.
        fab.setOnClickListener {
            //fab을 눌렀을때 처리입니다.
            onFabButtonClicked()
            // 메뉴 선택이 가운데로 하게 만듭니다.
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            //게시글 보기 화면으로 프레그먼트를 전환합니다.
            supportFragmentManager.beginTransaction().replace(
                R.id.linearLayout,
                OrderDiaryFragment()
            ).commitAllowingStateLoss()
        }

        // 활성화된 웃는 미니 fab을 눌렀을때의 처리
        smailFab.setOnClickListener {
            Toast.makeText(applicationContext, "smail Fab", Toast.LENGTH_LONG).show()
        }
        // 활성화된 슬픈 미니 fab을 눌렀을때의 처리
        sadFab.setOnClickListener {
            Toast.makeText(applicationContext, "sad Fab", Toast.LENGTH_LONG).show()
        }
        // 활성화된 화난 미니 fab을 눌렀을때의 처리
        angryFab.setOnClickListener {
            Toast.makeText(applicationContext, "angryFab", Toast.LENGTH_LONG).show()
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
                    if (clickedFab) {
                        angryFab.show()
                        sadFab.show()
                        smailFab.show()
                    }
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 다른 화면으로 다녀올때 VISABLE가 작동되지 않아 투명도를 사라지게 하는 방법으로 해결했습니다.
    override fun onPause() {
        super.onPause()
        //화면 전환시 투명 애니메이션이 풀리는 것을 대비해서 지정합니다.
        smailFab.alpha = 0f
        sadFab.alpha = 0f
        angryFab.alpha = 0f
        clickedFab = false
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //네비게이션 메뉴를 지정했을때의 기능 구현입니다.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 내일기를 보는 화면을 눌렀을 때
            R.id.view_my_diary -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.linearLayout,
                    MyDiaryFragment()
                ).commitAllowingStateLoss()
                if (clickedFab) {
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

            //글쓰기로 화면을 전환합니다.
            R.id.write_diary -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.linearLayout,
                    WriteDiaryFragment()
                ).commitAllowingStateLoss()
                if (clickedFab) {
                    setVisibility(clickedFab)
                    setAnimation(clickedFab)
                    setClickable(clickedFab)
                    clickedFab = !clickedFab
                }
                return true
                //var intent: Intent = Intent(applicationContext, WriteDiaryActivity::class.java)
                //startActivityForResult(intent, STATUS_WRITE_DIARY)
                return true
            }
        }
        return false
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //글쓰기에서 돌아올 때 처리할 일
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STATUS_WRITE_DIARY && resultCode == RESULT_OK) {
            // bottom navi가 중앙을 지정하게 합니다.
            bottomNavigationView.selectedItemId = R.id.placeholder
            bottomNavigationView.setSelectedItemId(R.id.placeholder)
            //프레그먼트를 지정합니다.
            supportFragmentManager.beginTransaction().replace(
                R.id.linearLayout,
                OrderDiaryFragment()
            ).commitAllowingStateLoss()
            Log.d("글쓰기에서 돌아옴", "글쓰기에서 돌아옴")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //fab버튼을 클릭했을때의 처리 과정입니다.
    private fun onFabButtonClicked() {
        setVisibility(clickedFab)
        setAnimation(clickedFab)
        setClickable(clickedFab)
        clickedFab = !clickedFab
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 하부 fab의 클릭의 가능여부를 지정하는 역할을 합니다.
    private fun setClickable(clickedFab: Boolean) {
        if (clickedFab) {
            smailFab.isClickable = false
            sadFab.isClickable = false
            angryFab.isClickable = false
        } else {
            smailFab.isClickable = true
            sadFab.isClickable = true
            angryFab.isClickable = true
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 등장이나 사라지는 애니메이션을 구현합니다.
    private fun setAnimation(clickedFab: Boolean) {
        if (!clickedFab) {
            smailFab.startAnimation(fromLeftBottom)
            sadFab.startAnimation(fromBottom)
            angryFab.startAnimation(fromRightBottom)
        } else {
            smailFab.startAnimation(toLeftBottom)
            sadFab.startAnimation(toBottom)
            angryFab.startAnimation(toRightBottom)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //하부 fab을 보이게 하는 역할을 합니다.
    private fun setVisibility(clickedFab: Boolean) {
        if (!clickedFab) {
            smailFab.visibility = View.VISIBLE
            sadFab.visibility = View.VISIBLE
            angryFab.visibility = View.VISIBLE
            smailFab.alpha = 1f
            sadFab.alpha = 1f
            angryFab.alpha = 1f
        } else {
            smailFab.visibility = View.INVISIBLE
            sadFab.visibility = View.INVISIBLE
            angryFab.visibility = View.INVISIBLE
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //뒤로 가기를 2번 연속으로 눌러야지만 뒤로가기가 작동하게 만듭니다.
    private var backBtnTime: Long = 0
    override fun onBackPressed() {
        //super.onBackPressed()
        var curTime = System.currentTimeMillis();
        var gapTime = curTime - backBtnTime
        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}