package model



data class User(
    val username: String,
    val email: String,
    val role:String = "member",
    val status:String = "active" ) {
    var id: String = ""
    constructor() : this("", "")
}