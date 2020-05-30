package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.entities.ScoreList
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

object Scores {
    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public lateinit var scoreData: List<ScoreList>
    public fun scoresShow() {
        println("###################### scoreList has been launched ##############################")

        val okHttpClient = OkHttpClient()
        println("1")

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/showmoney")
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
                    println("************** Body Here *************")
                    var gsonInstance = Gson()
                    scoreData = gsonInstance.fromJson(response.body!!.string(), Array<ScoreList>::class.java).toList()
                    message.postValue(AUTH_STATUS.SUCCESS)
                } else {
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}