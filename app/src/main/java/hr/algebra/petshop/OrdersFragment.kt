package hr.algebra.petshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hr.algebra.petshop.adapter.OrderAdapter
import hr.algebra.petshop.databinding.FragmentOrdersBinding
import hr.algebra.petshop.model.Order

class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var ordersRef: DatabaseReference
    private lateinit var ordersEventListener: ChildEventListener
    private val auth = FirebaseAuth.getInstance()
    private val orders = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ordersRef = Firebase.database.reference.child("orders").child(auth.currentUser?.uid.toString())
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = OrderAdapter(requireContext(), orders)
        }
        initListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListeners() {
        ordersEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue<Order>()
                if(order != null) {
                    orders.add(order)
                    binding.rvOrders.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue<Order>()
                if(order != null){
                    orders[orders.indexOf(orders.firstOrNull { it.pet_id == order.pet_id })] = order
                    binding.rvOrders.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) { Log.e(javaClass.name, error.message) }

        }
        ordersRef.addChildEventListener(ordersEventListener)
    }
}