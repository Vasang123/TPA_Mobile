package repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import model.Comment
import model.Review
import java.util.*

object CommentRepository {
    val db = FirebaseFirestore.getInstance()
    fun insertComment(comment: Comment, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val reviewRef = db.collection("comments").document(uid)
        reviewRef.set(comment)
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                completion("Error creating comment in database")
            }
    }
    fun fetchComments(
        configureQuery: (Query) -> Query,
        onSuccess: (List<Comment>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ) {
        val pageSize: Long = 5
        var commentRef = db.collection("comments")
            .whereEqualTo("status","active")
        commentRef = configureQuery(commentRef)


        if (lastDocumentSnapshot != null) {
            commentRef = commentRef.startAfter(lastDocumentSnapshot)
        }

        commentRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val comments = mutableListOf<Comment>()
                for (doc in querySnapshot.documents) {
                    val comment = doc.toObject(Comment::class.java)!!
                    comment?.id = doc.id
                    comments.add(comment)
                }
                val newLastDocumentSnapshot = if (comments.size < pageSize) {
                    null
                } else {
                    querySnapshot.documents.last()
                }

                val isEndOfList = comments.size < pageSize
                onSuccess(comments, newLastDocumentSnapshot, isEndOfList)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching reviews")
                Log.e("Query Error", e.localizedMessage)
            }
    }

    fun getComments(
        reviewId: String,
        onSuccess: (List<Comment>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null) {
        fetchComments(
            configureQuery = { query ->
                query
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .whereEqualTo("reviewId", reviewId)
            },
            onSuccess = onSuccess,
            onFailure = onFailure,
            lastDocumentSnapshot = lastDocumentSnapshot
        )

    }
    fun deleteComment(commentId:String,completion: (String?) -> Unit){
        val commentRef = db.collection("comments").document(commentId)
        commentRef.delete()
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                completion("Failed to delete review")
            }

    }
}