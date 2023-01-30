package hr.algebra.petshop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.petshop.PetActivity
import hr.algebra.petshop.R
import hr.algebra.petshop.framework.startActivity
import hr.algebra.petshop.model.Pet
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

const val POSITION = "hr.algebra.nasa.position"
class PetAdapter(private val context: Context, private val pets: MutableList<Pet>)
    : RecyclerView.Adapter<PetAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvType = itemView.findViewById<TextView>(R.id.tvType)
        private val tvAge = itemView.findViewById<TextView>(R.id.tvAge)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)

        @SuppressLint("SetTextI18n")
        fun bind(pet: Pet) {
            Picasso.get()
                .load(File(pet.image_path!!))
                .error(R.drawable.pet_shop)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvName.text = pet.name
            tvType.text = "Breed: " + pet.type?.name + ", " + pet.type?.breed
            tvAge.text = "Age: " + pet.age.toString()
            tvPrice.text = pet.price.toString() + "€"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = pets[position]

        holder.itemView.setOnClickListener {
            context.startActivity<PetActivity>(POSITION, pet.id!!)
        }

        holder.bind(pet)
    }

    override fun getItemCount() = pets.size
}