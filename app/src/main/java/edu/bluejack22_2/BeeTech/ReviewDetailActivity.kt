package edu.bluejack22_2.BeeTech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityReviewDetailBinding
import util.ActivityTemplate

class ReviewDetailActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var binding: ActivityReviewDetailBinding
    lateinit var imageView: ImageView
    lateinit var title: TextView
    lateinit var createdAt: TextView
    lateinit var author: TextView
    lateinit var favCount: TextView
    lateinit var favorite: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        init()
        onAction()
    }

    override fun init() {
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)

    }

    override fun onAction() {

    }
}