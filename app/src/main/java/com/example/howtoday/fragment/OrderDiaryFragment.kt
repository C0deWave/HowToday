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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_order_diary.*
import kotlinx.android.synthetic.main.fragment_order_diary.view.*

// 메인 일기 화면의 프레그먼트입니다.
class OrderDiaryFragment : Fragment() {

    //에러코드 태그입니다.
    val TAG = "OrderDiaryFragment"
    // firestore 데이터베이스 변수입니다.
    val db = Firebase.firestore

    // 일기 리스트를 할당하는 어레이 입니다.
    var listArray: MutableList<WriteInfo> = mutableListOf()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //화면이 시작되었을때
    override fun onResume() {
        super.onResume()
        //리스트 어레이를 초기화 합니다.
        listArray.clear()

        //서버에서 diary 데이터베이스의 글을 가져옵니다.
        db.collection("diary")
            .get()
            //글을 가져오기 성공했을때의 리스너입니다.
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

                    // 게시글 어레이에 추가합니다.
                    listArray.add(data)
                    diaryRecyclerView.adapter?.notifyDataSetChanged()
                    Log.d("글 새로고침","글 새로고침")

                }
            }
            //실패했을때의 리스너입니다.
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 뷰를 생성할때
    // 각종 어댑터를 지금 지정합니다.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_diary, container, false)

        //어댑터를 지정합니다.
        view.diaryRecyclerView.adapter = DiaryAdapter(listArray)
        view.diaryRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}
