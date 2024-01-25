package com.example.filesnstorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Uri.toBitmap(context: Context): Bitmap = if (Build.VERSION.SDK_INT < 28) {
    MediaStore.Images.Media.getBitmap(
        context.contentResolver, this
    )
} else {
    ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
            context.contentResolver, this)
    )
}