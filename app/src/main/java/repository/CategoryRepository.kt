package repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import model.Category

object CategoryRepository {
    val db = FirebaseFirestore.getInstance()
    fun getAllCategory(
        onSuccess: (List<Category>) -> Unit,
        onFailure: (String) -> Unit) {
        var categoryRef = db.collection("categories")
        categoryRef.get()
            .addOnSuccessListener { querySnapshot ->
                val categories = mutableListOf<Category>()
                for (doc in querySnapshot.documents) {
                    val item = doc.toObject(Category::class.java)
                    if (item != null) {
                        item.id = doc.id
                        categories.add(item)
                    }
                }
                onSuccess(categories)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching category")
            }
    }

}