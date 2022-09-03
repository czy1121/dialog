package me.reezy.cosmo.dialog.simple

import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import me.reezy.cosmo.dialog.simple.R
import me.reezy.cosmo.dialog.BottomDialog


class ActionSheetDialog(
    context: Context,
    items: Array<String>,
    themeId: Int = R.style.TooSimple_ActionSheet,
    cancelText: String = context.getString(R.string.toosimple_actionsheet_cancel),
    action: (which: Int) -> Unit = {}
) : BottomDialog(context, themeId) {

    init {
        setView(R.layout.toosimple_actionsheet)

        require<ListView>(android.R.id.list).apply {
            adapter = ArrayAdapter(context, R.layout.toosimple_actionsheet_item, android.R.id.text1, items)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                action(position)
                dismiss()
            }
        }

        require<TextView>(android.R.id.closeButton).apply {
            text = cancelText
            setOnClickListener { _ ->
                action(-1)
                dismiss()
            }
        }
    }
}