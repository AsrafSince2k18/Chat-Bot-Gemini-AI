package com.example.chatbot.root.presentance.response

sealed class Response <T>(val data : T?=null,
                          val message : String?=null,
                            val loading : Boolean?=null) {

    class Success<T>(data: T?) : Response<T>(data=data)

    class Error<T>(message: String?) : Response<T>(message=message)

    class Loading<T>(loading: Boolean?) : Response<T>(loading = loading)

}