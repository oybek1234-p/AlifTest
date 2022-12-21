package com.uz.aliftest

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun isImageUrl(text: String) =
        text.endsWith(".jpg") || text.endsWith(".png") || text.endsWith(".webp")

}

fun View.visibleOrGone(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String,circle: Boolean = false) {
    val glide = Glide.with(context)
    var request = glide
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())

    if (circle) {
        request = request.circleCrop()
    }
    request.into(this)
}

fun Context.toast(message: String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}