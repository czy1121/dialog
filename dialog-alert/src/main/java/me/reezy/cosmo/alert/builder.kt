package me.reezy.cosmo.alert

import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import me.reezy.cosmo.R


private fun Context.resolveAlertDialogTheme(@StyleRes themeId: Int): Int {
    if (themeId.ushr(24) and 0x000000ff >= 0x00000001) return themeId

    val outValue = TypedValue()
    theme.resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true)
    return outValue.resourceId
}

//===============================================
// AlertDialog.Builder
//===============================================
fun Context.alertDialog(message: CharSequence? = null, title: CharSequence? = null, @StyleRes themeId: Int = 0): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this, resolveAlertDialogTheme(themeId))
    if (title != null) {
        builder.setTitle(title)
    }
    if (message is CharSequence) {
        builder.setMessage(message)
    }
    return builder
}

inline fun AlertDialog.Builder.choose(items: Array<CharSequence>, crossinline onClick: (DialogInterface, Int) -> Unit): AlertDialog.Builder {
    return setItems(items) { dialog, which -> onClick(dialog, which) }
}

inline fun AlertDialog.Builder.choose(items: Array<CharSequence>, checkedItem: Int, crossinline onClick: (DialogInterface, Int) -> Unit): AlertDialog.Builder {
    return setSingleChoiceItems(items, checkedItem) { dialog, which -> onClick(dialog, which) }
}

inline fun AlertDialog.Builder.choose(items: Array<CharSequence>, checkedItems: BooleanArray, crossinline onClick: (DialogInterface, Int, Boolean) -> Unit): AlertDialog.Builder {
    return setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked -> onClick(dialog, which, isChecked) }
}

inline fun AlertDialog.Builder.ok(text: CharSequence = context.getString(R.string.toosimple_alert_ok), crossinline onClick: () -> Unit = {}): AlertDialog.Builder {
    return setPositiveButton(text) { _, _ -> onClick() }
}

inline fun AlertDialog.Builder.skip(text: CharSequence = context.getString(R.string.toosimple_alert_skip), crossinline onClick: () -> Unit = {}): AlertDialog.Builder {
    return setNegativeButton(text) { _, _ -> onClick() }
}

inline fun AlertDialog.Builder.cancel(text: CharSequence = context.getString(R.string.toosimple_alert_cancel), crossinline onClick: () -> Unit = {}): AlertDialog.Builder {
    return setNegativeButton(text) { _, _ -> onClick() }
}


//===============================================
// AlertDialog
//===============================================
fun AlertDialog.html(message: String): AlertDialog {
    setMessage(Html.fromHtml(message))
    findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
    return this
}

fun AlertDialog.link(): AlertDialog {
    findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
    return this
}

fun AlertDialog.centered(): AlertDialog {
    findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER_HORIZONTAL
    return this
}