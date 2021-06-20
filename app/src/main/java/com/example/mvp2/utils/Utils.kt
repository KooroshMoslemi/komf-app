package com.example.mvp2.utils

import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable

import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvp2.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Matcher
import java.util.regex.Pattern


fun makeSpannable(text: String?, regex: String?, startTag: String, endTag: String): SpannableStringBuilder? {
    val sb = StringBuffer()
    val spannable = SpannableStringBuilder()
    val pattern: Pattern = Pattern.compile(regex)
    val matcher: Matcher = pattern.matcher(text)
    while (matcher.find()) {
        sb.setLength(0)
        val group: String = matcher.group()
        val spanText = group.substring(startTag.length, group.length - endTag.length)
        matcher.appendReplacement(sb, spanText)
        spannable.append(sb.toString())
        val start = spannable.length - spanText.length
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    sb.setLength(0)
    matcher.appendTail(sb)
    spannable.append(sb.toString())
    return spannable
}


fun hideBottomNavigationView(view: BottomNavigationView) {
    //view.animate().translationY(view.height.toFloat())
    view.visibility = View.GONE
}

fun showBottomNavigationView(view: BottomNavigationView) {
    //view.animate().translationY(0f)
    view.visibility = View.VISIBLE
}

fun NavController.doIfCurrentDestination(@IdRes destination: Int, action: NavController.()-> Unit){
    if(this.currentDestination?.id == destination){action()}
}

fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(uri)
    if (applyCircle) {
        glide.apply(
                RequestOptions.circleCropTransform()
                        .placeholder(resources.getDrawable(R.mipmap.default_profile))
                        .error(resources.getDrawable(R.mipmap.default_profile))
        ).into(this)
    } else {
        glide.into(this)
    }
}

fun ImageView.setLocalImage(url: String, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(url.toUri())
    if (applyCircle) {
        glide.apply(
                RequestOptions.circleCropTransform()
                        .placeholder(resources.getDrawable(R.mipmap.default_profile))
                        .error(resources.getDrawable(R.mipmap.default_profile))
        ).into(this)
    } else {
        glide.into(this)
    }
}