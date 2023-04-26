package model

import java.util.Date

data class Comment(
            var userId : String,
            var username : String,
            var status : String,
            var content : String,
            var createdAt : Date,
            var updatedAt : Date,
            var reviewId : String
) {
    var id: String = ""
    constructor(): this("","","" ,"",Date(),Date(), "")
}