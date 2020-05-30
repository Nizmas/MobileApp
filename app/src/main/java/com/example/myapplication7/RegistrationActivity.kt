package com.example.myapplication7

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.repository.Registration
import com.example.myapplication7.entities.AUTH_STATUS
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val msg = Observer<AUTH_STATUS> { msg ->
            // Update the UI, in this case, a TextView.
            println("from observer")
            var tostMessage =""
            when (msg) {
                AUTH_STATUS.FAILED -> {tostMessage ="Проверьте подключение к сети"
                    AUTH_STATUS.UNSIGNED}
                AUTH_STATUS.SUCCESS -> {tostMessage ="Заявка отправлена модератору!"
                    val homeBringer = Intent(this, MainActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(homeBringer)
                    Registration.message.postValue(AUTH_STATUS.UNSIGNED)
                    }
                AUTH_STATUS.INCORRECT -> {tostMessage ="Этот адрес уже зарегистрирован"
                    AUTH_STATUS.UNSIGNED}
            }
            Toast.makeText(this, tostMessage, Toast.LENGTH_LONG).show()
        }
        Registration.message.observe(this, msg)
    }


    fun registrationUser (view: View) {
        fun String.isEmailValid(): Boolean { //проверка на почтовость
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        val emailInput = createEmail.text.toString()
        val passwordInput = createPassword.text.toString()
        val passwordCheckInput = checkPassword.text.toString()
        val nameInput = realName.text.toString()

        var e: Boolean = true
        when (e) {
            (emailInput.length == 0) -> Toast.makeText(this, "Заполните поле", Toast.LENGTH_LONG).show()
            (!emailInput.isEmailValid()) -> Toast.makeText(this, "Введите почтовый адрес", Toast.LENGTH_LONG).show()
            (passwordInput.length < 4) -> Toast.makeText(this, "Длина пароля не менее 4 символов", Toast.LENGTH_LONG).show()
            (passwordCheckInput != passwordInput) -> Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
            (nameInput.length < 3) -> Toast.makeText(this, "Слишком короткое имя", Toast.LENGTH_LONG).show()
            else -> e = false
        }

        if (!e) {
            var registrator = Registration
            registrator.regUser(emailInput, passwordInput, passwordCheckInput, nameInput)
        }

    }
}