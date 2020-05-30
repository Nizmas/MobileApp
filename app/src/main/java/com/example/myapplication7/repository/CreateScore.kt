package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.AUTH_STATUS
import okhttp3.*
import java.io.IOException

object CreateScore {

    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public fun newScore () {
        println("###################### newScore has been launched ##############################")

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/newscore")
            .addHeader("Authorization", "Bearer " + Authorization.userInfo.token)
            .get()
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
                } else {
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}