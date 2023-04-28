package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import repository.ReviewRepository
import repository.UserRepository

class UserReviewViewModel : BaseReviewViewModel() {
    val lastReviewTimestamp: MutableLiveData<Long?> = MutableLiveData()

    override fun loadReviews(context: Context) {
        UserRepository.getLoggedUser { user ->
            if (user != null) {
                ReviewRepository.getUserReviews(user.id, { reviewList, newLastDocumentSnapshot, isEndOfList ->
                    _reviewList.value = reviewList
                    lastDocumentSnapshot.value = newLastDocumentSnapshot
                    isLoading.value = false
                    isLastPage.value = isEndOfList
                }, { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    isLoading.value = false
                })
            }
        }
    }

    fun loadMoreUserReviews(context: Context) {
        if (isLoading.value == true || isLastPage.value == true) {
            return
        }

        isLoading.value = true


        loadMoreReviews(context) { context, lastDoc, reviewList, newLastDoc, loading, isEnd ->
            UserRepository.getLoggedUser { user ->
                if (user != null) {
                    ReviewRepository.getUserReviews(user.id, { moreReviewList, moreLastDoc, isEndOfList ->
                        reviewList.value = reviewList.value?.plus(moreReviewList)
                        newLastDoc.value = moreLastDoc
                        loading.value = false
                        isEnd.value = isEndOfList
                    }, { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        loading.value = false
                    }, lastDoc)
                } else {
                    loading.value = false
                }
            }
        }
    }

    fun loadFavoritedReviews(context: Context) {

        UserRepository.getLoggedUser { user ->
            if (user != null) {
                ReviewRepository.fetchFavoriteReviews(user.id, lastReviewTimestamp.value,
                    onSuccess = { reviewList, newLastTimestamp ->
                        _reviewList.value = reviewList
                        lastReviewTimestamp.value = newLastTimestamp
                        isLoading.value = false
                        isLastPage.value = newLastTimestamp == null
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        isLoading.value = false
                    })
            }
        }
    }

    fun loadMoreFavoritedReviews(context: Context) {
        if (isLoading.value == true || isLastPage.value == true) {
            return
        }

        isLoading.value = true

        UserRepository.getLoggedUser { user ->
            if (user != null) {
                ReviewRepository.fetchFavoriteReviews(user.id, lastReviewTimestamp.value,
                    onSuccess = { moreReviewList, moreLastTimestamp ->
                        _reviewList.value = _reviewList.value?.plus(moreReviewList)
                        lastReviewTimestamp.value = moreLastTimestamp
                        isLoading.value = false
                        isLastPage.value = moreLastTimestamp == null
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        isLoading.value = false
                    })
            } else {
                isLoading.value = false
            }
        }
    }



}

