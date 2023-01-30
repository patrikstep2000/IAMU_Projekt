package hr.algebra.petshop.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

data class Pet(
    var id: Int?,
    var name: String?,
    var details: String?,
    var image_path: String?,
    var type: PetType?,
    var age: Int?,
    var price: Double?,
    var location: String?,
    var sold: Boolean?
)
