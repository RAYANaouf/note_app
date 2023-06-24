package com.example.notes_app.ui.dialog


import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.DialogAddCategoryBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.CategoryHashtagJoin
import com.example.notes_app.modul.room_database.data_classes.Hashtag
import com.example.notes_app.recyclers.adapter.HashtagAdapter
import com.example.notes_app.recyclers.item_decoration.HashtagDicoration
import com.example.notes_app.ui.interfaces.OnClickNavigator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream


class AddCategoryDialogFragment : DialogFragment() {

    //view binding
    private lateinit var binding : DialogAddCategoryBinding
    //view model
    private lateinit var m_viewModel : MyViewModel
    //set onClickListener
    private lateinit var m_onClickNavigator : OnClickNavigator
    //set initial state for the dialog (normal =1 / error = 0)
    private var state = 1

    private lateinit var m_adapter  : HashtagAdapter
    private  var m_hashtags = ArrayList<Hashtag>()

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
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            binding.addCategoryDialogAddImg.setImageURI(uri)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickNavigator){
            m_onClickNavigator = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //set up view model
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //set up the view binding
        binding = DialogAddCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClicks()

    }

    fun setView(){
        m_adapter = HashtagAdapter()
        binding.recyclerView.adapter = m_adapter

        var flexBoxLayoutManager           = FlexboxLayoutManager(requireContext())
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW
        flexBoxLayoutManager.flexWrap      = FlexWrap.WRAP
        binding.recyclerView.layoutManager      = flexBoxLayoutManager

        //item decoration
        binding.recyclerView.addItemDecoration(HashtagDicoration(requireContext() , 4f,4f,3f,3f))

        m_adapter.set_contents(m_hashtags)


    }


    companion object {
        @JvmStatic
        fun newInstance() = AddCategoryDialogFragment()
    }


    fun setOnClicks(){
        binding.addCategoryDialogCancelTv.setOnClickListener {
            this.dialog?.dismiss()
        }

        binding.addCategoryDialogAddTv.setOnClickListener {
            binding.progress.visibility=View.VISIBLE
            if (state == 1){
                if (binding.addCategoryDialogCategoryTitleTv.text.toString().equals("") || binding.addCategoryDialogCategoryDescriptionTv.text.toString().equals("")){
                    //set the error icon change : icon + background
                    binding.addCategoryDialogAddImg.setImageResource(R.drawable.error_icon)
                    binding.addCategoryDialogAddImg.background=ColorDrawable(ContextCompat.getColor(requireContext() , R.color.white))
                    //change the button text and set the error text
                    binding.addCategoryDialogAddTv.setText("retry")

                    binding.addCategoryDialogHintTv.setText("set the title / description ")
                    state = 0
                }
                else{
                    //do what you need with add button
                    var bitmap_img = binding.addCategoryDialogAddImg.drawable.toBitmap()

                    //        var bitmap_drawable =  ContextCompat.getDrawable(baseContext , R.drawable.c0) as (BitmapDrawable)
                    var byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap_img.compress(Bitmap.CompressFormat.JPEG , 25 , byteArrayOutputStream)
                    var byteArray = byteArrayOutputStream.toByteArray()
                    var img_string = android.util.Base64.encodeToString(byteArray , android.util.Base64.DEFAULT)

                    var i =0
                    var j =0
                    GlobalScope.launch {

                    var cat_id = m_viewModel.addCategory(Category(name=binding.addCategoryDialogCategoryTitleTv.text.toString() , description = binding.addCategoryDialogCategoryDescriptionTv.text.toString() , image = img_string ))

                    m_hashtags.forEach {
                        j++
                        var is_exist = m_viewModel.isHashtagExist(it.hashtag)

                        var job1 : Job? = null
                        if (is_exist==0){
                            job1 = m_viewModel.addHashtag(it)
                            i++
                        }

                        job1?.join()

                        var relation = CategoryHashtagJoin(cat_id , it.hashtag)
                        m_viewModel.addCategoryHashtagJoin(relation)

                        }

                     withContext(Dispatchers.Main){
                         Toast.makeText(requireContext() , "i=$i \nj=$j" , Toast.LENGTH_LONG).show()
                         this@AddCategoryDialogFragment.dialog?.dismiss()
                     }

                    }
                }
            }
            else{
                if ( !binding.addCategoryDialogCategoryTitleTv.text.toString().equals("") && !binding.addCategoryDialogCategoryDescriptionTv.text.toString().equals("")){
                    //reset add icon change : icon + background
                    binding.addCategoryDialogAddImg.setImageResource(R.drawable.add_icon)
                    binding.addCategoryDialogAddImg.background=ColorDrawable(ContextCompat.getColor(requireContext() , R.color.gray6))
                    //change the button text and set the error text
                    binding.addCategoryDialogAddTv.setText("add")

                    binding.addCategoryDialogHintTv.setText("Add Category")
                    binding.addCategoryDialogHintTv.setTextColor(ContextCompat.getColor(requireContext() , R.color.gray8))
                    state = 1
                }
            }
        }

        binding.addCategoryDialogAddImg.setOnClickListener {

            state = 1

            when{
                ContextCompat.checkSelfPermission(requireActivity().baseContext , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                    getContent.launch("image/*")
                }
                else->{
                    external_storage_permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }


        binding.addCategoryDialogHashtagFieldTiet.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var origin = s.toString()
                var updated = origin.replace(" ","_")
                if(origin != updated){
                    binding.addCategoryDialogHashtagFieldTiet.setText("$updated")
                    binding.addCategoryDialogHashtagFieldTiet.setSelection(updated.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.addCategoryDialogSendIv.setOnClickListener {


            if (binding.addCategoryDialogHashtagFieldTiet.text.toString() == "")
                return@setOnClickListener


            m_adapter.add_hashtag(Hashtag(" # ${binding.addCategoryDialogHashtagFieldTiet.text.toString()}"))
            binding.addCategoryDialogHashtagFieldTiet.setText("")

        }


    }

}