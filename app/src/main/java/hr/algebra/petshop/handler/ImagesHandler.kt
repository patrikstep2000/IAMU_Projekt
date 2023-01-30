package hr.algebra.petshop.handler

import android.content.Context
import android.util.Log
import hr.algebra.petshop.factory.createHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

fun downloadImageAndStore(context: Context, url: String): String? {
    val filename = url.substring(url.lastIndexOf("/") + 1)
    val file = createFile(context, filename)

    try {
        val con: HttpURLConnection = createHttpUrlConnection(url)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    } catch (e: Exception){
        Log.e("IMAGESHANDLER", e.toString(), e)
    }

    return null
}

private fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if(file.exists()) file.delete()
    return file
}
