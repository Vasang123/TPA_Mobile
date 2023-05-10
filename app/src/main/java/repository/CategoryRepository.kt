package repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import model.Category
import model.Comment
import model.Review
import model.User
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
    fun addCategory(category: Category, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val categoryRef = db.collection("categories").document(uid)
        categoryRef.set(category)
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                completion("Error creating review record in database")
            }

    }

    fun deleteCategory(categoryId:String,completion: (String?) -> Unit){
        val reviewRef = db.collection("categories").document(categoryId)
        reviewRef.delete()
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                completion("Failed to delete category")
            }
    }
    fun updateCategory( name: String, id : String, completion: (String?) -> Unit) {
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
            completion("Failed to Update Category")
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