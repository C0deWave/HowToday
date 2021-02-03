package com.example.howtoday.classFile

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.howtoday.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_diary.view.*

///////////////////////////////////////////////////////////////////////////////////////////////////
// 다이어리 메인 게시글의 어댑터 클래스 입니다.
class DiaryAdapter(var diary : List<WriteInfo>) : RecyclerView.Adapter<DiaryItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryItem {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary, parent,false)
        return DiaryItem(view)
    }

    override fun getItemCount(): Int {
        return diary.size
    }

    override fun onBindViewHolder(holder: DiaryItem, position: Int) {
        holder.binddata(diary[position])
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////
//아이템 클래스로써 RecyclerView.ViewHolder(itemView)를 상속 받아야 합니다.
//아이템을 바인딩 해주는 함수가 들어있습니다.
class DiaryItem(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun binddata(writeInfo: WriteInfo){
        var imageView = itemView.item_image
        var titleTextView = itemView.item_title
        var contentTextView = itemView.item_content

//이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 글쓰기 배경이미지뷰에 이미지 로딩
        Picasso.get()
            .load(Uri.parse(writeInfo.imageUri))
            .fit()
            .into(imageView)
        titleTextView.text = writeInfo.title
        contentTextView.text = writeInfo.content
    }
}