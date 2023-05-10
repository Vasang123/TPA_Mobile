package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import model.User
import repository.ReviewRepository
import repository.UserRepository

class UserViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    //users
    protected val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    protected val lastDocumentSnapshot = MutableLiveData<DocumentSnapshot?>(null)

    init {
        UserRepository.getLoggedUser(){ user ->
            if(user != null){
                _currentUser.postValue(user)
            }
        }
    }

    fun loadUser(){
        UserRepository.fetchUsers(
            onSuccess = { users, newLastSnapshot, isEndOfList ->
                _userList.value = users
                lastDocumentSnapshot.value = newLastSnapshot
                isLastPage.value = isEndOfList
                isLoading.value = false
            },
            onFailure = { errorMessage ->
                isLoading.value = false
            },
            lastDocumentSnapshot = lastDocumentSnapshot.value
        )
    }

    fun loadMoreUser() {
        if (isLastPage.value == true || isLoading.value == true) return

        isLoading.value = true

        UserRepository.fetchUsers(
            onSuccess = { users, newLastSnapshot, isEndOfList ->
                val currentList = _userList.value ?: emptyList()
                _userList.value = currentList + users
                lastDocumentSnapshot.value = newLastSnapshot
                isLastPage.value = isEndOfList
                isLoading.value = false
            },
            onFailure = { errorMessage ->

                isLoading.value = false
            },
            lastDocumentSnapshot = lastDocumentSnapshot.value
        )
    }

    fun banUser(userID : String){
        UserRepository.updateUserStatus(userID,"banned", onSuccess = {}, onFailure = {})
    }

    fun unbanUser(userID : String){
        UserRepository.updateUserStatus(userID,"active", onSuccess = {}, onFailure = {})
    }

}

