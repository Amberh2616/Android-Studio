package com.example.myapplicationmyp2

import java.io.Serializable

class Result2: Serializable {
    var English= "未命名"
    var Chinese=  "未命名"


    constructor(English: String,Chinese: String) {
        this.English = English
        this.Chinese = Chinese

    }

    override fun toString(): String {
        return "Result2(English='$English',Chinese=$Chinese)"
    }
}
