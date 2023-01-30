package hr.algebra.petshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hr.algebra.petshop.adapter.POSITION
import hr.algebra.petshop.databinding.ActivityBuyPetBinding
import hr.algebra.petshop.framework.startActivity
import hr.algebra.petshop.handler.petDTOToPet
import hr.algebra.petshop.model.Order
import hr.algebra.petshop.model.Pet
import hr.algebra.petshop.model.PetDTO
import kotlinx.coroutines.launch
import java.util.*

class BuyPetActivity : AppCompatActivity() {
    private var database = Firebase.database.reference.child("pets")
    private var ordersRef = Firebase.database.reference.child("orders")
    private var auth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityBuyPetBinding
    private var petId = 0
    private lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = this
        petId = intent.getIntExtra(POSITION, petId)
        database.child(petId.toString()).get().addOnSuccessListener {
            val dto = it.getValue<PetDTO>()
            lifecycleScope.launch{
                pet = petDTOToPet(context, dto)
                initButtonListeners()
                initLabels()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLabels() {
        binding.tvPrice.text = "${binding.tvPrice.text} ${pet.price.toString()} €"
    }

    private fun initButtonListeners() {
        binding.btnBuy.setOnClickListener{
            val nameOnCard = binding.etNameOnCard.text.toString()
            val cardNumber = binding.etCardNumber.text.toString()
            val month = binding.etExpDateMonth.text.toString()
            val year = binding.etExpDateYear.text.toString()
            val currYear = Calendar.getInstance().get(Calendar.YEAR) % 100

            if(nameOnCard.isEmpty() || cardNumber.isEmpty() || month.isEmpty() || year.isEmpty()){
                Toast.makeText(this, "Please input all data", Toast.LENGTH_SHORT).show()
            }
            else if (month.toInt() < 1 || month.toInt() > 12 || year.toInt() < currYear){
                Toast.makeText(this, "Wrong expiration date.", Toast.LENGTH_SHORT).show()
            }
            else{
                ordersRef.child("${auth.currentUser?.uid}").push().setValue(Order(pet.id, false).toMap())
                database.child(pet.id.toString()).child("sold").setValue(true)
                Toast.makeText(this, "Successfully bought your pet. You can view your orders under orders tab.", Toast.LENGTH_SHORT).show()
                startActivity<MainActivity>()
            }
        }
    }
}