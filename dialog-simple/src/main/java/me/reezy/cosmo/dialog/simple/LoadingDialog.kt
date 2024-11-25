package me.reezy.cosmo.dialog.simple

import android.content.Context
import android.graphics.drawable.Animatable
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import me.reezy.cosmo.dialog.CustomDialog


class LoadingDialog(context: Context, compact: Boolean = false, iconResId: Int = 0, text: String? = null) : CustomDialog(context) {

    init {
        setView(R.layout.toosimple_loading)
        setDimAmount(0f)

        if (compact) {
            require<View>(android.R.id.widget_frame).setPadding(0, 0, 0, 0)
            require<TextView>(android.R.id.text1).visibility = View.GONE
        } else{
            val text1 = text ?: context.getString(R.string.toosimple_loading_text)
            require<TextView>(android.R.id.text1).isVisible = text1.isNotEmpty()
            require<TextView>(android.R.id.text1).text = text1
        }

        require<ImageView>(android.R.id.icon).apply {

            if (iconResId > 0) {
                setImageResource(iconResId)
            }

            val d = drawable ?: return@apply
            if (d is Animatable) {
                clearAnimation()
                d.start()
            } else {
                val spinner = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                spinner.repeatCount = Animation.INFINITE
                spinner.duration = 1000
                spinner.interpolator = Interpolator { input -> (input * 12).toInt() / 12f }
                startAnimation(spinner)
            }
        }
    }


}