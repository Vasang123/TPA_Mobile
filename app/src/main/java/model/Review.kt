package model

import java.util.Date

data class Review(
    var userId:String,
    var username:String,
    var categoryId:String,
    var categoryName:String,
    var imageURL:String,
    var title:String,
    var description:String,
    var createdAt:Date,
    var updatedAt:Date,
    var status:String,
    var totalFavorites:Long = 0) {
    var id: String = ""
    constructor() : this("", "", "", "", "", "", "", Date(), Date(),"", 0)
}