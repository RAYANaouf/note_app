package com.example.notes_app.ui.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.notes_app.R
import com.example.notes_app.databinding.DialogAddCategoryBinding


class AddCategoryDialogFragment : DialogFragment() {

    //view binding
    private lateinit var binding : DialogAddCategoryBinding

    private var state = 1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
                    binding.addCategoryDialogAddImg.background=resources.getDrawable(R.drawable.white_backgound_for_circle)
                    //change the button text and set the error text
                    binding.addCategoryDialogAddBtn.setText("retry")
                    binding.addCategoryDialogAddBtn.setBackgroundColor(Color.RED)

                    binding.addCategoryDialogHintTv.setText("set the title / description ")
                    state = 0
                }
                else{
                    //do what you need with add button
                }
            }
            else{
                //reset add icon change : icon + background
                binding.addCategoryDialogAddImg.setImageResource(R.drawable.add_icon)
                binding.addCategoryDialogAddImg.background=resources.getDrawable(R.drawable.white_border_gray_background_for_circle)
                //change the button text and set the error text
                binding.addCategoryDialogAddBtn.setText("add")
                binding.addCategoryDialogAddBtn.setBackgroundColor(Color("#0024FF"))

            }
        }
    }
}