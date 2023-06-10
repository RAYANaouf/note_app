package com.example.notes_app.ui.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.ActivityMainBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.ui.dialog.AddCategoryDialogFragment
import com.example.notes_app.ui.fragments.*
import com.example.notes_app.ui.interfaces.DialogViewer
import com.example.notes_app.ui.interfaces.OnClickNavigator
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() , OnClickNavigator, DialogViewer {

    //view binding && model && page
    private lateinit var binding : ActivityMainBinding
    private lateinit var m_viewModul:MyViewModel
    private var m_page = "home"

    //accounts handler
    private lateinit var m_accountHandler : RegesterHandler
    //user email    && user  (get the user using email)
    private  var m_email = ""
    private lateinit var m_user : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar color and icon color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.statusBarColor = Color.TRANSPARENT

            // Set status bar icons to be light (dark icons)
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = flags
        }

        /*******   first of all  is connected ************/
        //set up the connection handler
        m_accountHandler = RegesterHandler(applicationContext)


        if(!m_accountHandler.isLoggin()){
            var intent = Intent(applicationContext , LogginActivity::class.java)
            startActivity(intent)
            finish()
            return@onCreate
        }else{
            //set the email
            m_email = m_accountHandler.conn_user()
        }
        /************************************************/

        //set binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set view model
        m_viewModul = ViewModelProvider(this).get(MyViewModel::class.java)


        //set toolbar
        setAppBar()

        //set MainFragment
        setMainFragment()

        //set the user info
        setUser()

        //set the drawer && navigator
        drawer_navigator_setUp()

        //set onclicks
        setOnClicks()


    }
    



    fun setAppBar(){
        setSupportActionBar(binding.toolbar)
    }

    fun setMainFragment(){
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , MainFragment.newInstance())
        fragmentTransaction.commit()

    }

    fun setUser(){
        GlobalScope.launch {
            m_user =  async {
                m_viewModul.getUserByEmail(m_email)
            }.await()

            withContext(Dispatchers.Main){
                var img_string = m_user.photo
                var img_byteArray = Base64.decode(img_string , Base64.DEFAULT)
                var img_bitmap = BitmapFactory.decodeByteArray(img_byteArray , 0 , img_byteArray.size)
                binding.mainActivityAccountSiv.setImageBitmap(img_bitmap)
            }
        }
    }

    fun drawer_navigator_setUp(){
        binding.mainActivityNavigatorNv.itemIconTintList = null

        binding.mainActivityNavigatorNv.setNavigationItemSelectedListener {

            var itemId = it.itemId

            when(itemId){
                R.id.navMenu_home->{
                    if (m_page=="home"){
                        binding.mainActivityDrawerDl.closeDrawers()
                        return@setNavigationItemSelectedListener true
                    }
                    m_page="home"
                    var fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container , MainFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                    binding.mainActivityDrawerDl.closeDrawers()
                }
                R.id.navMenu_calendar->{
                    if (m_page=="calendar"){
                        binding.mainActivityDrawerDl.closeDrawers()
                        return@setNavigationItemSelectedListener true
                    }
                    Toast.makeText(applicationContext , "calendar" , Toast.LENGTH_LONG).show()
                }
                R.id.navMenu_AI->{
                    Toast.makeText(applicationContext , "AI" , Toast.LENGTH_LONG).show()
                }
            }

            return@setNavigationItemSelectedListener true
        }

        LayoutInflater.from(this).inflate(R.layout.navigator_header , null ).findViewById<ImageView>(R.id.navigationHeader_image_iv).setImageResource(R.drawable.emoji1)

    }


    fun setOnClicks(){
        binding.mainActivityAccountSiv.setOnClickListener {
            binding.mainActivityDrawerDl.openDrawer(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.main_menu_theme->{

            }
            R.id.main_menu_share->{

            }
            R.id.main_menu_add_category->{
                add_category()
            }
            R.id.main_menu_setting->{
                var fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container , SettingFragment.newInstance())
                    .addToBackStack(null)
                fragmentTransaction.commit()
            }
            R.id.main_menu_about_us->{
                var fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container , AboutUsFragment.newInstance())
                    .addToBackStack(null)
                fragmentTransaction.commit()
            }
            R.id.main_menu_deconnect->{
                m_accountHandler.deconnecting()
                var intent = Intent(this , LogginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        var fm = supportFragmentManager
        if (fm.backStackEntryCount == 1){
            m_page = "home"
            fm.popBackStack()
        }
        else if (fm.backStackEntryCount>0){
            fm.popBackStack()
        }
        else{
            finish()
        }
    }


    override fun onClick_to_notesFragment(cat_id : Int) {
        m_page="notes"
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , NotesFragment.newInstance(cat_id))
            .addToBackStack(null)
            .commit()
    }

    override fun onClick_to_addNoteFragment(cat_id : Int) {
        m_page="addNote"
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , AddNoteFragment.newInstance(cat_id))
            .addToBackStack("add")
            .commit()
    }


    //dialog viewer

    override fun add_category() {
        var dialogFragment = AddCategoryDialogFragment.newInstance()
        dialogFragment.show(supportFragmentManager,null)
    }

}