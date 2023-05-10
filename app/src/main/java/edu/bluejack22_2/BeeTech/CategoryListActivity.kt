package edu.bluejack22_2.BeeTech

import adapter.CategoryAdapter
import adapter.UserAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dialog_fragment.*
import edu.bluejack22_2.BeeTech.databinding.ActivityCategoryListBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityUserListBinding
import edu.bluejack22_2.BeeTech.databinding.CategoryListBinding
import edu.bluejack22_2.BeeTech.databinding.UserListBinding
import model.Category
import util.ActivityHelper
import util.ActivityTemplate
import view_model.CategoryViewModel
import view_model.CreateCategoryViewModel
import view_model.DeleteCategoryViewModel
import view_model.UpdateCategoryViewModel

class CategoryListActivity :
    AppCompatActivity(),
    DeleteCategoryDialog.DeleteCategoryDialogListener,
    UpdateCategoryDialog.UpdateCategoryListener,
    CreateCategoryDialog.CreateCategoryListener,
    ActivityTemplate {

    lateinit var binding : ActivityCategoryListBinding
    lateinit var recyclerView: RecyclerView
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var deleteCategoryViewModel: DeleteCategoryViewModel
    lateinit var updateCategoryViewModel: UpdateCategoryViewModel
    lateinit var createCategoryViewModel: CreateCategoryViewModel
    lateinit var createButton : FloatingActionButton
    lateinit var categoryId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onAction()
    }

    override fun init() {
        recyclerView = binding.categoryRecycleView
        createButton = binding.floatingActionButton
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        deleteCategoryViewModel = ViewModelProvider(this)[DeleteCategoryViewModel::class.java]
        updateCategoryViewModel = ViewModelProvider(this)[UpdateCategoryViewModel::class.java]
        createCategoryViewModel = ViewModelProvider(this)[CreateCategoryViewModel::class.java]
        categoryAdapter = CategoryAdapter(this)
        setupRecyclerView()
        categoryViewModel.loadCategory(this)

    }
    fun showDeleteCategoryConfirmation(categoryId : String){
        val deleteCategoryDialog = DeleteCategoryDialog()
        deleteCategoryDialog.show(supportFragmentManager, "DeleteCategoryFragment")
        this.categoryId = categoryId
    }
    fun showUpdateCategory(categoryId: String){
        val updateCategory = UpdateCategoryDialog.newInstance(categoryId)
        updateCategory.show(supportFragmentManager, "UpdateCategoryFragment")
        this.categoryId = categoryId
    }
    fun showCreateCategory(){
        val createCateory = CreateCategoryDialog()
        createCateory.show(supportFragmentManager, "CreateCategoryFragment")
    }
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = categoryAdapter
    }
    override fun onAction() {
        createButton.setOnClickListener{
            showCreateCategory()
        }
        categoryViewModel.categoryList.observe(this, Observer { category ->
            categoryAdapter.submitList(category)
        })
        deleteCategoryViewModel.success.observe(this, Observer {res ->
            if(res == "Success"){
                finish()
                ActivityHelper.changePage(this,CategoryListActivity::class.java)
            }
        })
        createCategoryViewModel.createSuccess.observe(this, Observer { res->
            if(res){
                finish()
                ActivityHelper.changePage(this,CategoryListActivity::class.java)
            }
        })
        updateCategoryViewModel.updateSuccess.observe(this, Observer { res ->
            if(res){
                finish()
                ActivityHelper.changePage(this,CategoryListActivity::class.java)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(
            object : UserReviewsFragment.InfiniteScrollListener(
                layoutManager,
                { categoryViewModel.loadMoreCategory() },
                { categoryViewModel.isLoading.value == true }
            ) {}
        )
    }
    override fun onCategoryDelete() {
        deleteCategoryViewModel.deleteCategory(this,categoryId)
    }

    override fun onCategoryUpdate(content: String) {
        updateCategoryViewModel.validateUpdate(content,categoryId,this)
    }

    override fun onCategoryInsert(content: String) {
        createCategoryViewModel.validateCreate(content,this)
    }
}