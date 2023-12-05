package com.demo.dialog

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun logE(message: String) {
    Log.e("ezy.dialog", message)
}

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER) {
    val t = Toast.makeText(this, text, duration)
    t.setGravity(gravity, 0, 0)
    t.show()
}

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER) {
    requireActivity().toast(text, duration, gravity)
}
