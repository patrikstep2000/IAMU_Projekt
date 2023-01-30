package hr.algebra.petshop

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import hr.algebra.petshop.adapter.POSITION
import hr.algebra.petshop.databinding.ActivityPetBinding
import hr.algebra.petshop.framework.startActivity
import hr.algebra.petshop.handler.petDTOToPet
import hr.algebra.petshop.model.Pet
import hr.algebra.petshop.model.PetDTO
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.launch
import java.io.File

class PetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetBinding
    private val database = Firebase.database.reference.child("pets")
    private lateinit var pet: Pet
    private var petId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context = this

        petId = intent.getIntExtra(POSITION, petId)
        database.child(petId.toString()).get().addOnSuccessListener {
            val dto = it.getValue<PetDTO>()
            lifecycleScope.launch{
                pet = petDTOToPet(context, dto)
                initButtonListener()
                initLabels()
            }
        }
    }

    private fun initButtonListener() {
        binding.btnBuy.setOnClickListener{
            startActivity<BuyPetActivity>(POSITION, petId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLabels() {
        Picasso.get()
            .load(File(pet.image_path!!))
            .error(R.drawable.pet_shop)
            .transform(RoundedCornersTransformation(50, 5))
            .into(binding.ivItem)

        binding.tvName.text = "Name " + pet.name
        binding.tvType.text = "Breed: " + pet.type?.name + ", " + pet.type?.breed
        binding.tvAge.text = "Age: " + pet.age.toString()
        binding.tvPrice.text = "Price: " + pet.price.toString() + " €"
        binding.tvLocation.text = "Location: " + pet.location
        binding.tvDetails.text = "Details: " + pet.details
    }
}