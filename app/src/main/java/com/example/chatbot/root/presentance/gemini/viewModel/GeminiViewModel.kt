package com.example.chatbot.root.presentance.gemini.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.root.data.local.GeminiData
import com.example.chatbot.root.domain.repository.GeminiRepository
import com.example.chatbot.root.presentance.gemini.stateEvent.GeminiEvent
import com.example.chatbot.root.presentance.gemini.stateEvent.GeminiState
import com.example.chatbot.root.presentance.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GeminiState())
    val state = _state.asStateFlow()

    fun onEvent(event: GeminiEvent){
        when(event){
            is GeminiEvent.Message -> {
                _state.update {
                    it.copy(
                        prompt = event.message
                    )

                }
            }
            is GeminiEvent.MessageAndBitmap -> {
                _state.update {
                    it.copy(
                        prompt = event.message,
                        bitmap = event.bitmap
                    )
                }

                if(event.message.isNotEmpty()) {
                    addUser(event.message,event.bitmap)
                    if (event.bitmap!=null) {
                        responseTextAndImage(event.message,event.bitmap)

                    } else {
                        responseText(event.message)

                    }
                }
            }
        }
    }

    fun responseText(message:String){
        viewModelScope.launch {
         geminiRepository.messageOnly(message)
                .collect{response->
                    when(response){
                        is Response.Loading -> {
                                response.loading?.let { load->
                                    _state.update {
                                        it.copy(isLoading = load)
                                    }
                                }
                        }

                        is Response.Error -> {
                            response.message?.let { load->
                                _state.update {
                                    it.copy(isLoading = false,
                                        isError = load)
                                }
                            }
                        }
                        is Response.Success -> {
                            response.data?.let {contentResponse->
                                _state.update {
                                    it.copy(
                                        prompt = "",
                                        isLoading = false,
                                        chatList = it.chatList.toMutableList().apply {
                                            add(0,contentResponse)
                                        }
                                    )
                                }
                            }
                        }

                    }
                }

        }
    }

    fun responseTextAndImage(message:String,bitmap: Bitmap?){
        viewModelScope.launch {
            if (bitmap != null) {
                geminiRepository.messageAndBitmap(message,bitmap)
                    .collect{response->
                        when(response){
                            is Response.Loading -> {
                                response.loading?.let { load->
                                    _state.update {
                                        it.copy(isLoading = load)
                                    }
                                }
                            }

                            is Response.Error -> {
                                response.message?.let {error->
                                    _state.update {
                                        it.copy(isError = error,
                                            isLoading = false)
                                    }
                                }
                            }

                            is Response.Success -> {
                                response.data?.let {contentResponse->
                                    _state.update {
                                        it.copy(
                                            prompt = "",
                                            isLoading = false,
                                            chatList = it.chatList.toMutableList().apply {
                                                add(0,contentResponse)
                                            }
                                        )
                                    }
                                }
                            }

                        }
                    }
            }

        }
    }


    fun addUser(
        message: String?,
        image:Bitmap?
    ){
        viewModelScope.launch {
            _state.update {
                it.copy(chatList = it.chatList.toMutableList().apply {
                    add(0,GeminiData(message?:"",image=image,isUser = true))
                },
                    bitmap =null)
            }
        }
    }

}

