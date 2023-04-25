package edu.bluejack22_2.BeeTech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.BeeTech.databinding.FragmentCreateBinding
import model.Category
import util.ActivityTemplate
import util.ActivityHelper
import view_model.CategoryViewModel
import view_model.CreateReviewViewModel


class CreateFragment : Fragment(), ActivityTemplate {
    lateinit var selectedImage:ImageView
    lateinit var chooseImage:Button
    lateinit var createButton:Button
    lateinit var titleEditText:EditText
    lateinit var descriptionEditText:EditText
    lateinit var binding:FragmentCreateBinding
    lateinit var spinner: Spinner
    lateinit var categoryViewModel: CategoryViewModel
    var selectedImageUri: Uri? = null
    var SELECT_IMAGE_CODE = 1
    lateinit var categoryList: List<Category>
    lateinit var selectedCategory: Category
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
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun init() {
        selectedImage = binding.selectedImage
        chooseImage = binding.chooseImage
        createButton = binding.addReview
        titleEditText= binding.titleField
        descriptionEditText= binding.descField
        spinner = binding.spinner
        selectedImage.setImageResource(R.drawable.blank_image)
        createViewModel = ViewModelProvider(this)[CreateReviewViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryViewModel.loadCategory(requireContext())
    }

    override fun onAction() {
        chooseImage.setOnClickListener {
            var intent:Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Title"), SELECT_IMAGE_CODE)
        }
        categoryViewModel.categoryList.observe(viewLifecycleOwner, Observer { list ->
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, list.map { it.name })
            spinner.adapter = adapter
            categoryList = list
        })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categoryList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        createButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            createViewModel.validateCreate(selectedImageUri,title,description,requireContext(),selectedCategory)
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