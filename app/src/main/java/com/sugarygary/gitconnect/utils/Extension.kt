package com.sugarygary.gitconnect.utils

import android.R.id.content
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sugarygary.gitconnect.R.drawable.avatar


fun ImageView.glide(imageUrl: String) {
    Glide.with(this.rootView).load(imageUrl).apply(
        RequestOptions().placeholder(
            avatar
        ).error(avatar)
    ).into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun FragmentActivity.makeSnackbar(
    message: CharSequence, actionName: CharSequence?, action: View.OnClickListener?
) {
    Snackbar.make(this.findViewById(content), message, Snackbar.LENGTH_LONG)
        .setAction(actionName, action).show()
}
