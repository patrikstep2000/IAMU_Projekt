package hr.algebra.petshop.handler

import android.content.Context
import hr.algebra.petshop.model.Pet
import hr.algebra.petshop.model.PetDTO
import hr.algebra.petshop.model.PetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun petDTOToPet(context: Context, dto: PetDTO?): Pet = withContext(Dispatchers.IO){
    val imagePath = downloadImageAndStore(context, dto?.image_path!!)
    return@withContext Pet(dto.id, dto.name, dto.details, imagePath, PetType(dto.type?.name, dto.type?.breed), dto.age, dto.price, dto.location, dto.sold)
}