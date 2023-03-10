package com.example.notes_app.ui.dialog


import android.content.Intent
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.DialogFragment
import com.example.notes_app.R
import com.example.notes_app.databinding.DialogAddCategoryBinding
import com.example.notes_app.ui.activities.MainActivity2


class AddCategoryDialogFragment : DialogFragment() {

    //view binding
    private lateinit var binding : DialogAddCategoryBinding
    //set onClickListener
    private lateinit var m_onClickNavigator : OnClickNavigator
    //set initial state for the dialog (normal =1 / error = 0)
    private var state = 1

    //permission
    private var external_storage_permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(requireContext() , "read external storage" , Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(requireContext() , "cant read " , Toast.LENGTH_LONG).show()
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

        //set up the view binding
        binding = DialogAddCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClicks()

    }


    companion object {
        @JvmStatic
        fun newInstance() = AddCategoryDialogFragment()
    }


    fun setOnClicks(){
        binding.addCategoryDialogCancelBtn.setOnClickListener {
            this.dialog?.dismiss()
        }

        binding.addCategoryDialogAddBtn.setOnClickListener {
            if (state == 1){
                if (binding.addCategoryDialogCategoryTitleTv.text.toString().equals("") || binding.addCategoryDialogCategoryDescriptionTv.text.toString().equals("")){
                    //set the error icon change : icon + background
                    binding.addCategoryDialogAddImg.setImageResource(R.drawable.error_icon)
                    binding.addCategoryDialogAddImg.background=ColorDrawable(ContextCompat.getColor(requireContext() , R.color.white))
                    //change the button text and set the error text
                    binding.addCategoryDialogAddBtn.setText("retry")
                    binding.addCategoryDialogAddBtn.setBackgroundColor(ContextCompat.getColor(requireContext() , R.color.color2))

                    binding.addCategoryDialogHintTv.setText("set the title / description ")
                    state = 0
                }
                else{
                    //do what you need with add button
                    var bitmap_img = binding.addCategoryDialogAddImg.drawable.toBitmap()
//                    Toast.makeText(context , "${bitmap_img.toString()}" , Toast.LENGTH_LONG).show()
                    m_onClickNavigator.onClick_addCategory(binding.addCategoryDialogCategoryTitleTv.text.toString() , binding.addCategoryDialogCategoryDescriptionTv.text.toString() , bitmap_img)
                    this.dialog?.dismiss()
                }
            }
            else{
                if ( !binding.addCategoryDialogCategoryTitleTv.text.toString().equals("") && !binding.addCategoryDialogCategoryDescriptionTv.text.toString().equals("")){
                    //reset add icon change : icon + background
                    binding.addCategoryDialogAddImg.setImageResource(R.drawable.add_icon)
                    binding.addCategoryDialogAddImg.background=ColorDrawable(ContextCompat.getColor(requireContext() , R.color.gray6))
                    //change the button text and set the error text
                    binding.addCategoryDialogAddBtn.setText("add")
                    binding.addCategoryDialogAddBtn.setBackgroundColor(ContextCompat.getColor(requireContext() , R.color.color1))

                    binding.addCategoryDialogHintTv.setText("Add Category")
                    binding.addCategoryDialogHintTv.setTextColor(ContextCompat.getColor(requireContext() , R.color.gray8))
                    state = 1
                }
            }
        }

        binding.addCategoryDialogAddImg.setOnClickListener {


            when{
                ContextCompat.checkSelfPermission(requireActivity().baseContext , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                    getContent.launch("image/*")
                }
                else->{
                    external_storage_permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

}