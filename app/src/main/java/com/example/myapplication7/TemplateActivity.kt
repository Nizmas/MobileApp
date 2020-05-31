package com.example.myapplication7

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.present.HistoryAdapter
import com.example.myapplication7.present.TemplateAdapter
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.repository.TakeHistory
import com.example.myapplication7.repository.TakeTemplates
import kotlinx.android.synthetic.main.activity_history.*
import java.text.SimpleDateFormat
import java.util.*

class TemplateActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Authorization.userInfo.token == "") {
            val regIntent = Intent(this, MainActivity::class.java)
            startActivity(regIntent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templates)
        TakeTemplates.takingTemplates()

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> { Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                    TakeTemplates.message.postValue(AUTH_STATUS.UNSIGNED)}

                AUTH_STATUS.SUCCESS -> { Toast.makeText(this, "Шаблоны загружены", Toast.LENGTH_LONG).show()
                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        TemplateAdapter(TakeTemplates.templateData, this)
                    recyclerView = findViewById<RecyclerView>(R.id.tmpl_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                    TakeTemplates.message.postValue(AUTH_STATUS.UNSIGNED)
                }
                AUTH_STATUS.INCORRECT -> { Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_LONG).show()
                    TakeTemplates.message.postValue(AUTH_STATUS.UNSIGNED)}
            }
        }
        TakeTemplates.message.observe(this, msg)
    }
}
