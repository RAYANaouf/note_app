package com.example.notes_app.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentMainBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.recyclers.adapter.Categories_adaptes
import com.example.notes_app.recyclers.adapter.DailyAdapter
import com.example.notes_app.recyclers.adapter.MainCategoriesAdapter
import com.example.notes_app.recyclers.item_decoration.CategoryDecoration
import com.example.notes_app.recyclers.item_decoration.MainCategoryDecoration
import com.example.notes_app.ui.interfaces.OnClickNavigator
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class MainFragment : Fragment() {

    private lateinit var binding  : FragmentMainBinding
    private lateinit var m_viewModel : MyViewModel
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
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //set the view binding
        binding  = FragmentMainBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //set the main category adapter
        var main_category_adapter = MainCategoriesAdapter(this , m_viewModel , object : MainCategoriesAdapter.OnClickListener{
            override fun onClick_mainCategory(cat_id : Int) {
                m_onClickNavigator.onClick_to_notesFragment(cat_id)
            }
        })
        binding.mainCategoryRv.adapter = main_category_adapter
        binding.mainCategoryRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.mainCategoryRv.addItemDecoration(MainCategoryDecoration(requireContext() , 15f,5f))


        //set the category adapter
//        var adapter = Categories_adaptes(this, m_viewModel , object : Categories_adaptes.OnClickListener{
//            override fun onClick_category(cat_id: Int) {
//                m_onClickNavigator.onClick_to_notesFragment(cat_id)
//            }
//        })
        var adapter = DailyAdapter(object : DailyAdapterListener{
            override fun onClick() {
                m_onClickNavigator.onClick_to_addNoteFragment(0)
            }

        })

        binding.categoryRecycler.adapter = adapter
        binding.categoryRecycler.layoutManager= LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
//        binding.categoryRecycler.addItemDecoration(CategoryDecoration(requireContext(),15f , 5f))


        setPieChart()


    }

    fun setUserData(){

    }

    fun setPieChart(){

        var list : ArrayList<PieEntry> = ArrayList()



//        for (cat in m_categories){
//            list.add(PieEntry(100f,"${cat.name}"))
//        }
        //charge the list


        list.add(PieEntry(100f,"spring"))
        list.add(PieEntry(89f,"summer"))
        list.add(PieEntry(20f,"Autumn"))
        list.add(PieEntry(50f,"winter"))

        //creat the pieDataSet
        var pieDataset = PieDataSet(list,"")

        //set colors to the pie chart  ==> two steps (2)
        //1. create a list of colors
        var colors = listOf(Color.parseColor("#0024FF"),
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

    interface DailyAdapterListener{
        fun onClick()
    }


}