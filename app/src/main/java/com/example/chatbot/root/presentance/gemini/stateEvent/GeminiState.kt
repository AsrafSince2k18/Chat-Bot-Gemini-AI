package com.example.chatbot.root.presentance.gemini.stateEvent

import android.graphics.Bitmap
import android.net.Uri
import com.example.chatbot.root.data.local.GeminiData
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GeminiState(

    var bitmap: Bitmap?=null,

    var textError : Boolean =false,

    val prompt: String?=null,

    val chatList : MutableList<GeminiData> = mutableListOf(),

    val respond: Boolean =false,

    val isLoading: Boolean=false,

    val isError : String?=null,

    var uri : String?=null
)
