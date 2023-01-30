package hr.algebra.petshop.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PetDTO(
    var id: Int? = 0,
    var name: String? = "",
    var details: String? = "",
    var image_path: String? = "",
    var type: PetTypeDTO? = PetTypeDTO(),
    var age: Int? = 0,
    var price: Double? = 0.0,
    var location: String? = "",
    var sold: Boolean? = false
){
    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "id" to id,
            "name" to name,
            "details" to details,
            "image_path" to image_path,
            "type" to type,
            "age" to age,
            "price" to price,
            "location" to location,
            "sold" to sold
        )
    }
}
