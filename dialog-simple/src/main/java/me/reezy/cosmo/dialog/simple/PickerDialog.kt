package me.reezy.cosmo.dialog.simple

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.NumberPicker
import me.reezy.cosmo.dialog.simple.R
import me.reezy.cosmo.dialog.BottomDialog

class PickerDialog(context: Context, items: Array<String>, selection: Int = 0, onConfirm: (Int, String) -> Unit) : BottomDialog(context) {

    init {
        setView(R.layout.toosimple_picker)

        val picker = require<NumberPicker>(R.id.picker)


        picker.apply {
            minValue = 0
            maxValue = items.size - 1
            displayedValues = items
            wrapSelectorWheel = false
            value = selection
            setSelectionDividerHeight(this, (resources.displayMetrics.density * 0.9f).toInt())
        }

        setButton(R.id.btn_cancel) {
            dismiss()
        }

        setButton(R.id.btn_confirm) {
            onConfirm(picker.value, items[picker.value])
            dismiss()
        }

    }


    @SuppressLint("DiscouragedPrivateApi")
    private fun setSelectionDividerHeight(picker: NumberPicker, value: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker.selectionDividerHeight = value
        } else {
            try {
                val field = NumberPicker::class.java.getDeclaredField("mSelectionDividerHeight")
                field.isAccessible = true
                field.set(picker, value)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }
}