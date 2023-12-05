package com.demo.dialog

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.dialog.databinding.ActivityDialogBinding
import com.google.android.material.tabs.TabLayoutMediator


class DialogActivity : AppCompatActivity() {


    private val tabItems = listOf(
        "Alert Dialog",
        "Custom Dialog",
    )

    private val binding by lazy { ActivityDialogBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)




        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> AlertDialogFragment()
                else -> CustomDialogFragment()
            }
        }

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = tabItems[position]
        }.attach()


    }


}
