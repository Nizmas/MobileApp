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
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.repository.TakeHistory
import kotlinx.android.synthetic.main.activity_history.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var startDate = String()
    var endDate = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Authorization.userInfo.token == "") {
            val regIntent = Intent(this, MainActivity::class.java)
            startActivity(regIntent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> { Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                    TakeHistory.message.postValue(AUTH_STATUS.UNSIGNED)}

                AUTH_STATUS.SUCCESS -> { Toast.makeText(this, "Jr", Toast.LENGTH_LONG).show()
                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        HistoryAdapter(TakeHistory.historyData)
                    recyclerView = findViewById<RecyclerView>(R.id.hist_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                    TakeHistory.message.postValue(AUTH_STATUS.UNSIGNED)
                }
                AUTH_STATUS.INCORRECT -> { Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_LONG).show()
                    TakeHistory.message.postValue(AUTH_STATUS.UNSIGNED)}
            }
        }
        TakeHistory.message.observe(this, msg)
    }

    fun takeHistory (view: View) {
        TakeHistory.takingHistory(setOnSelect.selectedNum, startDate, endDate)
    }

    fun firstDate (view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day  ->
            var calend = Calendar.getInstance()
            calend.set(year, month, day)
            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            startDate = dateFormat.format(calend.time)

            @SuppressLint("SetTextI18n")
            dateFrom.text = day.toString()  +"."+ (month+1).toString()  +"."+ year.toString()
        }, year, month, day)
        dpd.show()
    }

    fun secondDate (view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
            var calend = Calendar.getInstance()
            calend.set(year, month, day)
            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            endDate = dateFormat.format(calend.time)
            @SuppressLint("SetTextI18n")
            dateTo.text = day.toString()  +"."+ (month+1).toString()  +"."+ year.toString()
        }, year, month, day)
        dpd.show()
    }
}
