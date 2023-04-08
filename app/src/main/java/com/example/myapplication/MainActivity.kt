package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.api.YandexAPI
import com.example.myapplication.model.Answer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val BASE_URL = "https://predictor.yandex.net"
    val KEY = "pdct.1.1.20230408T133402Z.d442f7df36e78e57.a5e55d2b9a20ceaea12e2fbac308115f21718fa6"
    val LANG = "en"
    val LIMIT = 5

    lateinit var textView: TextView
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        editText = findViewById(R.id.editText)

        editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                // делаем здесь вызов в Яндекс Предиктор
                doRequest()
            }
        })
    }

   fun doRequest() {
       val retrofit: Retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
       val api: YandexAPI = retrofit.create(YandexAPI::class.java)
       api.complete(KEY, editText.text.toString(),LANG, LIMIT).enqueue(object: Callback<Answer> {
           override fun onResponse(call: Call<Answer>, response: Response<Answer>) {
                if(response.code() == 200) {
                    val result = response.body()?.text
                    if (result != null) {
                        textView.text = result.joinToString("\n")
                    }
                }
           }
           override fun onFailure(call: Call<Answer>, t: Throwable) {
                Log.d("RRR",t.message.toString())
           }
       })
   }
}