package com.example.notes_app.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.ActivitySignInBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.example.notes_app.modul.room_database.data_classes.User
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class SignUpActivity : AppCompatActivity() {

    private lateinit var m_binding : ActivitySignInBinding
    private lateinit var m_viewModul: MyViewModel
    private lateinit var m_connHandler : RegesterHandler

    // user photo
    var m_photo = ""

    /**********  activity  lanchers ***********************/

    //permission
    private var external_storage_permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(applicationContext , "read external storage is allowed " , Toast.LENGTH_LONG).show()
            getContent.launch("image/*")
        }else{
            Toast.makeText(applicationContext , "cant access to your gallery (to get a picture)" , Toast.LENGTH_LONG).show()
        }
    }

    //get picture
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            m_binding.SignUpActivityUserPhotoSiv.setImageURI(uri)
            val imageStream = this.contentResolver.openInputStream(uri)
            val imageData = imageStream?.readBytes()

            if (imageData!=null){
                var imageBitmap = BitmapFactory.decodeByteArray(imageData , 0 , imageData.size)
                var imageByteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG , 50 , imageByteArrayOutputStream)
                var imageByteArray = imageByteArrayOutputStream.toByteArray()
                val base64Image = android.util.Base64.encodeToString(imageByteArray,android.util.Base64.DEFAULT)
                m_photo = base64Image
            }
        }
    }

    /******************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

        //set up the bindong
        m_binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(m_binding.root)

        //set view model
        m_viewModul = ViewModelProvider(this).get(MyViewModel::class.java)

        //set connection handler
        m_connHandler = RegesterHandler(this)

        //set onClicks
        setOnClicks()
    }

    fun setOnClicks(){

        //have account text
        m_binding.SignUpActivityHaveAccountTv.setOnClickListener {
            var intent = Intent(this , LogginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //next button
        m_binding.SignUpActivityNextBtn.setOnClickListener {
            if (!validation()){
                return@setOnClickListener
            }
            var first_name = m_binding.SignUpActivityFirstNameTiet.text.toString()
            var last_name = m_binding.SignUpActivityLastNameTiet.text.toString()
            var email     = m_binding.SignUpActivityEmailTiet.text.toString()
            var password = m_binding.SignUpActivityPasswordTiet.text.toString()
            m_viewModul.addUser(User(f_name = first_name , l_name = last_name , email =  email , password = password , photo = m_photo))
            m_connHandler.connecting(email)

            var intent  = Intent (applicationContext , MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        //user photo
        m_binding.SignUpActivityUserPhotoSiv.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(this.applicationContext , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                    getContent.launch("image/*")
                }
                else->{
                    external_storage_permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }


    }

    fun validation(): Boolean{
        if (m_binding.SignUpActivityFirstNameTiet.text.toString()==""){
            Toast.makeText(this , "enter your first name" , Toast.LENGTH_LONG).show()
            return false
        }
        else if(m_binding.SignUpActivityLastNameTiet.text.toString()==""){
            Toast.makeText(this , "enter your last name" , Toast.LENGTH_LONG).show()
            return false
        }
        else if (m_binding.SignUpActivityEmailTiet.text.toString()==""){
            Toast.makeText(this , "enter your email" , Toast.LENGTH_LONG).show()
            return false
        }
        else if (m_binding.SignUpActivityPasswordTiet.editableText.length < 8){
            Toast.makeText(this,"your password should be more that 8 characters" , Toast.LENGTH_LONG).show()
            return false
        }
        else if(m_photo==""){
            Toast.makeText(this,"enter an image " , Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}