package repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.bluejack22_2.BeeTech.R
import model.Comment
import java.util.*

object CommentRepository {
    val db = FirebaseFirestore.getInstance()
    fun insertComment(comment: Comment, context: Context, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val reviewRef = db.collection("comments").document(uid)
        reviewRef.set(comment)
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.error_creating_comment))
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
    fun deleteComment(commentId: String, context: Context, completion: (String?) -> Unit){
        val commentRef = db.collection("comments").document(commentId)
        commentRef.delete()
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.failed_to_delete_comment))
            }

    }
    fun getCommentById(commentId:String, completion: (Comment?) -> Unit) {
        val query = db.collection("comments").document(commentId)
        query.get().addOnSuccessListener { documentSnapshot ->
            val comment = documentSnapshot.toObject(Comment::class.java)
            if (comment != null) {
                comment.id = documentSnapshot.id
                completion(comment)
            }
        }.addOnFailureListener {
            completion(null)
        }
    }
    fun updateComment(
        content: String,
        commentId: String,
        context: Context,
        completion: (String?) -> Unit
    ) {
        val documentRef = db.collection("comments").document(commentId)
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                documentRef.update(
                    "content", content,
                    "updatedAt", Date()
                ).addOnSuccessListener {
                    completion(context.getString(R.string.success))
                }.addOnFailureListener { exception ->
                    completion("Failed to update comment: ${exception.message}")
                }
            } else {
                completion("Review document with ID '${commentId}' not found")
            }
        }.addOnFailureListener {
            completion(context.getString(R.string.failed_to_update_comment))
        }
    }
}