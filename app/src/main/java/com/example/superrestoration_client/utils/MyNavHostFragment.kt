package com.example.superrestoration_client.utils

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.R

class MyNavHostFragment: NavHostFragment() {
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return MyFragmentNavigator(requireContext(), childFragmentManager, getContainerId())
    }

    private fun getContainerId(): Int {
        val id = id
        return if (id != 0 && id != View.NO_ID) {
            id
        } else R.id.nav_host_fragment_container
        // Fallback to using our own ID if this Fragment wasn't added via
        // add(containerViewId, Fragment)
    }
}