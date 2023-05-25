package id.xxx.module.auth.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat

object RichTextUtils {

    fun setText(
        context: Context,
        firstText: String,
        lastText: String,
        lastTextOnClick: (view: View) -> Unit = {}
    ): SpannableString {
        val spannableString = SpannableString(lastText)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) = lastTextOnClick(view)

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        val start = lastText.indexOf(firstText)
        val end = (start + firstText.length)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            AbsoluteSizeSpan(12, true),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        val colorPrimary =
            ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary)
        spannableString.setSpan(
            ForegroundColorSpan(colorPrimary),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}