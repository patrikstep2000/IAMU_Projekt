package hr.algebra.petshop.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PetTypeDTO(
    var name: String? = "",
    var breed: String? = ""
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "name" to name,
            "breed" to breed
        )
    }
}
