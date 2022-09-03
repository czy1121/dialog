package com.demo.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.demo.dialog.databinding.ActivityDialogBinding
import me.reezy.cosmo.alert.*
import me.reezy.cosmo.dialog.BottomDialog
import me.reezy.cosmo.dialog.DropdownDialog
import me.reezy.cosmo.dialog.SideDialog
import me.reezy.cosmo.dialog.simple.ActionSheetDialog
import me.reezy.cosmo.dialog.simple.InputDialog
import me.reezy.cosmo.dialog.simple.LoadingDialog
import me.reezy.cosmo.dialog.simple.PickerDialog


class DialogActivity : AppCompatActivity() {
    private val THEMES = intArrayOf(
        me.reezy.cosmo.R.style.TooSimple_Alert,
        androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert,
        android.R.style.ThemeOverlay_Material_Dialog_Alert
    )

    private val binding by lazy { ActivityDialogBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)


        binding.spTitle.setSelection(1)
        binding.spContent.setSelection(1)
        binding.spButton.setSelection(2)


        binding.spTitle.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                logE("item = " + parent.selectedItem + ", position = " + position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                logE("nothing selected")
            }
        }

        binding.btnShow.setOnClickListener { show() }


        val tooSimpleAlert = me.reezy.cosmo.R.style.TooSimple_Alert
        binding.btnTip.setOnClickListener {
            tip("This is message", "This is tip", themeId = tooSimpleAlert).setIcon(R.mipmap.gold)
        }

        binding.btnAlert.setOnClickListener {
            alert("This is message", "This is alert", themeId = tooSimpleAlert)
        }

        binding.btnConfirm.setOnClickListener {
            confirm("This is message", "This is confirm", themeId = tooSimpleAlert)
        }

        binding.btnChoose.setOnClickListener {
            choose(arrayOf("One", "Two", "Three"), themeId = tooSimpleAlert) { _, which ->
                toast("item [$which] chosen")
            }
        }

        binding.btnLoading.setOnClickListener {
            LoadingDialog(this).show()
        }
        binding.btnLoadingCompact.setOnClickListener {
            LoadingDialog(this, compact = true).show()
        }

        binding.btnActionsheet.setOnClickListener {
            ActionSheetDialog(this, arrayOf("One", "Two", "Three")) {
                toast("pos = $it")
            }.show()
        }
        binding.btnActionsheetIos.setOnClickListener {
            ActionSheetDialog(this, arrayOf("One", "Two", "Three"), themeId = me.reezy.cosmo.dialog.simple.R.style.TooSimple_ActionSheetIos) {
                toast("pos = $it")
            }.show()
        }
        binding.btnBottomDialogList.setOnClickListener {
            BottomDialog(this).setView(R.layout.layout_share).setButton(R.id.btn_session) {
                toast("btn_session clicked")
            }.setButton(R.id.btn_timeline) {
                toast("btn_timeline clicked")
            }.show()
        }
        binding.btnBottomDialogGrid.setOnClickListener {
            BottomDialog(this).setView(createGridView()).show()
        }
        binding.btnDropdownList.setOnClickListener { view ->
            DropdownDialog(this, view).setView(R.layout.layout_share).setButton(R.id.btn_session) {
                toast("btn_session clicked")
            }.setButton(R.id.btn_timeline) {
                toast("btn_timeline clicked")
            }.show()
        }
        binding.btnDropdownGrid.setOnClickListener { view ->
            DropdownDialog(this, view).setView(createGridView()).show()
        }
        binding.btnSideLeft.setOnClickListener {
            SideDialog(this).setView(R.layout.layout_side).show()
        }

        binding.btnSideRight.setOnClickListener {
            SideDialog(this, right = true).setView(R.layout.layout_side).show()
        }
        binding.btnPicker.setOnClickListener {
            PickerDialog(this, arrayOf("One", "Two", "Three", "Four", "Five", "Six"), 2) { which, value ->
                toast("item[$which] = [$value]")
            }.show()
        }

        binding.btnInput.setOnClickListener {
            InputDialog(this, title = "请输入昵称", maxLength = 20) { dialog, value ->
                toast("input text => $value")
                dialog.dismiss()
            }.show()
        }

    }


    private fun show() {
        val themeIndex: Int = binding.spTheme.selectedItemPosition
        val titleIndex: Int = binding.spTitle.selectedItemPosition
        val contentIndex: Int = binding.spContent.selectedItemPosition
        val buttons: Int = binding.spButton.selectedItemPosition
        val showIcon: Boolean = binding.showIcon.isChecked

        val builder = alertDialog(themeId = THEMES[themeIndex])

        when (titleIndex) {
            1 -> builder.setTitle("不知名科技")
            2 -> builder.setTitle("为什么「道理都懂，然而执行力差」的现象如此普遍？")
        }

        if (showIcon) {
            builder.setIcon(R.mipmap.gold)
        }

        when (contentIndex) {
            1 -> builder.setMessage("你愿意 35 岁以后继续写代码吗？")
            2 -> builder.setMessage("对话框是提示用户作出决定或输入额外信息的小窗口。 对话框不会填充屏幕，通常用于需要用户采取行动才能继续执行的模式事件。")
            3 -> builder.setItems(R.array.items) { _, which ->
                logE("item [$which] clicked")
            }
            4 -> builder.setSingleChoiceItems(R.array.items, 2) { dialog, which ->

                logE("item [$which] clicked")
            }
            5 -> builder.setMultiChoiceItems(R.array.items, booleanArrayOf(false, true, true, false, false)) { _, which, isChecked ->
                logE("item [$which], checked = $isChecked")
            }
            6 -> builder.setView(TextView(this).apply {
                text = "custom view"
                gravity = Gravity.CENTER
            })
        }
        if (buttons >= 1) {
            builder.setPositiveButton("确认") { _, _ -> }
        }
        if (buttons >= 2) {
            builder.setNegativeButton("取消") { _, _ -> }
        }
        if (buttons >= 3) {
            builder.setNeutralButton("跳过") { _, _ -> }
        }
        builder.show()
    }

    fun createGridView(): View {
        val inflater = LayoutInflater.from(this)
        val adapter: ListAdapter = object : BaseAdapter() {
            val res = intArrayOf(R.mipmap.ic_a, R.mipmap.ic_b, R.mipmap.ic_c, R.mipmap.ic_d, R.mipmap.ic_b, R.mipmap.ic_c, R.mipmap.ic_d)
            val items = arrayOf("One", "Two", "Three", "Four", "Five", "Six", "Seven")

            override fun getCount(): Int = items.size

            override fun getItem(position: Int): Any = items

            override fun getItemId(position: Int): Long = 0

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val text: TextView
                if (convertView == null) {
                    text = inflater.inflate(android.R.layout.simple_gallery_item, parent, false) as TextView
                    text.minHeight = 200
                    text.minWidth = 200
                    text.gravity = Gravity.CENTER
                } else {
                    text = convertView as TextView
                }
                text.text = items[position]
                text.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(resources, res[position], null), null, null)
                return text
            }
        }
        val grid = GridView(this)
        grid.setPadding(0, 80, 0, 80)
        grid.verticalSpacing = 80
        grid.gravity = Gravity.CENTER
        grid.adapter = adapter
        grid.numColumns = 4
        grid.background = ColorDrawable(-0x1)
        return grid
    }
}
