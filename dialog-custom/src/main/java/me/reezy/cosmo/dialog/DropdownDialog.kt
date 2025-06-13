package me.reezy.cosmo.dialog

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager


open class DropdownDialog(context: Context, anchor: View) : CustomDialog(context, 0) {
    init {
        setAnimation(translateY(-1f, 0f), translateY(0f, -1f))
        setLayout(MATCH_PARENT, WRAP_CONTENT)
        setCanceledOnTouchOutside(true)
        setBackgroundDimAmount(0.5f)


        val location = IntArray(2)
        val rect = Rect()
        anchor.getWindowVisibleDisplayFrame(rect)
        anchor.getLocationOnScreen(location)

        window?.let {
            val attrs = it.attributes
            attrs.gravity = Gravity.TOP or Gravity.START
            attrs.x = location[0]
            attrs.y = location[1] - rect.top + anchor.height
            attrs.width = WindowManager.LayoutParams.MATCH_PARENT
            attrs.height = rect.height() - attrs.y
            it.attributes = attrs
        }
    }

    fun setBackgroundDimAmount(amount: Float): CustomDialog {
        setDimAmount(0f)
        window?.setBackgroundDrawable(ColorDrawable((amount * 255).toInt() shl 24))
        return this
    }
}