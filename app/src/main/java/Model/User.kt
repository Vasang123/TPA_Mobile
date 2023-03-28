package Model

data class User(val uid: String, val username: String, val email: String) {
    constructor() : this("", "", "")
}
