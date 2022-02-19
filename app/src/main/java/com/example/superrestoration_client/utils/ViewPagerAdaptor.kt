package com.example.superrestoration_client.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdaptor(list: ArrayList<Fragment>, private val fragmentManager: FragmentManager, private val lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments: ArrayList<Fragment> = list

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}