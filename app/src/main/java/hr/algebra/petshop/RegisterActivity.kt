package hr.algebra.petshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.petshop.databinding.ActivityLoginBinding
import hr.algebra.petshop.databinding.ActivityRegisterBinding
import hr.algebra.petshop.framework.startActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()
            val confirmPassword = binding.etConfirmPassword.editText?.text.toString()

            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, "Please input email and password.", Toast.LENGTH_SHORT).show()
            }
            else if (password.length < 6){
                Toast.makeText(this, "Password needs to be 6 characters or longer.", Toast.LENGTH_SHORT).show()
            }
            else if (password != confirmPassword){
                Toast.makeText(this, "Password must match.", Toast.LENGTH_SHORT).show()
            }
            else {
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
            startActivity<MainActivity>()
        }.addOnFailureListener{
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}