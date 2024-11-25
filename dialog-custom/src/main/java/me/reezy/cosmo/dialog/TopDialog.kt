package me.reezy.cosmo.dialog

import android.content.Context
import android.view.Gravity

open class TopDialog(context: Context, themeId: Int = 0) : CustomDialog(context, themeId) {

    init {
        window?.setGravity(Gravity.TOP)
        setAnimation(translateY(-1f, 0f), translateY(0f, -1f))
        setLayout(MATCH_PARENT, WRAP_CONTENT)
        setCanceledOnTouchOutside(true)
    }
}