package me.reezy.cosmo.dialog.simple

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import me.reezy.cosmo.dialog.simple.R
import me.reezy.cosmo.dialog.BottomDialog

class InputDialog(
    context: Context,
    title: String = "",
    hint: String = "",
    text: CharSequence = "",
    maxLength: Int = 0,
    minLines: Int = 1,
    onConfirm: (DialogInterface, String) -> Unit
) : BottomDialog(context) {

    private val vEdit by lazy { require<EditText>(android.R.id.edit) }
    private val vConfirm by lazy { require<TextView>(R.id.btn_confirm) }
    private val vCount by lazy { require<TextView>(R.id.txt_count) }
    private val vTitle by lazy { require<TextView>(android.R.id.title) }

    init {
        setDimAmount(0.6f)
        setView(R.layout.toosimple_input)

        vTitle.visibility = if (title.isEmpty()) View.GONE else View.VISIBLE
        vTitle.text = title

        vEdit.isFocusable = true
        vEdit.isFocusableInTouchMode = true
        vEdit.minLines = minLines
        if (maxLength > 0) {
            vEdit.filters += InputFilter.LengthFilter(maxLength)
            vCount.visibility = View.VISIBLE
        } else {
            vCount.visibility = View.GONE
        }
        vEdit.hint = hint
        vEdit.text.clear()
        vEdit.text.append(text)

        vEdit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val content = text?.trim()?.toString() ?: ""
                vConfirm.isEnabled = content.isNotEmpty()
                vConfirm.alpha = if (content.isNotEmpty()) 1f else 0.5f
                if (maxLength > 0) {
                    vCount.text = "${content.length}/$maxLength"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        vConfirm.setOnClickListener {
            onConfirm(this, vEdit.text.toString().trim())
        }

        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        setOnDismissListener {
            vEdit.clearFocus()
            ContextCompat.getSystemService(context, InputMethodManager::class.java)?.hideSoftInputFromWindow(vEdit.windowToken, 0)
        }

        setOnShowListener {
            vEdit.requestFocus()
            ContextCompat.getSystemService(context, InputMethodManager::class.java)?.showSoftInput(vEdit, 0)
        }

    }

    fun setHint(value: String): InputDialog {
        vEdit.hint = value
        return this
    }

    fun setText(value: CharSequence): InputDialog {
        vEdit.text.clear()
        vEdit.text.append(value)
        return this
    }
}