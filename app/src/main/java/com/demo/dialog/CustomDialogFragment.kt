package com.demo.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.demo.dialog.databinding.FragmentCustomDialogBinding
import me.reezy.cosmo.dialog.BottomDialog
import me.reezy.cosmo.dialog.DropdownDialog
import me.reezy.cosmo.dialog.SideDialog
import me.reezy.cosmo.dialog.TopDialog
import me.reezy.cosmo.dialog.showInQueue
import me.reezy.cosmo.dialog.simple.*


class CustomDialogFragment : Fragment(R.layout.fragment_custom_dialog) {


    private val binding by lazy { FragmentCustomDialogBinding.bind(requireView()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnLoading.setOnClickListener {
            LoadingDialog(requireContext()).show()
        }
        binding.btnLoadingCompact.setOnClickListener {
            LoadingDialog(requireContext(), iconResId = R.drawable.animated_dots_move, compact = true).show()
        }

        binding.btnActionsheet.setOnClickListener {
            ActionSheetDialog(requireContext(), arrayOf("One", "Two", "Three")) {
                toast("pos = $it")
            }.show()
        }
        binding.btnActionsheetIos.setOnClickListener {
            ActionSheetDialog(requireContext(), arrayOf("One", "Two", "Three"), themeId = me.reezy.cosmo.dialog.simple.R.style.Theme_TooSimple_ActionSheetIos) {
                toast("pos = $it")
            }.show()
        }
        binding.btnTopDialogGrid.setOnClickListener {
            TopDialog(it.context).setView(createGridView()).show()
        }
        binding.btnBottomDialogList.setOnClickListener {
            BottomDialog(it.context).setView(R.layout.layout_share).setButton(R.id.btn_session) {
                toast("btn_session clicked")
            }.setButton(R.id.btn_timeline) {
                toast("btn_timeline clicked")
            }.show()
        }
        binding.btnDropdownList.setOnClickListener {
            DropdownDialog(it.context, it).setView(R.layout.layout_share).setButton(R.id.btn_session) {
                toast("btn_session clicked")
            }.setButton(R.id.btn_timeline) {
                toast("btn_timeline clicked")
            }.show()
        }
        binding.btnDropdownGrid.setOnClickListener {
            DropdownDialog(it.context, it).setView(createGridView()).show()
        }
        binding.btnSideLeft.setOnClickListener {
            SideDialog(it.context).setView(R.layout.layout_side).show()
        }

        binding.btnSideRight.setOnClickListener {
            SideDialog(it.context, right = true).setView(R.layout.layout_side).show()
        }
        binding.btnPickerDialog.setOnClickListener {
            PickerDialog(it.context, arrayOf("One", "Two", "Three", "Four", "Five", "Six"), 2) { which, value ->
                toast("item[$which] = [$value]")
            }.show()
        }
        binding.btnInputDialog.setOnClickListener {
            BottomInputDialog(it.context, title = "请输入昵称", maxLength = 20) { dialog, value ->
                toast("input text => $value")
                dialog.dismiss()
            }.show()
        }

        binding.btnSimple1.setOnClickListener {
            MessageDialog(it.context, "您的VIP过期了，请续费").ack {
            }.show()
        }
        binding.btnSimple2.setOnClickListener {
            MessageDialog(it.context, title = "该吃药了").ack {
            }.show()
        }

        binding.btnSimple3.setOnClickListener {
            MessageDialog(it.context, "你现在是VIP了", closeButton = true).ack {
                toast("Hello")
            }.show()
        }

        binding.btnSimple4.setOnClickListener {
            MessageDialog(it.context, "确定要退出登录吗？").confirm {
                toast("退出登录成功")
            }.show()
        }
    }

//    private var tick = 1
//    override fun onResume() {
//        super.onResume()
//        Log.e("OoO", "tick = $tick")
//        MessageDialog(requireContext(), title = "该吃药了 ${tick++}").ack().showInQueue()
//    }

    fun createGridView(): View {
        val inflater = LayoutInflater.from(requireContext())
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
        val grid = GridView(requireContext())
        grid.setPadding(0, 80, 0, 80)
        grid.verticalSpacing = 80
        grid.gravity = Gravity.CENTER
        grid.adapter = adapter
        grid.numColumns = 4
        grid.background = ColorDrawable(-0x1)
        return grid
    }
}