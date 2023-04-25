package dialog_fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack22_2.BeeTech.R
import edu.bluejack22_2.BeeTech.databinding.FragmentHomeBinding
import edu.bluejack22_2.BeeTech.databinding.PopupUpdateReviewBinding
import model.Category
import repository.ReviewRepository
import util.ActivityTemplate
import view_model.*

class UpdateReviewDialog : BaseDialogFragment(), ActivityTemplate {
    interface UpdateReviewDialogListener {
        fun onReviewUpdate(file: Uri?,
                           title: String,
                           description: String,
                           context: Context,
                           category : Category,
                           currId : String,
                            dialog: Dialog,
                            selectedImage : ImageView,
                            imageUrl : String)
    }
    lateinit var updateReviewDialogListener: UpdateReviewDialogListener
    lateinit var reviewDetailViewModel: ReviewDetailViewModel
    lateinit var categoryList: List<Category>
    lateinit var selectedCategory: Category
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var itemId : String
    lateinit var cancelButton : ImageView
    lateinit var imageUrl : String
    lateinit var updateButton : Button
    lateinit var selectedImage : ImageView
    lateinit var chooseImage : Button
    lateinit var descField : EditText
    lateinit var titleField : EditText
    lateinit var spinner : Spinner
    lateinit var binding: PopupUpdateReviewBinding
    var selectedImageUri: Uri? = null
    var SELECT_IMAGE_CODE = 1
    fun observer(){
        reviewDetailViewModel.review.observe(this, Observer { review->
            val title = review.title.toString()
            val desc = review.description.toString()
            val editableTitle = Editable.Factory.getInstance().newEditable(title)
            val editableDesc = Editable.Factory.getInstance().newEditable(desc)
            titleField.text = editableTitle
            descField.text = editableDesc
            imageUrl = review.imageURL
            Glide.with(requireContext())
                .load(review.imageURL)
                .into(selectedImage)
            val index = categoryList.indexOfFirst { it.id == review.categoryId }
            if (index >= 0) {
                spinner.setSelection(index)
            }

        })
        categoryViewModel.categoryList.observe(viewLifecycleOwner, Observer { list ->
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, list.map { it.name })
            spinner.adapter = adapter
            categoryList = list
        })
    }

    override fun init(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryViewModel.loadCategory(requireContext())
        itemId = arguments?.getString("itemId").toString()
        cancelButton = binding.closeUpdateReview
        updateButton = binding.updateReviewButton
        selectedImage = binding.updateSelectedImage
        chooseImage = binding.updateChooseImage
        descField = binding.updateDescField
        titleField = binding.updateTitleField
        spinner = binding.updateSpinner
        reviewDetailViewModel = ViewModelProvider(this)[ReviewDetailViewModel::class.java]
    }

    override fun onAction() {
        updateButton.setOnClickListener {
            dialog?.let { it1 ->
                updateReviewDialogListener.onReviewUpdate(selectedImageUri,
                    titleField.text.toString(),
                    descField.text.toString(),
                    requireContext(),
                    selectedCategory,
                    itemId,
                    it1,
                    selectedImage,
                    imageUrl
                )
            }
        }
        chooseImage.setOnClickListener {
            var intent: Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Title"), SELECT_IMAGE_CODE)
        }
        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = PopupUpdateReviewBinding.inflate(layoutInflater, container, false)
        init()
        onAction()
        observer()
        if (itemId != null) {
            reviewDetailViewModel.viewDetail(requireContext(),itemId)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categoryList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateReviewDialogListener = context as UpdateReviewDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UpdateReviewDialog")
        }
    }
    companion object {
        fun newInstance(itemId: String): UpdateReviewDialog {
            val args = Bundle().apply {
                putString("itemId", itemId)
            }
            return UpdateReviewDialog().apply {
                arguments = args
            }
        }
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