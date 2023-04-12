package model

import java.util.Date

data class Review(
    var userId:String,
    var username:String,
    var imageURL:String,
    var title:String,
    var description:String,
    var createdAt:Date,
    var updatedAt:Date,
    var status:String,
    val totalFavorites:Number = 0) {
    var id: String = ""
    constructor() : this("", "", "", "", "", Date(), Date(),"", 0)
}