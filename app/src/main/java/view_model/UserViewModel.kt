package view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.User
import repository.UserRepository

class UserViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    init {
        UserRepository.getLoggedUser(){ user ->
            if(user != null){
                _currentUser.postValue(user)
            }
        }
    }
}

