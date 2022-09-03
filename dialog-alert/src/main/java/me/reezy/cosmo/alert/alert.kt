package me.reezy.cosmo.alert


import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import me.reezy.cosmo.R


//===============================================
// Context -> tip/alert/confirm/choose
//===============================================

// tip => 无按钮
fun Context.tip(message: CharSequence, title: CharSequence? = null, @StyleRes themeId: Int = 0): AlertDialog {
    return alertDialog(message, title, themeId).show().centered()
}

// dialog-dialog => 一个按钮(确定)
inline fun Context.alert(
    message: CharSequence,
    title: CharSequence? = null,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    @StyleRes themeId: Int = 0,
    crossinline onOk: () -> Unit = {}
): AlertDialog {
    return alertDialog(message, title, themeId).ok(okText, onOk).show().centered().apply {
        setCanceledOnTouchOutside(false)
    }
}

// confirm => 两个按钮(确定 & 取消)
inline fun Context.confirm(
    message: CharSequence,
    title: CharSequence? = null,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    cancelText: CharSequence = getString(R.string.toosimple_alert_cancel),
    @StyleRes themeId: Int = 0,
    crossinline onCancel: () -> Unit = {},
    crossinline onOk: () -> Unit = {}
): AlertDialog {
    return alertDialog(message, title, themeId).ok(okText, onOk).cancel(cancelText, onCancel).show().centered().apply {

        setCanceledOnTouchOutside(false)
    }
}

// choose => 选择列表，无按钮
inline fun Context.choose(
    items: Array<CharSequence>,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    crossinline onClick: (DialogInterface, Int) -> Unit
): AlertDialog {
    return alertDialog(null, title, themeId).choose(items, onClick).show()
}

//===============================================
// Fragment -> tip/alert/confirm/choose
//===============================================
fun Fragment.tip(message: CharSequence, title: CharSequence? = null, @StyleRes themeId: Int = 0): AlertDialog {
    return requireContext().alertDialog(message, title, themeId).show().centered()
}

inline fun Fragment.alert(
    message: CharSequence,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    crossinline onOk: () -> Unit = {}
): AlertDialog {
    return requireContext().alertDialog(message, title, themeId).ok(okText, onOk).show().centered().apply {
        setCanceledOnTouchOutside(false)
    }
}

inline fun Fragment.confirm(
    message: CharSequence,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    cancelText: CharSequence = getString(R.string.toosimple_alert_cancel),
    crossinline onCancel: () -> Unit = {},
    crossinline onOk: () -> Unit = {}
): AlertDialog {
    return requireContext().alertDialog(message, title, themeId).ok(okText, onOk).cancel(cancelText, onCancel).show().centered().apply {
        setCanceledOnTouchOutside(false)
    }
}

inline fun Fragment.choose(
    items: Array<CharSequence>,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    crossinline onClick: (DialogInterface, Int) -> Unit
): AlertDialog {
    return requireContext().alertDialog(null, title, themeId).choose(items, onClick).show()
}
