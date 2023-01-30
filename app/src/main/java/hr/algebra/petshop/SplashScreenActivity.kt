package hr.algebra.petshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.petshop.databinding.ActivitySplashScreenBinding
import hr.algebra.petshop.framework.applyAnimation
import hr.algebra.petshop.framework.callDelayed
import hr.algebra.petshop.framework.isOnline
import hr.algebra.petshop.framework.startActivity


private const val DELAY = 3000L
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()

        if (isOnline()) {
            redirect()
        } else {
            binding.tvSplash.text = getString(R.string.no_internet)
            callDelayed(DELAY) { finish() }
        }
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.ivSplash.applyAnimation(R.anim.rotate)
    }

    private fun redirect() {
        if(FirebaseAuth.getInstance().currentUser != null){
            callDelayed(DELAY) { startActivity<MainActivity>() }
        }
        else{
            callDelayed(DELAY) { startActivity<StartActivity>() }
        }
    }
}