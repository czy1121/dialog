package me.reezy.cosmo.dialog.simple

import android.content.Context
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import me.reezy.cosmo.dialog.CustomDialog

class MessageDialog(context: Context, message: CharSequence? = null, title: String? = null, closeButton: Boolean = false, layoutResId: Int = R.layout.toosimple_message) : CustomDialog(context) {

    init {
        setDimAmount(0.6f)
        setView(layoutResId)

        setCanceledOnTouchOutside(false)

        if (title != null) {
            findViewById<TextView>(android.R.id.title)?.text = title
        }
        if (message.isNullOrEmpty()) {
            findViewById<TextView>(android.R.id.message)?.visibility = View.GONE
        } else {
            findViewById<TextView>(android.R.id.message)?.let {
                if (message is Spanned) {
                    it.movementMethod = LinkMovementMethod.getInstance()
                }
                it.text = message
            }
        }
        if (closeButton) {
            findViewById<View>(android.R.id.closeButton)?.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    fun ack(confirmText: String? = context.getString(R.string.toosimple_simple_known), onConfirm: () -> Unit = {}):MessageDialog {
        setButton(R.id.btn_confirm, confirmText, onConfirm)
        return this
    }

    fun confirm(cancelText: String? = null, confirmText: String? = null, onCancel: () -> Unit = {}, onConfirm: () -> Unit = {}):MessageDialog {
        setButton(R.id.btn_cancel, cancelText, onCancel)
        setButton(R.id.btn_confirm, confirmText, onConfirm)
        return this
    }

    private fun setButton(buttonId: Int, buttonText: String?, onClick: () -> Unit) {
        findViewById<TextView>(buttonId)?.let {
            it.visibility = View.VISIBLE
            if (buttonText != null) {
                it.text = buttonText
            }
            it.setOnClickListener {
                onClick()
                dismiss()
            }
        }
    }
}