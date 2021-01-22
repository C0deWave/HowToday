package com.example.howtoday.classFile

import java.util.*

class WriteInfo {
    var title: String = ""
    var content: String = ""
    var imageUri : String = ""
    var publisherName: String = ""
    var publisherUid: String = ""
    var postId: Int = 0
    var date:Date? = null

    constructor(title: String, content: String, publisherName: String, publisherUid: String, date:Date?) {
        this.title = title
        this.content = content
        this.publisherName = publisherName
        this.publisherUid = publisherUid
        this.date = date
        postId = (publisherUid + title).hashCode()
    }
}