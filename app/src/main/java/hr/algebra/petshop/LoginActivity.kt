package hr.algebra.petshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.petshop.databinding.ActivityLoginBinding
import hr.algebra.petshop.framework.startActivity

class LoginActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please input email and password.", Toast.LENGTH_SHORT).show()
            }
            else{
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
            startActivity<MainActivity>()
        }.addOnFailureListener {
            Toast.makeText(this, "Login failed, check email and password.", Toast.LENGTH_SHORT).show()
        }
    }
}