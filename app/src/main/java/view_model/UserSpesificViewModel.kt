package view_model

import android.content.Context
import android.widget.Toast
import repository.ReviewRepository

class UserSpesificViewModel : BaseUserSpesificViewModel() {

    override fun loadReviews(context: Context, userId: String) {
        ReviewRepository.getUserSpesficReviews({ reviewList, newLastDocumentSnapshot, isEndOfList ->
            _reviewList.value = reviewList
            lastDocumentSnapshot.value = newLastDocumentSnapshot
            isLoading.value = false
            isLastPage.value = isEndOfList
        }, { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            isLoading.value = false
        },
            userId = userId)
    }

    fun loadMoreUserSpesificReviews(context: Context, userId: String) {
        loadMoreReviews(context, userId) { context, lastDoc, reviewList, newLastDoc, loading, isEnd, userId ->
            ReviewRepository.getUserSpesficReviews({ moreReviewList, moreLastDoc, isEndOfList ->
                reviewList.value = reviewList.value?.plus(moreReviewList)
                newLastDoc.value = moreLastDoc
                loading.value = false
                isEnd.value = isEndOfList
            }, { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                loading.value = false
            }, lastDoc, userId)
        }
    }
}
