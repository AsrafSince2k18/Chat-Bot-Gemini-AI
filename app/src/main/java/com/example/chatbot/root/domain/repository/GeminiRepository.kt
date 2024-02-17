package com.example.chatbot.root.domain.repository

import android.graphics.Bitmap
import com.example.chatbot.root.data.local.GeminiData
import com.example.chatbot.root.presentance.response.Response
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {

    suspend fun messageOnly(message:String):  Flow<Response<GeminiData>>

    suspend fun messageAndBitmap(message: String,image: Bitmap):
            Flow<Response<GeminiData>>

}