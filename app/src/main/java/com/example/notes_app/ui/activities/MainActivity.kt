package com.example.notes_app.ui.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Base64
import android.view.*
import android.view.View.OnTouchListener
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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
import java.util.*


class MainActivity : AppCompatActivity() , OnClickNavigator, DialogViewer , DatePickerDialog.OnDateSetListener{

    //view binding && model && page
    private lateinit var binding : ActivityMainBinding
    private lateinit var m_viewModul:MyViewModel
    private var m_page = "home"

    //accounts handler
    private lateinit var m_accountHandler : RegesterHandler
    //user email    && user  (get the user using email)
    private  var m_email = ""
    private lateinit var m_user : User

    //my rate
    private var m_rate=0F

    //fragment
    private lateinit var m_fragment :Fragment

    //for search fragment
    private var filter = "text"

    private lateinit var m_datePicker :  DatePickerDialog


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

        //register item with context menu
        registerItemWithContextMenu()

        //set onclicks
        setOnClicks()


    }




    fun setAppBar(){
        setSupportActionBar(binding.toolbar)
    }

    fun setMainFragment(){
        var fragment = MainFragment.newInstance(m_email)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , fragment)
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
                R.id.navMenu_profile->{
                    binding.mainActivityDrawerDl.closeDrawers()

                    if (m_page=="profile"){
                        return@setNavigationItemSelectedListener true
                    }

                    m_fragment =ProfileFragment.newInstance()
                    m_page="profile"
                    var fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container , m_fragment )
                        .addToBackStack(null)
                        .commit()
                }
                R.id.navMenu_home->{
                    binding.mainActivityDrawerDl.closeDrawers()

                    if (m_page=="home"){
                        return@setNavigationItemSelectedListener true
                    }

                    m_fragment =MainFragment.newInstance(m_email)
                    m_page="home"
                    var fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container , m_fragment )
                        .addToBackStack(null)
                        .commit()
                }
                R.id.navMenu_diary->{
                    binding.mainActivityDrawerDl.closeDrawers()

                    if (m_page=="diary"){
                        return@setNavigationItemSelectedListener true
                    }

                    m_page="setting"

                    m_fragment = DiariesFragment.newInstance(-1)

                    var fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container , m_fragment )
                        .addToBackStack(null)
                    fragmentTransaction.commit()
                }
                R.id.navMenu_calendar->{
                    binding.mainActivityDrawerDl.closeDrawers()

                    if (m_page=="calendar"){
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

    }

    fun registerItemWithContextMenu(){
        registerForContextMenu(binding.mainActivityFilterSiv)
    }

    fun setOnClicks(){
        binding.mainActivityAccountSiv.setOnClickListener {
            binding.mainActivityDrawerDl.openDrawer(GravityCompat.START)
        }

        binding.mainActivityReturnSiv.setOnClickListener {
            var fm = supportFragmentManager
            fm.popBackStack()
        }

        binding.mainActivityDayRateMrb.setOnRatingChangeListener { ratingBar, rating ->
            changeRate(binding.mainActivityDayRateMrb.rating)
        }


        binding.mainActivitySearchBarTiet.addTextChangedListener ( object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



//                Toast.makeText(baseContext , "chnge : DiariesFragment" , Toast.LENGTH_LONG).show()
                var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is DiariesFragment){
                        fragment.search(binding.mainActivitySearchBarTiet.text.toString() , filter)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        } )
    }

    /*******************************************   set the option menu **********************************************/
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
                m_page="setting"
                var fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container , SettingFragment.newInstance())
                    .addToBackStack(null)
                fragmentTransaction.commit()
            }
            R.id.main_menu_about_us->{
                m_page="about_us"
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {


        //reset m_email m_user the profile photo ...
        m_email = m_accountHandler.conn_user()
        setUser()

        var theme_item_menu = menu?.findItem(R.id.main_menu_theme)
        var share_item_menu = menu?.findItem(R.id.main_menu_share)

        //*********************  set invisible ************************//

        //menu
        theme_item_menu?.isVisible=false
        share_item_menu?.isVisible=false

        //tools (in the tool-bar)
        binding.mainActivityReturnSiv.visibility    = View.INVISIBLE
        binding.mainActivityAccountSiv.visibility   = View.INVISIBLE
        binding.mainActivityFilterSiv.visibility    = View.INVISIBLE
        binding.mainActivitySearchBarTil.visibility = View.INVISIBLE
        binding.mainActivityDayRateMrb.isVisible    = false
        binding.mainActivityDayRateMrb.setIsIndicator(true)

        //*************************************************************//

        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment is MainFragment){
            //menu
            share_item_menu?.isVisible=true

            //tools (in the tool-bar)
            binding.mainActivityAccountSiv.visibility   = View.VISIBLE

        }
        if(currentFragment is AddNoteFragment){

            //visible
            binding.mainActivityReturnSiv.visibility=View.VISIBLE
            binding.mainActivityDayRateMrb.isVisible=true
            binding.mainActivityDayRateMrb.rating=m_rate
            binding.mainActivityDayRateMrb.setIsIndicator(false)


        }
        else if(currentFragment is DiaryFragment){

            //visible
            binding.mainActivityReturnSiv.visibility=View.VISIBLE
            binding.mainActivityDayRateMrb.isVisible=true
            binding.mainActivityDayRateMrb.rating=m_rate

        }
        else if(currentFragment is DiariesFragment){

            binding.mainActivityReturnSiv.visibility=View.VISIBLE
            binding.mainActivitySearchBarTil.visibility=View.VISIBLE
            binding.mainActivityFilterSiv.visibility = View.VISIBLE

        }

        return true
    }

    /****************************************************************************************************************/

    /*******************************************   set the context menu **********************************************/
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.search_filter_menu , menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.searchFilterMenu_title_item->{
                filter="title"
                Toast.makeText(baseContext , "search by title" , Toast.LENGTH_SHORT).show()
            }
            R.id.searchFilterMenu_hashtag_item->{
                filter="hashtag"
                Toast.makeText(baseContext , "search by hashtag" , Toast.LENGTH_SHORT).show()
            }
            R.id.searchFilterMenu_content_item->{
                filter="content"
                Toast.makeText(baseContext , "search by content" , Toast.LENGTH_SHORT).show()
            }
            R.id.searchFilterMenu_date_item->{
//                Toast.makeText(baseContext , "search by date" , Toast.LENGTH_SHORT).show()
                var calendar = Calendar.getInstance()
                m_datePicker = DatePickerDialog(this@MainActivity , this , calendar[Calendar.YEAR] , calendar[Calendar.MONDAY] , calendar[Calendar.DAY_OF_MONTH])
                m_datePicker.show()

            }
        }
        return true
    }
    /*****************************************************************************************************************/


    /***********************************   set the action with  return btn ******************************************/
    override fun onBackPressed() {
        var fm = supportFragmentManager
        if (fm.backStackEntryCount == 1){
            m_page = "home"
            m_fragment =fm.findFragmentById(R.id.fragment_container) ?: MainFragment()
            if ( m_fragment !is MainFragment){
                fm.popBackStack()
            }
        }
        else if (fm.backStackEntryCount>0){
            fm.popBackStack()
        }
        else{
            finish()
        }
    }
    /****************************************************************************************************************/




    /*****************************************   edit fragment (response) *******************************************/
    fun changeRate(rating : Float){
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is AddNoteFragment){
            fragment.setRate(rating)
        }
    }
    /****************************************************************************************************************/



    override fun onClick_to_notesFragment(cat_id : Long) {
        m_page="notes"

        m_fragment = DiariesFragment.newInstance(cat_id)

        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , m_fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClick_to_addNoteFragment(rating : Float) {
        m_rate=rating
        m_page="addNote"

        m_fragment =AddNoteFragment.newInstance(rating)

        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , m_fragment)
            .addToBackStack("add")
            .commit()
    }

    override fun onClick_to_diaryFragment(diary_id: Long , rate : Float) {

        m_fragment = DiaryFragment.newInstance(diary_id)

        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container , m_fragment )
        ft.addToBackStack("dairy")
        ft.commit()
        binding.mainActivityDayRateMrb.rating = rate
    }

    //dialog viewer

    override fun add_category() {
        var dialogFragment = AddCategoryDialogFragment.newInstance()
        dialogFragment.show(supportFragmentManager,null)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.mainActivitySearchBarTiet.setText("${month+1}/${dayOfMonth}/${year}")
        var fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment is DiariesFragment){
            fragment.search("${month+1}/${dayOfMonth}/${year}" , "date")
        }
    }

}