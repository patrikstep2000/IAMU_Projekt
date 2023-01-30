package hr.algebra.petshop.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import hr.algebra.petshop.handler.downloadImageAndStore
import hr.algebra.petshop.model.Pet
import hr.algebra.petshop.model.PetDTO
import hr.algebra.petshop.model.PetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun View.applyAnimation(animationId: Int) = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun <reified T:Activity> Context.startActivity() = startActivity(Intent(this, T::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

inline fun<reified T: Activity> Context.startActivity(key: String, value: Int) =
    startActivity(Intent(this, T::class.java).apply { putExtra(key, value) }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

inline fun <reified T: BroadcastReceiver> Context.sendBroadcast() = sendBroadcast(Intent(this, T::class.java))

@Suppress("DEPRECATION")
fun Context.setBooleanPreference(key: String, value: Boolean = true){
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

@Suppress("DEPRECATION")
fun Context.getBooleanPreference(key: String) = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false)

fun Context.isOnline(): Boolean{
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network).let { cap ->
            if (cap != null) {
                return cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            }
        }
    }
    return false
}

fun callDelayed(delay: Long, runnable: Runnable) = Handler(Looper.getMainLooper()).postDelayed(runnable, delay)