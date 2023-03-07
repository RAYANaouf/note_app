package com.example.notes_app.modul.room_database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notes_app.R
import com.example.notes_app.modul.data_classes.Category
import com.example.notes_app.modul.data_classes.Note
import com.example.notes_app.modul.room_database.DAO.CategoryDAO
import com.example.notes_app.modul.room_database.DAO.NoteDAO
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = arrayOf(Category::class , Note::class) , version = 1 , exportSchema = false)
abstract class MyDatabase: RoomDatabase() {



    abstract fun noteDAO():NoteDAO
    abstract fun categoryDAO():CategoryDAO

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.

        private lateinit var m_context :Context

        private val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor : ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Volatile
        private var INSTANCE : MyDatabase? = null


        fun getDatabase(context: Context):MyDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                if (INSTANCE == null ){
                    m_context = context
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "note_databaseV9"
                    ).addCallback(sRoomDatabaseCallback).build()
                INSTANCE = instance
                }
                INSTANCE!!
            }
        }

        private var sRoomDatabaseCallback : Callback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                databaseWriteExecutor.execute {

                    var d1 = ContextCompat.getDrawable(m_context , R.drawable.c1)
                    var d2 = ContextCompat.getDrawable(m_context , R.drawable.c2)
                    var d3 = ContextCompat.getDrawable(m_context , R.drawable.c3)

                    // Convert the drawable to a Bitmap
                    // Convert the drawable to a Bitmap
                    var bitmap1 = (d1 as BitmapDrawable).bitmap
                    var bitmap2 = (d2 as BitmapDrawable).bitmap
                    var bitmap3 = (d3 as BitmapDrawable).bitmap

                    var bitmaps : ArrayList<Bitmap> = ArrayList()
                    bitmaps.add(bitmap1)
                    bitmaps.add(bitmap2)
                    bitmaps.add(bitmap3)


                    //declaration needed
                    var byteArrayOutputStream : ByteArrayOutputStream
                    var byteArray : ByteArray
                    var imageString: String

                    var i = 0
                    for (bitmap in bitmaps) {
                        byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        byteArray = byteArrayOutputStream.toByteArray()
                        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)


                        var categoryDAO = INSTANCE!!.categoryDAO()

                        if (i == 0)
                            categoryDAO.addCategory(Category(i,imageString,"Goals","you can write notes about your future goals "))
                        else if(i==1)
                            categoryDAO.addCategory(Category(i,imageString,"to do","write down what you wanna do in the future to remember it later"))
                        else if(i==2)
                            categoryDAO.addCategory(Category(i,imageString,"to learn" , "write down what you wanna learn to grow your knowledge in the future"))

                        i++
                    }

                }
            }
        }

    }

}