package com.denimsoftware.safe_boda_challange

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response

/**
 *Created by Yerimah on 7/10/2020.
 */

@SuppressLint("StaticFieldLeak")
private var snackBar: Snackbar? = null

fun loadImage(context: Context, imageView: ImageView, imageUrl: String, placeHolder: Int) {

    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions().fitCenter())
        .apply(
            RequestOptions
            .placeholderOf(R.drawable.loading_image)
            .error(placeHolder))
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .listener(object : RequestListener<Drawable> {

            override fun onLoadFailed(e: GlideException?, model: Any,
                                      target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                return false
            }
            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>,
                                         dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(imageView)
}

fun successMessage(context: AppCompatActivity, layout: ConstraintLayout, message: String) {
    layout.hideKeyboard()
    snackBar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
    val snackBarLayout = snackBar!!.view
    snackBarLayout.setBackgroundColor(Color.GREEN)
    val textView = snackBarLayout.findViewById<TextView>(R.id.snackbar_text)
    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.good_icon, 0, 0, 0)
    textView.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.five_dp)
    snackBar!!.show()
}

fun errorMessage(context: Context, layout: ConstraintLayout,
                 message: String, actionTitle: String, action: View.OnClickListener) {
    layout.hideKeyboard()
    snackBar = Snackbar.make(layout, message, Snackbar.LENGTH_INDEFINITE)
    snackBar!!.setAction(actionTitle, action)
    snackBar!!.setActionTextColor(ContextCompat.getColor(context, android.R.color.white))
    val snackBarLayout = snackBar!!.view
    snackBarLayout.setBackgroundColor(Color.RED)
    val textView = snackBarLayout.findViewById<TextView>(R.id.snackbar_text)
    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_outline_black_24dp, 0, 0, 0)
    textView.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.five_dp)
    snackBar!!.show()
}


fun dismissSnackBar() {

    if (snackBar != null) {
        snackBar!!.dismiss()
    }
}

fun View.hideKeyboard() {
    val hideAction = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    hideAction.hideSoftInputFromWindow(windowToken, 0)
}


fun showErrorMessage(activity: AppCompatActivity,
                     constraintLayout: ConstraintLayout?,
                     message: String,
                     action: View.OnClickListener,
                     actionTitle: String) {
    if (constraintLayout != null) {
        errorMessage(activity, constraintLayout, message, actionTitle, action)
    }
}
