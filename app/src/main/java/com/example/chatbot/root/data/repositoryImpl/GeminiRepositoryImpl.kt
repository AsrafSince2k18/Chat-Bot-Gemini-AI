package com.example.chatbot.root.data.repositoryImpl

import android.graphics.Bitmap
import coil.network.HttpException
import com.example.chatbot.root.data.local.GeminiData
import com.example.chatbot.root.domain.repository.GeminiRepository
import com.example.chatbot.root.presentance.response.Response
import com.example.chatbot.root.presentance.utils.Utils.API_KEY
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiRepositoryImpl :GeminiRepository{
    override suspend fun messageOnly(message: String):
            Flow<Response<GeminiData>> {
        return flow {
            emit(Response.Loading(true))

            try {
                val data  =GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = API_KEY,
                    safetySettings =listOf(
                        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
                    )
                )
                val response = data.generateContent(message).text
                emit(Response.Success(GeminiData(prompt = response?:"",image = null,isUser = false)))
            }catch (e:Exception){
                emit(Response.Error(e.message))
            }catch (e:HttpException){
                emit(Response.Error(e.message))
            }catch (e:ResponseStoppedException){
                emit(Response.Error(e.message))
            }
        }
    }

    override suspend fun messageAndBitmap(message: String, image: Bitmap):
            Flow<Response<GeminiData>>{

        return flow {
            emit(Response.Loading(true))
            try {
                val response = GenerativeModel(
                    modelName = "gemini-pro-vision",
                    apiKey = API_KEY,
                    safetySettings =listOf(
                        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
                    )
                )
                val inputContent = content {
                    image(image)
                    text(message)
                }

                val botResponse=response.generateContent(inputContent).text

                emit(Response.Success(GeminiData(
                    prompt = botResponse ?:"",
                    image=null,
                    isUser = false
                )))
            }catch (e:Exception){
                emit(Response.Error(e.message))
            }catch (e:HttpException){
                emit(Response.Error(e.message))
            }catch (e:ResponseStoppedException){
                emit(Response.Error(e.message))
            }
        }

    }
}