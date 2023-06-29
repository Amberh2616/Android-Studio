package com.example.myapplicationlistview1

import android.icu.util.LocaleData.MeasurementSystem
import java.io.Serializable

class Result2: Serializable {
    var Measurement = "未命名"
    var 翻譯=  "未命名"


    constructor(Measurement: String,翻譯: String) {
        this.Measurement = Measurement
        this.翻譯 = 翻譯

    }

    override fun toString(): String {
        return "Result2(Measurement='$Measurement',翻譯=$翻譯)"
    }
}
