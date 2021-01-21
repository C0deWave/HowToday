package com.example.howtoday.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howtoday.R
import com.example.howtoday.classFile.DiaryAdapter
import com.example.howtoday.classFile.WriteInfo
import kotlinx.android.synthetic.main.fragment_order_diary.view.*
import java.util.*


class OrderDiaryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_diary, container, false)


        //실험용 데이터
        var data2 = WriteInfo("오늘의 개발일", "어댑터 만들기 힘들다\n\n\n4줄까지만 표시", "주명", "aa", Date())
        data2.imageUri =
            "https://firebasestorage.googleapis.com/v0/b/howtoday-4521d.appspot.com/o/image%2F-166976989?alt=media&token=0009b952-8564-462e-ac03-7248c0e5d10b"

        var listArray: List<WriteInfo> = mutableListOf(
            data2,
            WriteInfo("", "", "", "", Date())
        )


        view.diaryRecyclerView.adapter = DiaryAdapter(listArray)
        view.diaryRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderDiaryFragment().apply {

            }
    }
}
