package view_model

import android.content.Context
import android.widget.Toast
import repository.ReviewRepository
import repository.UserRepository

class UserReviewViewModel : BaseReviewViewModel() {

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
                }
            }
        }
    }
}

