package hr.algebra.petshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import hr.algebra.petshop.databinding.ActivityMainBinding
import hr.algebra.petshop.databinding.ActivityStartBinding
import hr.algebra.petshop.framework.startActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }
        binding.btnLogin.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }
}