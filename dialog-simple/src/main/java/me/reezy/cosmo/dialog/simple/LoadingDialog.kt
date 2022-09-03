package me.reezy.cosmo.dialog.simple

import android.content.Context
import android.view.View
import android.widget.*
import me.reezy.cosmo.dialog.simple.R
import me.reezy.cosmo.dialog.CustomDialog


class LoadingDialog(context: Context, compact: Boolean = false, text: String = context.getString(R.string.toosimple_loading_text)) : CustomDialog(context) {

    init {
        setView(R.layout.toosimple_loading)

        if (compact) {
            require<View>(android.R.id.widget_frame).setPadding(0, 0, 0, 0)
            require<TextView>(android.R.id.text1).visibility = View.GONE
        } else{
            require<TextView>(android.R.id.text1).text = text
        }
    }


}