package com.example.myapplication7

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.repository.SaveSettings
import kotlinx.android.synthetic.main.activity_registration.*

class SettingsActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        textRegistration.text= "Настройка"
        registration_button.text = "Сохранить изменения"
        registration_button.setOnClickListener {acceptChanges()}

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            var tostMessage =""
            when (msg) {
                AUTH_STATUS.FAILED -> {tostMessage ="Проверьте подключение к сети"
                    AUTH_STATUS.UNSIGNED}
                AUTH_STATUS.SUCCESS -> {tostMessage ="Изменения успешно сохранены"
                    val homeBringer = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(homeBringer)
                    SaveSettings.message.postValue(AUTH_STATUS.UNSIGNED)}
                AUTH_STATUS.INCORRECT -> {tostMessage ="Этот адрес уже зарегистрирован"
                    AUTH_STATUS.UNSIGNED}
            }
            Toast.makeText(this, tostMessage, Toast.LENGTH_LONG).show()
        }
        SaveSettings.message.observe(this, msg)
    }

    fun acceptChanges () {
        fun String.isEmailValid(): Boolean {
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        if ((createEmail.text.toString().isEmailValid()
            || realName.text.toString().length > 3)) {
            SaveSettings.savingName(createEmail.text.toString(), realName.text.toString())
            Toast.makeText(this, "Меняем имя...", Toast.LENGTH_SHORT).show()
        }
        if (createPassword.text.toString() == checkPassword.text.toString() && createPassword.text.toString().length > 3) {
            SaveSettings.savingPassword(createPassword.text.toString(), checkPassword.text.toString())
            Toast.makeText(this, "Меняем пароль...", Toast.LENGTH_SHORT).show()
        }
    }
}