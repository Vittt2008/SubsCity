package com.markus.subscity.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.util.ArrayMap
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * @author Vitaliy Markus
 */
class ClickableSpanBuilder(private val context: Context, private val text: String) {

    private val linkInfoArray: ArrayMap<String, SpanNode> = ArrayMap()

    constructor(context: Context, @StringRes text: Int) : this(context, context.getString(text))

    fun addLink(@StringRes linkText: Int, listener: () -> Unit) = addLink(context.getString(linkText), listener)

    fun addLink(@StringRes linkText: Int, @ColorRes color: Int, listener: () -> Unit) = addLink(context.getString(linkText), color, listener)

    fun addLink(linkText: String, listener: () -> Unit) = apply {
        linkInfoArray[linkText] = SpanNode(listener)
    }

    fun addLink(linkText: String, @ColorRes color: Int, listener: () -> Unit) = apply {
        linkInfoArray[linkText] = SpanNode(listener, ContextCompat.getColor(context, color))
    }

    fun build(): CharSequence {
        val spannableString = SpannableString.valueOf(text)
        for (linkText in linkInfoArray.keys) {
            val item = linkInfoArray.getValue(linkText)
            val clickableSpan = ColorClickableLinkSpan(item.listener, item.color)
            val startIndex = text.indexOf(linkText)
            spannableString.setSpan(clickableSpan, startIndex, startIndex + linkText.length, 0)
        }
        return spannableString
    }

    private class ColorClickableLinkSpan(private val listener: () -> Unit, private val color: Int) : ClickableSpan() {

        override fun onClick(widget: View) {
            listener.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            if (color != 0) {
                ds.color = color
            }
            ds.isUnderlineText = true
        }
    }

    private class SpanNode(val listener: () -> Unit, val color: Int = 0)
}