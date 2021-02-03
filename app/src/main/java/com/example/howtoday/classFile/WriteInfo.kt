package com.example.howtoday.classFile

import java.util.*

// 일기 기록을 저장하는 클래스 입니다.
class WriteInfo {
    var title: String = ""              // 일기의 제목
    var content: String = ""            // 일기의 내용
    var imageUri : String = ""          // 일기 이미지 uri
    var publisherName: String = ""      // 작성자 닉네임
    var publisherUid: String = ""       // 작성자 URI
    var postId: Int = 0                 // 글의 고유 ID
    var date:Date? = null               // 글을 작성한 날짜

    //생성자 입니다.
    constructor(title: String, content: String, publisherName: String, publisherUid: String, date:Date?) {
        this.title = title
        this.content = content
        this.publisherName = publisherName
        this.publisherUid = publisherUid
        this.date = date
        postId = (publisherUid + title).hashCode()
    }
}