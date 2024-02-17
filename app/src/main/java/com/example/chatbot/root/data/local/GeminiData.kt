package com.example.chatbot.root.data.local

import android.graphics.Bitmap

data class GeminiData(
    val prompt : String,

    val image : Bitmap?,

    val isUser : Boolean

)
