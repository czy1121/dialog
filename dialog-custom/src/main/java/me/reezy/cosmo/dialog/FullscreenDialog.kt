package me.reezy.cosmo.dialog

import android.content.Context
import android.os.Build
import android.view.WindowManager

open class FullscreenDialog(context: Context, themeId: Int = 0) : CustomDialog(context, themeId) {

    init {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                attributes = attributes.apply {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setDecorFitsSystemWindows(false)
            }
            setDimAmount(0.5f)
        }
        setLayout(MATCH_PARENT, MATCH_PARENT)
        setCanceledOnTouchOutside(false)
    }
}