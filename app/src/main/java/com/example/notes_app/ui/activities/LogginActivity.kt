package com.example.notes_app.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.ActivityLoggingBinding
import com.example.notes_app.modul.MyViewModel
import kotlinx.coroutines.*

class LogginActivity : AppCompatActivity() {

    private lateinit var m_binding: ActivityLoggingBinding
    private lateinit var m_connHandler : RegesterHandler
    private lateinit var m_viewModel : MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set the binding
        m_binding = ActivityLoggingBinding.inflate(layoutInflater)
        setContentView(m_binding.root)

        //set the view model
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //set up connection handler
        m_connHandler= RegesterHandler(this)

        // Set status bar color and icon color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.statusBarColor = Color.parseColor("#E9F8F8")

            // Set status bar icons to be light (dark icons)
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = flags
        }

        setOnClicks()


    }


    fun setOnClicks(){

        m_binding.logginActivityCreatAccountTv.setOnClickListener {
            var intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        m_binding.logginActivityNextBtn.setOnClickListener {

            if (!validation()){
                Toast.makeText(applicationContext , "enter user name and the passwordD" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            m_binding.progressBar.visibility=View.VISIBLE
            m_binding.logginActivityNextBtn.visibility=View.INVISIBLE

            GlobalScope.launch {
                delay(1000)
                var user = m_viewModel.getUserByEmail(m_binding.logginActivityUserMailTiet.text.toString())

                withContext(Dispatchers.Main){
                    if(user==null) {
                        Toast.makeText(applicationContext, "user name :${m_binding.logginActivityUserMailTiet.text.toString()}  \n didnt exist", Toast.LENGTH_LONG).show()
                        m_binding.progressBar.visibility=View.INVISIBLE
                        m_binding.logginActivityNextBtn.visibility=View.VISIBLE
                    }else if(user.password != m_binding.logginActivityPasswordTiet.text.toString()){
                        Toast.makeText(applicationContext, " user name or password is incorrect", Toast.LENGTH_LONG).show()
                        m_binding.progressBar.visibility=View.INVISIBLE
                        m_binding.logginActivityNextBtn.visibility=View.VISIBLE
                    }else{
                        m_connHandler.connecting(m_binding.logginActivityUserMailTiet.text.toString())
                        var intent = Intent(baseContext,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    fun validation():Boolean{
        if (m_binding.logginActivityUserMailTiet.text.toString()==""||m_binding.logginActivityPasswordTiet.text.toString()=="")
            return false
        return true
    }


}