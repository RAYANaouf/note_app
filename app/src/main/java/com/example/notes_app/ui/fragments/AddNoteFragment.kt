package com.example.notes_app.ui.fragments

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.FragmentAddNoteBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.DiaryHashtagJoin
import com.example.notes_app.modul.room_database.data_classes.Hashtag
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.example.notes_app.receivers.ActivateReceiver
import com.example.notes_app.recyclers.adapter.NoteContentAdapter
import com.example.notes_app.recyclers.item_decoration.NoteContentDecoration
import com.example.notes_app.ui.bottomSheet.HashtagBottomSheet
import com.example.notes_app.ui.bottomSheet.ModalBottomSheet
import com.example.notes_app.ui.dialog.AddEmojiDialog
import com.example.notes_app.ui.dialog.AddTasksDialog
import com.example.notes_app.ui.dialog.ImageViewer
import com.example.notes_app.ui.dialog.ThemeDialog
import com.example.notes_app.ui.interfaces.AddTaskInterface
import com.example.notes_app.ui.interfaces.OnClickNavigator
import kotlinx.coroutines.*
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

public const val ARG_RATING="rating"

class AddNoteFragment : Fragment() , AddTaskInterface , OnColorPickedListener<ColorPickerDialog> , SetEmoji {

    //binding view-model navigator(to get back into notes-fragment)
    private lateinit var binding : FragmentAddNoteBinding
    private lateinit var m_viewModel : MyViewModel
    private lateinit var m_navigator : OnClickNavigator
    private lateinit var m_accountHandler : RegesterHandler


    //cat_id arrayOfContent  &&  color && icon && content  &&  lock
    private lateinit var m_contents : ArrayList<NoteContent>
    private var cat_id: Int = 0
    private var m_rating : Float = 0F
    private var m_color = -9408400
    private var m_icon = R.drawable.emoji4
    private var m_cont = ""
    private var m_lock = false
    private var m_hashtags = ArrayList<Hashtag>()

    //calendar & date
    private val m_calendar = Calendar.getInstance()
    private val m_date     = "${m_calendar[Calendar.MONTH]+1}/${m_calendar[Calendar.DAY_OF_MONTH]}/${m_calendar[Calendar.YEAR]}"

    //the adapter
    private lateinit var m_adapter : NoteContentAdapter

    /******************  activity  lanchers   *************************/
    private var post_notification_permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(requireContext() , "post notification is allowed " , Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(requireContext() , "the notification would appear " , Toast.LENGTH_LONG).show()
        }
    }

    //permission
    private var external_storage_permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(requireContext() , "read external storage is allowed " , Toast.LENGTH_LONG).show()
            getContent.launch("image/*")
        }else{
            Toast.makeText(requireContext() , "cant access to your gallery (to get a picture)" , Toast.LENGTH_LONG).show()
        }
    }

    //get picture
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            //get  image stream  then the byteArray
            val imageStream = requireActivity().contentResolver.openInputStream(uri)
            var imageData = imageStream?.readBytes()

            if (imageData!=null){
                //convert the byteArray to bitmap
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                //compress bitmap and convert it to bytearrayOutputstream
                var image_byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG , 80 , image_byteArrayOutputStream)

                //convert the bytearrayOutputstream to bytearray then string
                imageData = image_byteArrayOutputStream.toByteArray()
                val base64Image = android.util.Base64.encodeToString(imageData,android.util.Base64.DEFAULT)

                m_adapter.addItem(NoteContent( type = 2 , cont = base64Image))
            }

        }
    }

    /********************************************************************************/


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickNavigator){
            m_navigator = context
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            m_rating = it.getFloat(ARG_RATING)
        }

        //create handler
        m_accountHandler = RegesterHandler(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set up the view model
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        // set the binding
        binding = FragmentAddNoteBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        m_contents = ArrayList()
        m_contents.add(NoteContent(type = 0 , cont = ""))
        //set adapters
        var onClickListener = object : OnClickListener{
            override fun OnCickImage(img : String) {
                ImageViewer.newInstance(img).show(childFragmentManager,"")
            }
        }
        m_adapter = NoteContentAdapter(m_contents , requireContext(),onClickListener)
        binding.fragmentAddNoteNoteContentsRv.adapter = m_adapter
        binding.fragmentAddNoteNoteContentsRv.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
        binding.fragmentAddNoteNoteContentsRv.addItemDecoration(NoteContentDecoration(requireContext()))



        //set all onclicks
        setAllOnclicks()
        //set view
        setView()

    }

    override fun onStart() {
        super.onStart()
        requireActivity().invalidateOptionsMenu()
    }


    fun setRate(rating : Float){
        m_rating = rating
        if(rating>6){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji7)
            m_icon =  R.drawable.emoji6
        }
        else if(rating>5){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji6)
            m_icon =  R.drawable.emoji7
        }
        else if(rating>4){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji5)
            m_icon =  R.drawable.emoji5
        }
        else if(rating>3){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji4)
            m_icon =  R.drawable.emoji4
        }
        else if(rating>2){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji3)
            m_icon =  R.drawable.emoji3
        }
        else if(rating>1){
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji2)
            m_icon =  R.drawable.emoji2
        }
        else {
            binding.addNoteFragmentEmojiIv.setImageResource(R.drawable.emoji1)
            m_icon =  R.drawable.emoji1
        }
    }
    private fun setView(){
        //set time
        binding.addNoteFragmentTimeTv.setText(m_date)
        //set imoji
        this.setRate(m_rating)


    }

    private fun setAllOnclicks(){

        /*********************************************  done btn  *********************************************/
        binding.addNoteFragmentDoneIv.setOnClickListener {

            if (!check()){
                Toast.makeText(requireContext() , "you forgot the title " , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            CoroutineScope(Dispatchers.Default).launch {

                coroutineScope {

                    var contents = m_adapter.get_all_item()
                    m_cont = contents[0].cont

                    val noteId = m_viewModel.addNote(Note(cat_id = cat_id , lock = m_lock ,  date = m_date, rate = m_rating , color = m_color, icon = m_icon, title = binding.addNoteFragmentNoteTitleTiet.text.toString() , description = m_cont)).await()

                    for (i in 0 until m_adapter.getItemCount()){
                        contents[i].note_id = noteId
                        m_viewModel.addNoteContent( contents[i] )
                    }


                    var job0 = launch {
                        m_hashtags.forEach {hashtag ->

                            val job = if (m_viewModel.isHashtagExist(hashtag.hashtag) == 0) {
                                    m_viewModel.addHashtag(hashtag)
                                }
                            else{
                                null
                            }

                            val job1 = launch {
                                job?.join() // Wait for job to complete before proceeding
                                val relationTable = DiaryHashtagJoin(noteId, hashtag.hashtag)
                                m_viewModel.addDiaryHashtagJoin(relationTable)
                            }

                            job1.join()

                        }
                    }

                    job0.join()
                    withContext(Dispatchers.Main){
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }

                m_accountHandler.deactivate()

                var alarmManager = requireContext().getSystemService(AlarmManager::class.java)
                var intent = Intent(requireContext(),ActivateReceiver::class.java)
                var pendingIntent = PendingIntent.getBroadcast(requireContext() , 0 , intent , 0)

                var calendar = Calendar.getInstance()
//                calendar.set(Calendar.HOUR , 0)
                calendar.add(Calendar.SECOND , 10)
//                calendar.set(Calendar.SECOND , 0)
//                calendar.add(Calendar.DAY_OF_MONTH,1)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP , calendar.timeInMillis , pendingIntent)

            }
        }

        /*********************************************  content btn  *********************************************/

        binding.addNoteFragmentAddNoteContentIv.setOnClickListener {
            m_adapter.addItem(NoteContent( type = 0  , cont = ""))
        }


        /*********************************************  checkbox btn  *********************************************/

        binding.addNoteFragmentAddCheckBoxIv.setOnClickListener {
            var addtasksDialog = AddTasksDialog.newInstance()
            addtasksDialog.show(childFragmentManager , "")
        }

        /*********************************************  attach file  btn  *********************************************/

        binding.addNoteFragmentAttachFileIv.setOnClickListener {

            when{
                ContextCompat.checkSelfPermission(requireActivity().baseContext , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                    getContent.launch("image/*")
                }
                else->{
                    external_storage_permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        /*********************************************  alarm   btn  *********************************************/

        binding.addNoteFragmentAddAlarmIv.setOnClickListener {


            when{
                ContextCompat.checkSelfPermission(requireActivity().baseContext , Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED ->{
                }
                else->{
                    Toast.makeText(requireContext() , "the app need notification permission to post notification at the time " , Toast.LENGTH_LONG).show()
                    post_notification_permission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            var bottomSheet = ModalBottomSheet()
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL , R.style.ModalBottomSheetDialog)
            bottomSheet.show(childFragmentManager , "")


        }

        /*********************************************  color   btn  *********************************************/

        binding.addNoteFragmentSetColorIv.setOnClickListener {
            var colorPickerDialog = ColorPickerDialog()
                .withColor(Color.BLUE)
                .withListener(this)
                .withPresets(Color.RED, Color.GREEN, Color.BLUE)
                .withPicker(ImagePickerView::class.java)
                .show(childFragmentManager , "color picker")



        }

        /*********************************************  emoji   btn  *********************************************/


        binding.addNoteFragmentEmojiIv.setOnClickListener {
            AddEmojiDialog.nesInstance(this).show(childFragmentManager , "emoji")
        }

        /*********************************************  theme   btn  *********************************************/

        binding.addNoteFragmentSetStyleIv.setOnClickListener {
            ThemeDialog.newInstance().show(childFragmentManager,"themeDialog")
        }

        /*********************************************   lock  icon   *********************************************/

        binding.addNoteFragmentLockSiv.setOnClickListener {
            m_lock = !m_lock
            if (m_lock){
                binding.addNoteFragmentLockSiv.setImageResource(R.drawable.lock_icon)
            }
            else{
                binding.addNoteFragmentLockSiv.setImageResource(R.drawable.lock_open_icon)
            }
        }

        /*********************************************   hashtag  icon   *********************************************/

        binding.addNoteFragmentHashtagSiv.setOnClickListener {
           var bottomSheet = HashtagBottomSheet.newInstantce(m_hashtags)
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL , R.style.ModalBottomSheetDialog)
            bottomSheet.show(childFragmentManager , "")
        }

    }



    fun check():Boolean{

        if (binding.addNoteFragmentNoteTitleTiet.text.toString()=="" ){
            return false
        }
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance(rating: Float) =
            AddNoteFragment().apply {
                arguments = Bundle().apply {
                    putFloat(ARG_RATING, rating)
                }
            }
    }

    override fun addTask(text : String ) {
        m_adapter.addItem(NoteContent(type = 1 , cont = "$text"))
    }


    override fun onColorPicked(pickerView: ColorPickerDialog?, color: Int) {
     m_color = color

        binding.addNoteFragmentTitlebarV.setBackgroundColor(m_color)
        binding.addNoteFragmentTimeTv.setTextColor(m_color)
        binding.addNoteFragmentColorBadgeSiv.setBackgroundColor(m_color)
        var lighter_version = ColorUtils.blendARGB(color, Color.WHITE, 0.7f)
        binding.addNoteFragmentTitleContentSeparetorV.setBackgroundColor(lighter_version)

    }

    interface OnClickListener{
        fun OnCickImage(img : String)
    }

    override fun onSet(res: Int) {
        binding.addNoteFragmentEmojiIv.setImageResource(res)
        m_icon = res
    }


}


interface SetEmoji{
    fun onSet(res:Int)
}