package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.*
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*

object TakeHistory {
    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public lateinit var historyData: List<HistoryBox>

    public fun takingHistory(scoreFrom: String, timeFrom: String, timeTo: String) {
        println("###################### taking History has been launched ##############################")

        val okHttpClient = OkHttpClient()
        val payload = "{\"scorefrom\":\"$scoreFrom\", \"timefrom\":\"$timeFrom\", \"timeto\":\"$timeTo\"}"
        val body: RequestBody =
            payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        println(payload)

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/history")
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
                    historyData = gson.fromJson(response.body!!.string(), Array<HistoryBox>::class.java).toList()
                    message.postValue(AUTH_STATUS.SUCCESS)
                } else {
                    println(response.message)
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}