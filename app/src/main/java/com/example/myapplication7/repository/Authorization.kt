package com.example.myapplication7.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.entities.User
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object Authorization {
    val message: MutableLiveData<AUTH_STATUS> by lazy {
        MutableLiveData<AUTH_STATUS>()
    }

    public var userInfo =  User ("", "")

    public fun getUser(email: String, password: String) {
        println("###################### getUser has been launched ##############################")

        val okHttpClient = OkHttpClient()
        val payload = "{\"username\":\"$email\", \"password\":\"$password\"}"
        val body: RequestBody =
            payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/api/token")
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
                    userInfo = gson.fromJson<User>(response.body!!.string(), User::class.java)
                    userInfo.email=email
                    message.postValue(AUTH_STATUS.SUCCESS)
                } else {
                    message.postValue(AUTH_STATUS.INCORRECT)
                }
            }
        })
    }
}
