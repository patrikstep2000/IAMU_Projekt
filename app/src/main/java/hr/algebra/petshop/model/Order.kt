package hr.algebra.petshop.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    var pet_id: Int? = 0,
    var arrived: Boolean? = false
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "pet_id" to pet_id,
            "arrived" to arrived
        )
    }
}