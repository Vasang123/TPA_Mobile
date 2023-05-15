package repository

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.bluejack22_2.BeeTech.R
import model.Category
import java.util.*

object CategoryRepository {
    val db = FirebaseFirestore.getInstance()
    fun fetchCategory(
        onSuccess: (List<Category>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ) {
        val pageSize: Long = 10
        var categoryRef = db.collection("categories").orderBy("name", Query.Direction.DESCENDING)

        if (lastDocumentSnapshot != null) {
            categoryRef = categoryRef.startAfter(lastDocumentSnapshot)
        }

        categoryRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val categories = mutableListOf<Category>()
                for (doc in querySnapshot.documents) {
                    val category = doc.toObject(Category::class.java)!!
                    category.id = doc.id
                    categories.add(category)
                }
                val newLastDocumentSnapshot = if (categories.size < pageSize) {
                    null
                } else {
                    querySnapshot.documents.last()
                }

                val isEndOfList = categories.size < pageSize
                onSuccess(categories, newLastDocumentSnapshot, isEndOfList)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching category")
            }
    }
    fun addCategory(category: Category, context: Context, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val categoryRef = db.collection("categories").document(uid)
        categoryRef.set(category)
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.error_creating_review))
            }

    }

    fun deleteCategory(categoryId: String, context: Context, completion: (String?) -> Unit){
        val reviewRef = db.collection("categories").document(categoryId)
        reviewRef.delete()
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.failed_to_delete_category))
            }
    }
    fun updateCategory(name: String, id: String, context: Context, completion: (String?) -> Unit) {
        val documentRef = db.collection("categories").document(id)
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                documentRef.update(
                    "name", name
                ).addOnSuccessListener {
                    ReviewRepository.updateReviewCategory(id,name){
                        completion(it)
                    }
                    completion("Success")
                }.addOnFailureListener { exception ->
                    completion("Failed to update category: ${exception.message}")
                }
            } else {
                completion("Category document with ID '${id}' not found")
            }
        }.addOnFailureListener {
            completion(context.getString(R.string.failed_to_update_category))
        }
    }
    fun getCategoryById(categoyId:String, completion: (Category?) -> Unit) {
        val query = CommentRepository.db.collection("categories").document(categoyId)
        query.get().addOnSuccessListener { documentSnapshot ->
            val category = documentSnapshot.toObject(Category::class.java)
            if (category != null) {
                category.id = documentSnapshot.id
                completion(category)
            }
        }.addOnFailureListener {
            completion(null)
        }
    }



}