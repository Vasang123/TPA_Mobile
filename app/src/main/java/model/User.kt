package model

data class User(val username: String, val email: String, val id:String = "",val role:String = "member",val status:String = "active" ) {
    constructor() : this("", "")
}
