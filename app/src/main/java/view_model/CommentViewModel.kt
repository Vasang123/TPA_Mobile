package view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import repository.CommentRepository

class CommentViewModel : BaseCommentViewModel() {
    override fun loadComments(reviewId: String,context: Context) {
        CommentRepository.getComments(reviewId, { commentList, newLastDocumentSnapshot, isEndOfList ->
            _commentList.value = commentList
            lastDocumentSnapshot.value = newLastDocumentSnapshot
            isLoading.value = false
            isLastPage.value = isEndOfList
        }, { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

            isLoading.value = false
        })
    }

    fun loadMoreReviewComments(reviewId : String, context: Context) {
        loadMoreComments(reviewId, context) { context, lastDoc, commentList, newLastDoc, loading, isEnd ->
            CommentRepository.getComments(reviewId, { moreCommentList, moreLastDoc, isEndOfList ->
                commentList.value = commentList.value?.plus(moreCommentList)
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