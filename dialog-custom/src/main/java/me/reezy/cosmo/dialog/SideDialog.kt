package me.reezy.cosmo.dialog

import android.content.Context
import android.view.Gravity


open class SideDialog(context: Context, themeId: Int = 0, right: Boolean = false) : CustomDialog(context, themeId) {

    init {
        if (right) {
            window?.setGravity(Gravity.RIGHT)
            setAnimation(translateX(1f, 0f), translateX(0f, 1f))
            setLayout(WRAP_CONTENT, MATCH_PARENT)
            setCanceledOnTouchOutside(true)
        } else {
            window?.setGravity(Gravity.LEFT)
            setAnimation(translateX(-1f, 0f), translateX(0f, -1f))
            setLayout(WRAP_CONTENT, MATCH_PARENT)
            setCanceledOnTouchOutside(true)
        }
    }
}