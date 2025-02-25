package com.morse.core.binding_adapters

import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.morse.core.R
import java.util.Locale


@BindingAdapter("playGif")
fun AppCompatImageView.playGif(@DrawableRes gifResource: Int) {
    Glide.with(context).asGif().load(gifResource).into(this)
}

private fun String.isValidImageUrl(locale: Locale): Boolean {
    return this.toLowerCase(locale).endsWith("jpg") || this.toLowerCase(locale).endsWith("jpeg") || this.toLowerCase(locale).endsWith("png")
}

@BindingAdapter("loadImage")
fun ImageView.loadCirculeImage(
    imageUrl: String? = null
) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    with(circularProgressDrawable) {
        setColorSchemeColors(
            android.R.color.holo_red_dark ,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_blue_light
        )
        strokeWidth = 5f
        centerRadius = 35f
        start()
    }
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(20))
    if (TextUtils.isEmpty(imageUrl) && (imageUrl?.isValidImageUrl(Locale.ROOT)) != true) {
        Glide.with(context).load("https://randomuser.me/api/portraits/lego/6.jpg").placeholder(circularProgressDrawable)
            .apply(requestOptions)
            .error(R.drawable.error_loading_image).into(this)
    } else {
        Glide.with(context).load(imageUrl).placeholder(circularProgressDrawable)  .apply(requestOptions)
            .error(R.drawable.error_loading_image).into(this)
    }

}