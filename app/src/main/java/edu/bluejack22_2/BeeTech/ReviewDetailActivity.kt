package edu.bluejack22_2.BeeTech

import adapter.CommentAdapter
import adapter.ReviewAdapter
import android.app.Dialog
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.auth.User
import dialog_fragment.DeleteConfirmationDialog
import dialog_fragment.UpdateReviewDialog
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityReviewDetailBinding
import model.Category
import model.Review
import util.ActivityHelper
import util.ActivityTemplate
import util.FragmentHelper
import view_model.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewDetailActivity :
    AppCompatActivity(),
    ActivityTemplate,
    UpdateReviewDialog.UpdateReviewDialogListener,
    DeleteConfirmationDialog.DeleteDialogListener {
    lateinit var binding: ActivityReviewDetailBinding
    lateinit var imageView: ImageView
    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var createdAt: TextView
    lateinit var author: TextView
    lateinit var category: TextView
    lateinit var favCount: TextView
    lateinit var favorite: ImageView
    lateinit var updateReviewButton: ImageView
    lateinit var deleteReviewButton: ImageView
    lateinit var closeButton: ImageView
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var reviewDetailViewModel: ReviewDetailViewModel
    lateinit var reviewId : Serializable
    lateinit var reviewOwner : Serializable
    lateinit var userId :String
    lateinit var commentBox : EditText
    lateinit var sendComment : ImageView
    lateinit var currentItem :Review
    lateinit var createCommentViewModel: CreateCommentViewModel
    lateinit var commentViewModel: CommentViewModel
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var deleteReviewViewModel: DeleteReviewViewModel
    lateinit var updateReviewViewModel: UpdateReviewViewModel
    private val favoriteStatusMap = mutableMapOf<String, Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(this, Observer{user->
            userId = user.id
            init()
            onAction()
        })
        setContentView(binding.root)
    }

    override fun init() {
        imageView = binding.imageView
        commentBox = binding.commentTextField
        sendComment = binding.sendButton
        closeButton = binding.closeDetail
        title = binding.titleDetail
        category = binding.categoryDetail
        description = binding.reviewDetail
        createdAt = binding.reviewCreatedAt
        author = binding.authorDetail
        favCount = binding.favCountDetail
        favorite = binding.favoriteButtonDetail
        updateReviewButton = binding.updateDetail
        deleteReviewButton = binding.deleteDetail
        recyclerView = binding.commentRecycleView
        reviewDetailViewModel = ViewModelProvider(this)[ReviewDetailViewModel::class.java]
        favouriteViewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        createCommentViewModel = ViewModelProvider(this)[CreateCommentViewModel::class.java]
        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        deleteReviewViewModel = ViewModelProvider(this)[DeleteReviewViewModel::class.java]
        updateReviewViewModel = ViewModelProvider(this)[UpdateReviewViewModel::class.java]
        reviewId = intent.getSerializableExtra("review")!!
        reviewOwner = intent.getSerializableExtra("reviewOwner")!!
        reviewDetailViewModel.viewDetail(this,reviewId.toString())
        commentAdapter = CommentAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = commentAdapter
        if(reviewOwner != userId){
            deleteReviewButton.visibility = View.INVISIBLE
            updateReviewButton.visibility = View.INVISIBLE
        }
    }
    fun showUpdateReview(itemId:String){
        val updateReviewDialog = UpdateReviewDialog.newInstance(itemId)
        updateReviewDialog.show(supportFragmentManager, "UpdateReviewDialog")
    }
    fun showDeleteReviewConfirmation(reviewId: String){
        val deleteReviewDialog = DeleteConfirmationDialog()
        deleteReviewDialog.show(supportFragmentManager, "DeleteDialogFragment")
        this.reviewId = reviewId
    }
    override fun onAction() {
        reviewDetailViewModel.review.observe(this, Observer { review->
            if(review != null){
                currentItem = review
                Log.e("Review Data", review.title.toString())
                title.text = review.title
                description.text = review.description
                author.text = review.username
                category.text = "Category : " +  review.categoryName
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                createdAt.text = sdf.format(review.createdAt)
                favCount.text = review.totalFavorites.toString()
                Glide.with(this)
                    .load(review.imageURL)
                    .into(imageView)
            }
        })
        createCommentViewModel.createSuccess.observe(this, Observer { res ->
            if(res){
                ActivityHelper.changePage(this,ReviewDetailActivity::class.java,reviewId.toString(), reviewOwner.toString())
            }
        })
        updateReviewButton.setOnClickListener{
            showUpdateReview(reviewId.toString())
        }
        deleteReviewButton.setOnClickListener{
            showDeleteReviewConfirmation(reviewId.toString())
        }
        sendComment.setOnClickListener{
            createCommentViewModel.validateCreate(commentBox.text.toString(), reviewId.toString(),this)
            commentBox.setText("")
        }
        closeButton.setOnClickListener{
            finish()
            ActivityHelper.changePage(this,MainActivity::class.java)
        }

        userId?.let { userId ->
            favouriteViewModel.isReviewFavorited(userId, reviewId.toString()) { isFavorited ->
                favoriteStatusMap[userId] = isFavorited
                favouriteViewModel.updateFavoriteIndicator(favorite, isFavorited)
            }
        }
        favorite.setOnClickListener {
            favorite.isEnabled = false

            val currentStatus = favoriteStatusMap[reviewId] ?: false

            val newStatus = !currentStatus

            if (newStatus) {
                favouriteViewModel.addReviewToFavorites(userId, reviewId.toString())
            } else {
                favouriteViewModel.removeReviewFromFavorites(userId, reviewId.toString())
            }

            favouriteViewModel.updateFavoriteCount(currentItem, newStatus) { newCount ->
                currentItem.totalFavorites = newCount
                favCount.text = newCount.toString()
                favorite.isEnabled = true
            }

            favoriteStatusMap[reviewId.toString()] = newStatus
            favouriteViewModel.updateFavoriteIndicator(favorite, newStatus)
        }
        commentViewModel.loadComments(reviewId.toString(), this)
        commentViewModel.commentList.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                commentAdapter.submitList(list)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { commentViewModel.loadMoreReviewComments(reviewId.toString(),this) },
                { commentViewModel.isLoading.value == true }
            )
        )
        deleteReviewViewModel.success.observe(this, Observer { res->
            finish()
            ActivityHelper.changePage(this,MainActivity::class.java)
        })
        updateReviewViewModel.updateSuccess.observe(this, Observer { res->
            if(res){
                finish()
                ActivityHelper.changePage(this,ReviewDetailActivity::class.java,reviewId.toString(),reviewOwner.toString())
            }
        })
    }
    class InfiniteScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val loadMore: () -> Unit,
        private val isLoading: () -> Boolean
    ) : RecyclerView.OnScrollListener() {
        private val threshold = 5

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (isLoading()) return

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (firstVisibleItemPosition + visibleItemCount + threshold >= totalItemCount) {
                loadMore()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        ActivityHelper.changePage(this,MainActivity::class.java)
    }

    override fun onDelete() {
        deleteReviewViewModel.deleteReview(this, reviewId.toString())
    }

    override fun onReviewUpdate(
        file: Uri?,
        title: String,
        description: String,
        context: Context,
        category: Category,
        currId: String,
        dialog: Dialog,
        selectedImage: ImageView,
        imageUrl: String
    ) {
        updateReviewViewModel.validateUpdate(
            file,
            title,
            description,
            context,
            category,
            currId,
            dialog,
            selectedImage,
            imageUrl)
    }
}