package com.example.notes_app.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.FragmentMainBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.recyclers.adapter.Categories_adaptes
import com.example.notes_app.recyclers.adapter.DailyAdapter
import com.example.notes_app.recyclers.adapter.MainCategoriesAdapter
import com.example.notes_app.recyclers.item_decoration.CategoryDecoration
import com.example.notes_app.recyclers.item_decoration.MainCategoryDecoration
import com.example.notes_app.ui.dialog.CheckPasswordDialog
import com.example.notes_app.ui.interfaces.OnClickNavigator
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*
import java.util.*


class MainFragment : Fragment() {

    private lateinit var binding  : FragmentMainBinding
    private lateinit var m_viewModel : MyViewModel

    //accounts handler
    private lateinit var m_accountHandler : RegesterHandler

    private lateinit var m_onClickNavigator : OnClickNavigator
    private  var m_email  = ""
    private lateinit var m_user : User
    private var is_active = false

    //the diary i click
    private  var m_rate:Float = 0F

    // the dalog check
    private lateinit var m_dialog: CheckPasswordDialog

    private var m_selected_dairy=0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickNavigator){
            m_onClickNavigator = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            m_email = it?.getString("email" ) ?: ""
        }

        //set the view model
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //set up the connection handler
        m_accountHandler = RegesterHandler(requireContext())

        runBlocking {
            launch {
                m_user = m_viewModel.getUserByEmail(m_email)
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set the view binding
        binding  = FragmentMainBinding.inflate(inflater)

        //to reset each time and when we return back
        is_active        = m_accountHandler.isActive()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //set the category adapter
//        var adapter = Categories_adaptes(this, m_viewModel , object : Categories_adaptes.OnClickListener{
//            override fun onClick_category(cat_id: Int) {
//                m_onClickNavigator.onClick_to_notesFragment(cat_id)
//            }
//        })

//        setPieChart()

        setViewIn()

        set_animation()

        setOnClicks()

    }

    override fun onStart() {
        super.onStart()

        requireActivity().invalidateOptionsMenu()


        /****************   reset daily rate    ***************/
        binding.MainFragmentRateDayMrb.rating = 3f
        binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji3)

    }


//    fun setPieChart(){
//
//        var list : ArrayList<PieEntry> = ArrayList()
//
//
//
////        for (cat in m_categories){
////            list.add(PieEntry(100f,"${cat.name}"))
////        }
//        //charge the list
//
//
//        list.add(PieEntry(100f,"spring"))
//        list.add(PieEntry(89f,"summer"))
//        list.add(PieEntry(20f,"Autumn"))
//        list.add(PieEntry(50f,"winter"))
//
//        //creat the pieDataSet
//        var pieDataset = PieDataSet(list,"")
//
//        //set colors to the pie chart  ==> two steps (2)
//        //1. create a list of colors
//        var colors = listOf(Color.parseColor("#0024FF"),
//            Color.parseColor("#81127F"),
//            Color.parseColor("#C1093F"),
//            Color.parseColor("#FD0004"),
//        )
//        //2.set it to pieDataSet
//        pieDataset.setColors(colors)
//        //hide the text properties and its values
//        pieDataset.valueTextSize = 10f
//        pieDataset.valueTextColor = Color.BLACK
//
//        // to make the label of each slice hidden you can set the formatter to null  noti
//        pieDataset.valueFormatter = null
//
//        //create pie Data and past the pieDataSet to its constructor
//        var pieData = PieData(pieDataset)
//        //to hide the value
//        pieData.setDrawValues(false)
//
////         to ajust the label you can edit the Foramatter
////        pieData.setValueFormatter(object : ValueFormatter() {
////            override fun getFormattedValue(value: Float): String {
////                return if (value == 100f) "my Goal"
////                else if (value == 20f) "my To Do"
////                else "my To Learn"
////            }
////        })
//
//
//
////        //set the data of the piechart  using pieData we just create
////        binding.mainPieChart.data = pieData
////        //set description of the graph
////        binding.mainPieChart.description.text = " "
////        //set center title
////        binding.mainPieChart.centerText = "Notes"
////        //hide the lable
////        binding.mainPieChart.setDrawEntryLabels(false)
////        //set the color of hole to transparent #xxxxxx00
////        binding.mainPieChart.setHoleColor(Color.parseColor("#00ffffff"))
////        //set the hole radius to x% of chart radius
////        binding.mainPieChart.holeRadius = 70f
////        //set the transparent circle radius to x% of chart radius
////        binding.mainPieChart.transparentCircleRadius = 80f
////        //set the animation to 2 second
////        binding.mainPieChart.animateY(2000)
////
////
////        //customize Legend using the legend (وسيلة الايضاح)
////        binding.mainPieChart.legend.apply {
////            setDrawInside(false)
////            verticalAlignment = Legend.LegendVerticalAlignment.CENTER
////            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
////            orientation = Legend.LegendOrientation.VERTICAL
////        }
//    }


    fun setOnClicks(){

        binding.MainFragmentRateDayMrb.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action === MotionEvent.ACTION_UP) {
                    // User released the rating bar, handle the action here
                    val rating: Float = binding.MainFragmentRateDayMrb.getRating()
                    // Perform your desired action with the rating value

                    m_onClickNavigator.onClick_to_addNoteFragment(rating)

                }
                return false
            }

        })

        binding.MainFragmentRateDayMrb.setOnRatingChangeListener { ratingBar, rating ->
            if (rating>7){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji7)
            }
            else if(rating>6){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji7)
            }
            else if(rating>5){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji6)
            }
            else if(rating>4){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji5)
            }
            else if(rating>3){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji4)
            }
            else if(rating>2){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji3)
            }
            else if(rating>1){
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji2)
            }
            else {
                binding.MainFragmentEmojiSiv.setImageResource(R.drawable.emoji1)
            }
        }
    }

    fun setViewIn(){

        /***********     set the main category adapter   *****************/
        var main_category_adapter = MainCategoriesAdapter(this , m_viewModel , object : MainCategoriesAdapter.OnClickListener{
            override fun onClick_mainCategory(cat_id : Int) {
                m_onClickNavigator.onClick_to_notesFragment(cat_id)
            }
        })
        binding.mainCategoryRv.adapter = main_category_adapter
        binding.mainCategoryRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.mainCategoryRv.addItemDecoration(MainCategoryDecoration(requireContext() , 15f,5f))

        /************     daily adapter **************/
        var listener = object:CheckPasswordDialogListener{
            override fun onChecked( lock: Boolean ) {
                if (lock){
                    Toast.makeText(requireContext() , "the password is incorrect " , Toast.LENGTH_LONG).show()

                }
                else{
                    Toast.makeText(requireContext() , "opening note " , Toast.LENGTH_LONG).show()
                    m_dialog.dismiss()
                    m_onClickNavigator.onClick_to_diaryFragment(m_selected_dairy , m_rate)
                }
            }
        }

        var adapter = DailyAdapter(
            object : DailyAdapterListener{

                override fun onClickLocked(id : Int , rate : Float) {
                    m_rate = rate
                    m_dialog = CheckPasswordDialog.newInstance(m_email , listener)
                    m_dialog.show(childFragmentManager , "")
                    m_selected_dairy = id
                }

                override fun onClickOpened(id: Int , rate : Float) {
                    m_selected_dairy = id
                    m_onClickNavigator.onClick_to_diaryFragment(m_selected_dairy , rate )
                }

            }
            , requireContext(), m_viewModel,this)

        binding.categoryRecycler.adapter = adapter
        binding.categoryRecycler.layoutManager= LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)


        var calendar = Calendar.getInstance()
        var date = String.format("%02d/%02d/%02d",calendar[Calendar.MONTH]+1 , calendar[Calendar.DAY_OF_MONTH] , calendar[Calendar.YEAR])

        binding.MainFragmentDateTv.setText(date)

        /************** user photo  ****************/

        var byteArray = Base64.decode(m_user.photo , Base64.DEFAULT)
        binding.MainFragmentPhotoSiv.setImageBitmap(BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size))

        /************  active daily rate or not    ************************/

        if (is_active){
            binding.MainFragmentRateDateContainerCsl.visibility = View.VISIBLE
            binding.MainFragmentContainerCsl.visibility = View.INVISIBLE
         }
        else{
            binding.MainFragmentRateDateContainerCsl.visibility = View.INVISIBLE
            binding.MainFragmentContainerCsl.visibility = View.VISIBLE

            GlobalScope.launch {

                var calendar = Calendar.getInstance()
                var date = "${calendar[Calendar.MONTH]+1}/${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.YEAR]}"

                var note = m_viewModel.getNoteByDate(date)



                withContext(Dispatchers.Main){
              Toast.makeText(requireContext() , "${note}" , Toast.LENGTH_LONG).show()
                    binding.MainFragmentActiveEmojiIv.setImageResource(note.icon)
                    binding.MainFragmentActiveTitleTv.setText(note.title)
                    binding.MainActivityActiveDescriptionTv.setText(note.description)
                    binding.MainFragmentActiveDateTv.setText(note.date)
                    binding.MainFragmentActiveRateDayMrb.rating=note.rate
                }
            }

        }


    }

    fun set_animation(){

        if (is_active){
            // To make the view appear (fade-in)
            val fadeInAnimator = ObjectAnimator.ofFloat(binding.MainFragmentActiveSiv, "alpha", 0f, 1f)
            fadeInAnimator.duration = 1800 // Adjust the duration as desired

            // To make the view disappear (fade-out)
            val fadeOutAnimator = ObjectAnimator.ofFloat(binding.MainFragmentActiveSiv, "alpha", 1f, 0f)
            fadeOutAnimator.duration = 1800 // Adjust the duration as desired

            // Set up the infinite loop
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(fadeInAnimator, fadeOutAnimator)
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    animatorSet.start() // Restart the animation when it ends
                }
            })

            // Start the infinite animation
            animatorSet.start()
        }

        else{
            // To make the view appear (fade-in)
            val fadeInAnimator2 = ObjectAnimator.ofFloat(binding.MainFragmentActiveDiarySiv, "alpha", 0f, 1f)
            fadeInAnimator2.duration = 1800 // Adjust the duration as desired

            // To make the view disappear (fade-out)
            val fadeOutAnimator2 = ObjectAnimator.ofFloat(binding.MainFragmentActiveDiarySiv, "alpha", 1f, 0f)
            fadeOutAnimator2.duration = 1800 // Adjust the duration as desired

            // Set up the infinite loop
            val animatorSet2 = AnimatorSet()
            animatorSet2.playSequentially(fadeInAnimator2, fadeOutAnimator2)
            animatorSet2.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    animatorSet2.start() // Restart the animation when it ends
                }
            })

            // Start the infinite animation
            animatorSet2.start()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(email : String ) : MainFragment{
            var fragment = MainFragment()
            fragment.arguments = Bundle().apply {
                putString("email", email)
            }
            return fragment
        }
    }

    interface DailyAdapterListener{
        fun onClickLocked(id : Int , rate : Float)
        fun onClickOpened(id : Int , rate : Float)
    }

    interface CheckPasswordDialogListener{
        fun onChecked( lock : Boolean )
    }


}