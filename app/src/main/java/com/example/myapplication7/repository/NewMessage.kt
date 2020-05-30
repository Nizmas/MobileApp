package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.entities.MessageBox
import com.example.myapplication7.entities.ScoreList
import com.example.myapplication7.entities.User
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object NewMessage {
    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public lateinit var messageData: List<MessageBox>

    public fun sendingMessage(newMsg: String) {
        println("###################### sending Message has been launched ##############################")

        val okHttpClient = OkHttpClient()
        val payload = "{\"newmessage\":\"$newMsg\"}"
        val body: RequestBody =
            payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/newmsg")
            .addHeader("Authorization", "Bearer " + Authorization.userInfo.token)
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException){
                println("Failure!!!!")
                message.postValue(AUTH_STATUS.FAILED)
            }
            override fun onResponse(call: Call, response: Response) {
                println("Response!!!!")
                if (response.code ==200){
                    val gson = Gson()
                    messageData = gson.fromJson(response.body!!.string(), Array<MessageBox>::class.java).toList()
                    message.postValue(AUTH_STATUS.SUCCESS)
                } else {
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }

    public fun showMessages() {
        println("###################### showing Message has been launched ##############################")

        val okHttpClient = OkHttpClient()

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/showmsg")
            .addHeader("Authorization", "Bearer " + Authorization.userInfo.token)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException){
                println("Failure!!!!")
                message.postValue(AUTH_STATUS.FAILED)
            }
            override fun onResponse(call: Call, response: Response) {
                println("Response!!!!")
                if (response.code ==200){
                    val gson = Gson()
                    messageData = gson.fromJson(response.body!!.string(), Array<MessageBox>::class.java).toList()
                    message.postValue(AUTH_STATUS.SUCCESS)
                } else {
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}