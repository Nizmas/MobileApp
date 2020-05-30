package com.example.myapplication7

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.entities.AUTH_STATUS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            var tostMessage =""
            when (msg) {
                AUTH_STATUS.FAILED -> tostMessage ="Проверьте подключение к сети"
                AUTH_STATUS.SUCCESS -> { tostMessage ="Добро пожаловать"
                    val enterIntent = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(enterIntent)}
                AUTH_STATUS.INCORRECT -> tostMessage ="Некорретное имя пользователя или пароль"
            }
            Toast.makeText(this, tostMessage, Toast.LENGTH_LONG).show()
        }
        Authorization.message.observe(this, msg)
    }

    fun signIn (view: View) {

        fun String.isEmailValid(): Boolean { //проверка на почтовость
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        val emailInput = editEmail.text.toString()
        val passwordInput = editPassword.text.toString()

        var e = true
        when (e) {
            (emailInput.length == 0) -> Toast.makeText(this, "Заполните поле", Toast.LENGTH_SHORT).show()
            (!emailInput.isEmailValid()) -> Toast.makeText(this, "Введите почтовый адрес", Toast.LENGTH_SHORT).show()
            (passwordInput.length < 3) -> Toast.makeText(this, "Длина пароля не менее 4 символов", Toast.LENGTH_SHORT).show()
            else -> e = false
        }

        if (!e) {
            var authorizator = Authorization
            authorizator.getUser(emailInput, passwordInput)
        }
    }

    fun startRegistration (view: View) {
        editEmail.setText("")
        editPassword.setText("")
        val regIntent = Intent(this, RegistrationActivity::class.java) // создаём объект с описанием нового окна ява
        startActivity(regIntent) // функция запуска нового "активити". т.е. второго окна
    }
}
