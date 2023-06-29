package com.example.myapplicationlistview

import java.io.Serializable

class Result2: Serializable {
    var name = "未命名"
    var eng = -1
    var math = -1

    constructor(name: String, eng: Int, math: Int) {
        this.name = name
        this.eng = eng
        this.math = math
    }

    override fun toString(): String {
        return "Result2(name='$name', eng=$eng, math=$math)"
    }
}
