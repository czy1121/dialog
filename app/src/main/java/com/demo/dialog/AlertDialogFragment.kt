package com.demo.dialog

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.demo.dialog.databinding.FragmentAlertDialogBinding
import me.reezy.cosmo.alert.*


class AlertDialogFragment : Fragment(R.layout.fragment_alert_dialog) {

    private val THEMES = intArrayOf(
        me.reezy.cosmo.R.style.Theme_TooSimple_AlertDialog,
        0,
        android.R.style.Theme_Material_Dialog_Alert,
        android.R.style.Theme_Material_Light_Dialog_Alert,
        androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert,
        androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert,
    )

    private val binding by lazy { FragmentAlertDialogBinding.bind(requireView()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spTitle.setSelection(1)
        binding.spContent.setSelection(1)
        binding.spButton.setSelection(2)


        binding.spTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                logE("item = " + parent.selectedItem + ", position = " + position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                logE("nothing selected")
            }
        }

        binding.btnShow.setOnClickListener { showAlert() }

        val tooSimpleAlert = THEMES[0]
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
        binding.btnInput.setOnClickListener {
            input("不能超过20个字", "请输入昵称", hint = "请输入昵称", themeId = THEMES[0]) { dialog, text ->
                toast("input text => $text")
                dialog.dismiss()
            }
        }
    }


    private fun showAlert() {
        val themeIndex: Int = binding.spTheme.selectedItemPosition
        val titleIndex: Int = binding.spTitle.selectedItemPosition
        val contentIndex: Int = binding.spContent.selectedItemPosition
        val buttons: Int = binding.spButton.selectedItemPosition
        val showIcon: Boolean = binding.showIcon.isChecked

        val builder = requireContext().alertDialog(themeId = THEMES[themeIndex])

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
            3 -> builder.setItems(R.array.items) { _, which -> logE("item [$which] clicked") }
            4 -> builder.setSingleChoiceItems(R.array.items, 2) { _, which -> logE("item [$which] clicked") }
            5 -> builder.setMultiChoiceItems(R.array.items, booleanArrayOf(false, true, true, false, false)) { _, which, isChecked ->
                logE("item [$which], checked = $isChecked")
            }
            6 -> builder.setView(TextView(requireContext()).apply {
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
}