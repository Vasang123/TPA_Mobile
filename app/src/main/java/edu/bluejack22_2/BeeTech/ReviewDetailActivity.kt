package edu.bluejack22_2.BeeTech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityReviewDetailBinding
import util.ActivityHelper
import util.ActivityTemplate
import view_model.ReviewDetailViewModel
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
    lateinit var reviewDetailViewModel: ReviewDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
    }

    override fun init() {
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        imageView = binding.imageView
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
        val reviewId = intent.getSerializableExtra("review")
        reviewDetailViewModel.viewDetail(this,reviewId.toString())
    }

    override fun onAction() {
        reviewDetailViewModel.review.observe(this, Observer { review->
            if(review != null){
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}