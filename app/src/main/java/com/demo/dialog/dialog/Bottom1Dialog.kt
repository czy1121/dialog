package com.demo.dialog.dialog

import android.content.Context
import com.demo.dialog.R
import me.reezy.cosmo.dialog.BottomDialog


open class Bottom1Dialog(context: Context, themeId: Int = 0) : BottomDialog(context, themeId) {

    init {
        setView(R.layout.dialog_bottom_1)
    }
}