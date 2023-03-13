package com.example.notes_app.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.ActivityMainBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.ui.dialog.AddCategoryDialogFragment
import com.example.notes_app.ui.dialog.AddNoteDialogFragment
import com.example.notes_app.ui.fragments.AboutUsFragment
import com.example.notes_app.ui.fragments.MainFragment
import com.example.notes_app.ui.fragments.NotesFragment
import com.example.notes_app.ui.fragments.SettingFragment


class MainActivity : AppCompatActivity() , OnClickNavigator, DialogViewer {

    private lateinit var binding : ActivityMainBinding
    private lateinit var m_viewModul:MyViewModel
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
        m_viewModul = ViewModelProvider(this).get(MyViewModel::class.java)

        //set toolbar
        setAppBar()

        //set MainFragment
        setMainFragment()

    }



    fun setAppBar(){
        setSupportActionBar(binding.toolbar)
    }

    fun setMainFragment(){
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , MainFragment.newInstance())
            .addToBackStack(null)
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
        }
        return true
    }


    override fun onClick_to_notesFragment(cat_id : Int) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container , NotesFragment.newInstance(cat_id))
            .addToBackStack(null)
            .commit()

    }


    //dialog viewer
    override fun add_note(cat_id: Int) {
        var dialogFragment = AddNoteDialogFragment.newInstance(cat_id)
        dialogFragment.show(supportFragmentManager,null)
    }

    override fun add_category() {
        var dialogFragment = AddCategoryDialogFragment.newInstance()
        dialogFragment.show(supportFragmentManager,null)
    }

}


