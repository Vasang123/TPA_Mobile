package edu.bluejack22_2.BeeTech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import util.ActivityTemplate
import util.ActivityHelper
import view_model.CreateReviewViewModel


class CreateFragment : Fragment(), ActivityTemplate {
    lateinit var selectedImage:ImageView
    lateinit var chooseImage:Button
    lateinit var createButton:Button
    lateinit var titleEditText:EditText
    lateinit var descriptionEditText:EditText
    var selectedImageUri: Uri? = null
    var SELECT_IMAGE_CODE = 1
    lateinit var createViewModel: CreateReviewViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onAction()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun init() {
        selectedImage = requireView().findViewById(R.id.selectedImage)
        chooseImage = requireView().findViewById(R.id.chooseImage)
        createButton = requireView().findViewById(R.id.addReview)
        titleEditText= requireView().findViewById(R.id.titleField)
        descriptionEditText= requireView().findViewById(R.id.descField)
        selectedImage.setImageResource(R.drawable.blank_image)
        createViewModel = ViewModelProvider(this)[CreateReviewViewModel::class.java]
    }

    override fun onAction() {
        chooseImage.setOnClickListener {
            var intent:Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Title"), SELECT_IMAGE_CODE)
        }

        createButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            createViewModel.validateCreate(selectedImageUri,title,description,requireContext())
        }

        createViewModel.createSuccess.observe(viewLifecycleOwner, Observer{
            if(it){
                requireActivity().finish()
                ActivityHelper.changePage(requireContext(),MainActivity::class.java)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if (data != null) {
                selectedImageUri = data.data!!
            }
            selectedImage.setImageURI(selectedImageUri)
        }
    }
}