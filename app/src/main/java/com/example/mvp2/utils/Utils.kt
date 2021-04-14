package com.example.mvp2.utils

import android.graphics.Typeface
import android.text.Spannable

import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
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