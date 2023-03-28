package Util



interface FirebaseInterface {
    fun createAccount(username:String, email: String, password: String,completion: (String?) -> Unit )
    fun login(email: String, password: String,completion: (String?) -> Unit )
}