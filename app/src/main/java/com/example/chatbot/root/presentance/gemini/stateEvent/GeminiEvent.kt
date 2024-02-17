package com.example.chatbot.root.presentance.gemini.stateEvent

import android.graphics.Bitmap

sealed class GeminiEvent {


    data class Message(val message: String) : GeminiEvent()

    data class MessageAndBitmap(val message: String,
                                val bitmap: Bitmap?) : GeminiEvent()


}