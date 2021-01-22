package com.example.howtoday.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howtoday.R
import com.example.howtoday.classFile.DiaryAdapter
import com.example.howtoday.classFile.WriteInfo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_order_diary.*
import kotlinx.android.synthetic.main.fragment_order_diary.view.*
import java.util.*
import kotlin.time.TimeSource


class OrderDiaryFragment : Fragment() {

    val TAG = "OrderDiaryFragment"
    val db = Firebase.firestore

    var listArray: MutableList<WriteInfo> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //리스트 어레이를 초기화 합니다.
        listArray.clear()

        //서버에서 글을 가져옵니다.
        db.collection("diary")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    var data = WriteInfo(document.data.get("title").toString(),
                        document.data.get("content").toString(),
                        document.data.get("publisherName").toString(),
                        document.data.get("publisherUid").toString(),
                        document.getDate("date")
                    )
                    data.imageUri = document.data.get("imageUri").toString()

                    listArray.add(data)
                    diaryRecyclerView.adapter?.notifyDataSetChanged()
                    Log.d("글 새로고침","글 새로고침")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_order_diary, container, false)
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
