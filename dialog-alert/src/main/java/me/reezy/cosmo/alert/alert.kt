package me.reezy.cosmo.alert


import android.content.Context
import android.content.DialogInterface
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import me.reezy.cosmo.R


//===============================================
// Context -> tip/alert/confirm/choose
//===============================================

/** tip => 无按钮 */
fun Context.tip(message: CharSequence, title: CharSequence? = null, @StyleRes themeId: Int = 0): AlertDialog {
    return alertDialog(message, title, themeId).show().centered()
}

/** alert => 一个按钮(确定) */
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

/** confirm => 两个按钮(确定 & 取消) */
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

/** choose => 选择列表，无按钮 */
inline fun Context.choose(
    items: Array<CharSequence>,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    crossinline onClick: (DialogInterface, Int) -> Unit
): AlertDialog {
    return alertDialog(null, title, themeId).choose(items, onClick).show()
}

fun Context.input(
    message: CharSequence? = null,
    title: CharSequence? = null,
    editText: String? = null,
    hint: String? = null,
    maxLength: Int = 0,
    minLines: Int = 1,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    cancelText: CharSequence = getString(R.string.toosimple_alert_cancel),
    @StyleRes themeId: Int = 0,
    onCancel: () -> Unit = {},
    onConfirm: (DialogInterface, String) -> Unit
): AlertDialog {
    return alertDialog(message, title, themeId).setView(R.layout.toosimple_alert_input).ok().cancel(cancelText, onCancel).create().apply {

        setCanceledOnTouchOutside(false)

        val view by lazy { findViewById<EditText>(android.R.id.edit)?: throw Throwable("can't find EditText") }

        setButton(DialogInterface.BUTTON_POSITIVE, okText) { dialog, _ ->
            onConfirm(dialog, view.text.toString().trim())
        }

        setOnShowListener {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            ContextCompat.getSystemService(context, InputMethodManager::class.java)?.showSoftInput(view, 0)
        }

        setOnDismissListener {
            view.clearFocus()
            ContextCompat.getSystemService(context, InputMethodManager::class.java)?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        show()

        editText?.let {
            view.text.append(it)
        }
        view.hint = hint
        view.minLines = minLines
        if (maxLength > 0) {
            view.filters += InputFilter.LengthFilter(maxLength)
        }

        val space = (resources.displayMetrics.density * 20).toInt()
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            leftMargin = space
            rightMargin = space
            bottomMargin = space
        }
        if (themeId == R.style.Theme_TooSimple_AlertDialog) {
            findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER_HORIZONTAL
        }
    }
}



//===============================================
// Fragment -> tip/alert/confirm/choose
//===============================================

/** tip => 无按钮 */
fun Fragment.tip(message: CharSequence, title: CharSequence? = null, @StyleRes themeId: Int = 0): AlertDialog {
    return requireContext().alertDialog(message, title, themeId).show().centered()
}

/** alert => 一个按钮(确定) */
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

/** confirm => 两个按钮(确定 & 取消) */
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

/** choose => 选择列表，无按钮 */
inline fun Fragment.choose(
    items: Array<CharSequence>,
    title: CharSequence? = null,
    @StyleRes themeId: Int = 0,
    crossinline onClick: (DialogInterface, Int) -> Unit
): AlertDialog {
    return requireContext().alertDialog(null, title, themeId).choose(items, onClick).show()
}

inline fun Fragment.input(
    message: CharSequence? = null,
    title: CharSequence? = null,
    editText: String? = null,
    hint: String? = null,
    maxLength: Int = 0,
    minLines: Int = 1,
    okText: CharSequence = getString(R.string.toosimple_alert_ok),
    cancelText: CharSequence = getString(R.string.toosimple_alert_cancel),
    @StyleRes themeId: Int = 0,
    noinline onCancel: () -> Unit = {},
    noinline onConfirm: (DialogInterface, String) -> Unit
): AlertDialog {
    return requireActivity().input(message, title, editText, hint, maxLength, minLines, okText, cancelText, themeId, onCancel, onConfirm)
}