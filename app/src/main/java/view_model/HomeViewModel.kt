package view_model

import android.content.Context
import android.widget.Toast
import repository.ReviewRepository

class HomeViewModel : BaseReviewViewModel() {

    override fun loadReviews(context: Context) {
        ReviewRepository.getHomeReviews({ reviewList, newLastDocumentSnapshot, isEndOfList ->
            _reviewList.value = reviewList
            lastDocumentSnapshot.value = newLastDocumentSnapshot
            isLoading.value = false
            isLastPage.value = isEndOfList
        }, { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            isLoading.value = false
        })
    }

    fun loadMoreHomeReviews(context: Context) {
        loadMoreReviews(context) { context, lastDoc, reviewList, newLastDoc, loading, isEnd ->
            ReviewRepository.getHomeReviews({ moreReviewList, moreLastDoc, isEndOfList ->
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
