package edu.bluejack22_2.BeeTech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.auth.User
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityReviewDetailBinding
import model.Review
import util.ActivityHelper
import util.ActivityTemplate
import view_model.FavouriteViewModel
import view_model.ReviewDetailViewModel
import view_model.UserViewModel
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewDetailActivity : AppCompatActivity(), ActivityTemplate {
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
    lateinit var userId :String
    lateinit var currentItem :Review
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
        reviewDetailViewModel = ViewModelProvider(this)[ReviewDetailViewModel::class.java]
        favouriteViewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        reviewId = intent.getSerializableExtra("review")!!
        reviewDetailViewModel.viewDetail(this,reviewId.toString())

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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        ActivityHelper.changePage(this,MainActivity::class.java)
    }
}