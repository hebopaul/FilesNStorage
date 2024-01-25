package com.example.filesnstorage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.IOException

class MainViewModel(
    private val context: Context
): ViewModel() {

    var selectedImageUri: Uri? by mutableStateOf<Uri?>(null)

    var isFileListVisible: Boolean by mutableStateOf(false)



    fun updateSelectedImageUri(uri: Uri?) {
        selectedImageUri = uri
    }

    fun getFileList(): List<File> {
        val files = File(context.filesDir, "files").listFiles()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
        }
        return files?.toList()?: listOf()
    }

    fun saveImageToInternal(bmp: Bitmap, fileName: String) :Boolean {
        return try {
            context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE).use { stream ->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException("Could not save image")
                }
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()

            false
        }
    }

    private fun deleteImageFromInternal(context: Context) {
        val files = getFileList()
        for (file in files) {
            file.delete()
        }
    }





}