package com.example.notes_app.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentMainBinding
import com.example.notes_app.modul.MyViewModul
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.MyDatabase
import com.example.notes_app.recyclers.adapter.Category_adaptes
import com.example.notes_app.recyclers.item_decoration.CategoryDecoration
import com.example.notes_app.ui.dialog.OnClickNavigator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream


class MainFragment : Fragment() {

    private lateinit var binding  : FragmentMainBinding
    private lateinit var m_viewModel : MyViewModul
    private lateinit var m_categories : List<Category>
    private lateinit var m_onClickNavigator : OnClickNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickNavigator){
            m_onClickNavigator = context
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set the view model
        m_viewModel = ViewModelProvider(this).get(MyViewModul::class.java)

        //set the view binding
        binding  = FragmentMainBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var adapter = Category_adaptes(this, m_viewModel , m_onClickNavigator)
        binding.categoryRecycler.adapter = adapter
        binding.categoryRecycler.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.categoryRecycler.addItemDecoration(CategoryDecoration(requireContext(),3f))





        m_viewModel.getAllCategories().observe(viewLifecycleOwner){
            Toast.makeText(context , "the size : ${it.size}" , Toast.LENGTH_LONG).show()
            if (it.size>=3)
                setAllCategories(it)
        }

        setPieChart()



    }

    fun setAllCategories(categories:List<Category>){


        var cat = categories[0]
        var byteArray_img : ByteArray = Base64.decode(cat.image , Base64.DEFAULT)
        var bitmap_img = BitmapFactory.decodeByteArray(byteArray_img , 0 , byteArray_img.size)

        binding.goalImg1.setImageBitmap(bitmap_img)
        binding.goalName1.setText("${cat.name}")


        cat = categories[1]
        byteArray_img = Base64.decode(cat.image , Base64.DEFAULT)
        bitmap_img = BitmapFactory.decodeByteArray(byteArray_img , 0 , byteArray_img.size)

        binding.goalImg2.setImageBitmap(bitmap_img)
        binding.goalName2.setText("${cat.name}")

        cat = categories[2]
        byteArray_img = Base64.decode(cat.image , Base64.DEFAULT)
        bitmap_img = BitmapFactory.decodeByteArray(byteArray_img , 0 , byteArray_img.size)

        binding.goalImg3.setImageBitmap(bitmap_img)
        binding.goalName3.setText("${cat.name}")

    }

    fun setPieChart(){

        var list : ArrayList<PieEntry> = ArrayList()


//        for (cat in m_categories){
//            list.add(PieEntry(100f,"${cat.name}"))
//        }
        //charge the list

        list.add(PieEntry(100f,"goal"))
        list.add(PieEntry(89f,"to do"))
        list.add(PieEntry(20f,"to learn"))
        list.add(PieEntry(50f,"to build"))
        list.add(PieEntry(35f,"else"))

        //creat the pieDataSet
        var pieDataset = PieDataSet(list,"list")

        //set colors to the pie chart  ==> two steps (2)
        //1. create a list of colors
        var colors = listOf(Color.parseColor("#0024FF"),
            Color.parseColor("#5C17A4"),
            Color.parseColor("#81127F"),
            Color.parseColor("#C1093F"),
            Color.parseColor("#FD0004"),
        )
        //2.set it to pieDataSet
        pieDataset.setColors(colors)
        //hide the text properties and its values
        pieDataset.valueTextSize = 10f
        pieDataset.valueTextColor = Color.BLACK

        // to make the label of each slice hidden you can set the formatter to null  noti
        pieDataset.valueFormatter = null

        //create pie Data and past the pieDataSet to its constructor
        var pieData = PieData(pieDataset)
        //to hide the value
        pieData.setDrawValues(false)

//         to ajust the label you can edit the Foramatter
//        pieData.setValueFormatter(object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return if (value == 100f) "my Goal"
//                else if (value == 20f) "my To Do"
//                else "my To Learn"
//            }
//        })



        //set the data of the piechart  using pieData we just create
        binding.mainPieChart.data = pieData
        //set description of the graph
        binding.mainPieChart.description.text = " "
        //set center title
        binding.mainPieChart.centerText = "Notes"
        //hide the lable
        binding.mainPieChart.setDrawEntryLabels(false)
        //set the color of hole to transparent #xxxxxx00
        binding.mainPieChart.setHoleColor(Color.parseColor("#00ffffff"))
        //set the hole radius to x% of chart radius
        binding.mainPieChart.holeRadius = 70f
        //set the transparent circle radius to x% of chart radius
        binding.mainPieChart.transparentCircleRadius = 80f
        //set the animation to 2 second
        binding.mainPieChart.animateY(2000)


        //customize Legend using the legend (وسيلة الايضاح)
        binding.mainPieChart.legend.apply {
            setDrawInside(false)
            verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}