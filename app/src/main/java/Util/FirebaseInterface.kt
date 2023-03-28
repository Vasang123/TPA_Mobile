package Util



interface FirebaseInterface {
    fun createAccount(username:String, email: String, password: String,completion: (String?) -> Unit )
}