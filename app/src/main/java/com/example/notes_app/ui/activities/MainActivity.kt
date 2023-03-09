package com.example.notes_app.ui.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.ActivityMainBinding
import com.example.notes_app.modul.MyViewModul
import com.example.notes_app.modul.data_classes.Category
import com.example.notes_app.ui.dialog.AddCategoryDialogFragment
import com.example.notes_app.ui.dialog.OnClickNavigator
import com.example.notes_app.ui.fragments.MainFragment
import java.io.ByteArrayOutputStream
import java.util.Base64
import kotlin.random.Random

class MainActivity : AppCompatActivity() , OnClickNavigator {

    private lateinit var binding : ActivityMainBinding
    private lateinit var m_viewModul:MyViewModul
    private  var m_uri : Uri? = null

    //permission
    private var external_storage_permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(baseContext , "true" , Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(baseContext , "false" , Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set view model
        m_viewModul = ViewModelProvider(this).get(MyViewModul::class.java)

        //set toolbar
        setAppBar()

        //set MainFragment
        setMainFragment()

    }



    fun setAppBar(){
        setSupportActionBar(binding.toolbar)
    }

    fun setMainFragment(){
        var mainFragment = MainFragment.newInstance()
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , mainFragment)
        fragmentTransaction.commit()

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
                var dialogFragment = AddCategoryDialogFragment.newInstance()
                dialogFragment.show(supportFragmentManager,null)
            }
            R.id.main_menu_setting->{

            }
            R.id.main_menu_about_us->{

            }
        }
        return true
    }

    override fun onClickGetPicture(): Uri? {
        return null
    }

    override fun onClick_to_notesFragment() {

    }

    override fun onClick_addCategory(name : String , des : String) {

        var bitmap_drawable =  ContextCompat.getDrawable(baseContext , R.drawable.c0) as (BitmapDrawable)
        var bitmap = bitmap_drawable.bitmap
        var byteArrayOutputStream:ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream)
        var byteArray = byteArrayOutputStream.toByteArray()
        var img_string = android.util.Base64.encodeToString(byteArray , android.util.Base64.DEFAULT)
        var id = (0 .. 850000).random()
        m_viewModul.addCategory(Category(id,img_string,name , des))
    }

}


