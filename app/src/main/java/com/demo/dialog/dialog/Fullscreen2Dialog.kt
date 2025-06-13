package com.demo.dialog.dialog

import android.content.Context
import android.view.View
import com.demo.dialog.R
import me.reezy.cosmo.dialog.FullscreenDialog


open class Fullscreen2Dialog(context: Context, themeId: Int = 0) : FullscreenDialog(context, themeId) {

    init {
        setView(R.layout.dialog_fullscreen_2)

        findViewById<View>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
    }
}