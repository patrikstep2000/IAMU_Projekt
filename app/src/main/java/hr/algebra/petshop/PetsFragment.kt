package hr.algebra.petshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hr.algebra.petshop.adapter.PetAdapter
import hr.algebra.petshop.databinding.FragmentPetsBinding
import hr.algebra.petshop.handler.petDTOToPet
import hr.algebra.petshop.model.Pet
import hr.algebra.petshop.model.PetDTO
import kotlinx.coroutines.*

class PetsFragment : Fragment() {
    private lateinit var binding: FragmentPetsBinding
    private lateinit var pets: MutableList<Pet>
    private val database = Firebase.database.reference.child("pets")
    private lateinit var childEventListener: ChildEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetsBinding.inflate(inflater, container, false)
        pets = mutableListOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PetAdapter(requireContext(), pets)
        }
        initDBListeners()
    }

    override fun onStop() {
        database.removeEventListener(childEventListener)
        super.onStop()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initDBListeners() {
        childEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                lifecycleScope.launch{
                    val pet = snapshot.getValue<PetDTO>()
                    if(pet?.sold != true){
                        pets.add(petDTOToPet(requireContext(), pet))
                        pets.sortBy { it.id }
                        binding.rvItems.adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                lifecycleScope.launch{
                    val pet = snapshot.getValue<PetDTO>()
                    pets.remove(pets.firstOrNull { it.id == pet?.id })
                    pets.sortBy { it.id }
                    binding.rvItems.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                lifecycleScope.launch{
                    val dto = snapshot.getValue<PetDTO>()
                    if(dto?.sold == false){
                        val newPet = pets.firstOrNull{p -> p.id == dto.id }
                        if(newPet != null){
                            pets[pets.indexOf(pets.firstOrNull{p -> p.id == dto.id })] = petDTOToPet(requireContext(), dto)
                        }
                        else{
                            pets.add(petDTOToPet(requireContext(), dto))
                        }
                    }
                    else{
                        pets.remove(pets.firstOrNull { it.id == dto?.id })
                    }
                    pets.sortBy { it.id }
                    binding.rvItems.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Log.e(javaClass.name, error.message)
            }

        }
        database.addChildEventListener(childEventListener)
    }
}