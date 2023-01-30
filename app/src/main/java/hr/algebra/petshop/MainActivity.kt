package hr.algebra.petshop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.petshop.databinding.ActivityMainBinding
import hr.algebra.petshop.framework.startActivity
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvEmail).text = auth.currentUser?.email

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open,  R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(PetsFragment(), "Pets")
        binding.navView.setCheckedItem(R.id.nav_pets)
        binding.btnLogout.setOnClickListener{ logoutUser() }
        binding.btnExit.setOnClickListener{ exitApp() }

        binding.navView.setNavigationItemSelectedListener {
            if(!it.isChecked){
                it.isChecked = true

                when(it.itemId){
                    R.id.nav_pets -> replaceFragment(PetsFragment(), it.title.toString())
                    R.id.nav_about -> replaceFragment(AboutFragment(), it.title.toString())
                    R.id.nav_orders -> replaceFragment(OrdersFragment(), it.title.toString())
                }
            }
            true
        }

        askNotificationPermission()
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        startActivity<StartActivity>()
    }

    private fun replaceFragment(fragment: Fragment, title: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        binding.drawer.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.exit)
            setIcon(R.drawable.exit)
            setMessage(getString(R.string.really))
            setCancelable(true)
            setPositiveButton("Ok") { _, _ -> finishAffinity() }
            setNegativeButton(getString(R.string.cancel), null)
            show()
        }
    }

    override fun onBackPressed() {}

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Log.e(javaClass.name, "Notifications permission approved.")
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder(this).apply {
                    setTitle("Notifications")
                    setMessage("Allow this app to send notifications?")
                    setCancelable(true)
                    setPositiveButton("OK"){_, _ -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)}
                    setNegativeButton("No thanks", null)
                    show()
                }
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}