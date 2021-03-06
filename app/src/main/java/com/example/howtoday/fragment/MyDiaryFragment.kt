package com.example.howtoday.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.howtoday.Activity.LoginActivity
import com.example.howtoday.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_my_diary.view.*

// 내일기 버튼을 클릭했을때의 프레그먼트 입니다.
class MyDiaryFragment : Fragment() {


    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_diary, container, false)

        // 로그아웃 버튼을 눌렀을때 리스너입니다.
        view.logoutButton.setOnClickListener {
            Log.d("로그아웃버튼", ": 클릭")
            //파이어베이스 로그아웃을 합니다.
            FirebaseAuth.getInstance().signOut()
            //로그인 화면으로 넘어갑니다.
            val intent: Intent = Intent(activity?.applicationContext, LoginActivity::class.java)
            startActivity(intent)

            //현재 화면을 종료시킵니다.
            activity?.finish()
        }

        // Inflate the layout for this fragment
        return view
    }
}