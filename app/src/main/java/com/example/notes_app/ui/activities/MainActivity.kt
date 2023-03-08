package com.example.notes_app.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.ActivityMainBinding
import com.example.notes_app.modul.MyViewModul
import com.example.notes_app.ui.dialog.AddCategoryDialogFragment
import com.example.notes_app.ui.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var m_viewModul:MyViewModul

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



}


