package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.entities.ScoreList
import com.example.myapplication7.entities.User
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object SenderTemplate {
    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public fun startSending(templateName: String, scoreFrom: String, scoreTo: String, takerName: String, howMuch: String) {
        println("###################### startSending has been launched ##############################")
        val okHttpClient = OkHttpClient()
        val payload = "{\"templateName\":\"$templateName\", \"ScoreFrom\":\"$scoreFrom\", \"ScoreTo\":\"$scoreTo\", \"TakerName\":\"$takerName\", \"HowMuch\":$howMuch}"
        println(payload)

        val body: RequestBody =
            payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/newtemplate")
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
                    message.postValue(AUTH_STATUS.SUCCESS)
                    println("Everything is goo")
                } else {
                    println("Everything is mad")
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}