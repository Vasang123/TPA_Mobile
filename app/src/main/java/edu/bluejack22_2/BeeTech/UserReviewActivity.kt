package edu.bluejack22_2.BeeTech

import adapter.AdminReviewAdapter
import adapter.BaseReviewAdapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.databinding.ActivityUserReviewBinding
import view_model.DeleteReviewViewModel

import view_model.UserReviewViewModel

class UserReviewActivity : AppCompatActivity(), BaseReviewAdapter.OnFavoriteClickListener  {

    private lateinit var userID : String

    private lateinit var rv : RecyclerView
    private lateinit var adminReviewAdapter : AdminReviewAdapter

    private lateinit var binding : ActivityUserReviewBinding
    private lateinit var userReviewViewModel: UserReviewViewModel
    private lateinit var deleteReviewViewModel: DeleteReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userID = intent.getSerializableExtra("userid")!!.toString()
        init()

        Log.e("userid", userID)
    }

    fun init(){
        rv = binding.userReviewAdminRecycleView
        userReviewViewModel = ViewModelProvider(this)[UserReviewViewModel::class.java]
        deleteReviewViewModel = ViewModelProvider(this)[DeleteReviewViewModel::class.java]
        adminReviewAdapter = AdminReviewAdapter(deleteReviewViewModel,this, this)
    }

     override fun onFavoriteClick() {

    }


}