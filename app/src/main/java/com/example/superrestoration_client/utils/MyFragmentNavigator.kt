package com.example.superrestoration_client.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("fragment")
class MyFragmentNavigator(private val mContext: Context, private val mFragmentManager: FragmentManager, private val mContainerId: Int)
    : FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    val TAG = "MyFragmentNavigator"

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
//        val frag = instantiateFragment(
//            mContext, mFragmentManager,
//            className, args
//        )
//        frag.arguments = args
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        // ??????Fragment????????????
        val tag = destination.id.toString()
        var frag = mFragmentManager.findFragmentByTag(tag)
        if (mFragmentManager.fragments.size > 0) {
            for (fragment in mFragmentManager.fragments){
                Log.e(TAG, "${fragment.tag} == $tag?")
//                if (fragment.tag != tag)
                    ft.hide(fragment)
            }
//            ft.hide(mFragmentManager.fragments[mFragmentManager.fragments.size-1])
        }
        if (frag == null){
            frag = instantiateFragment(
                mContext, mFragmentManager,
                className, args
            )
            frag.arguments = args
            ft.add(mContainerId, frag, tag)
        }else{
            ft.show(frag)
        }
        ft.setPrimaryNavigationFragment(frag)
//        -----------------------------
        var mBackStack: java.util.ArrayDeque<Int>? = null
        try {
            val declaredField = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            declaredField.isAccessible = true
            mBackStack = declaredField.get(this) as java.util.ArrayDeque<Int>?
        }catch (e: NoSuchFieldException){
            e.printStackTrace()
        }catch (e: SecurityException){

        }catch (e: IllegalArgumentException){

        }catch (e: IllegalAccessException){

        }
//        -----------------------------------
        @IdRes val destId = destination.id
        val initialNavigation = mBackStack?.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation!!
                && navOptions.shouldLaunchSingleTop()
                && mBackStack!!.peekLast() == destId)

        val isAdded: Boolean = when {
            initialNavigation!! -> {
                true
            }
            isSingleTopReplacement -> {
                // Single Top means we only want one instance on the back stack
                if (mBackStack!!.size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
                    mFragmentManager.popBackStack(
                        generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
                }
                false
            }
            else -> {
                ft.addToBackStack(generateBackStackName(mBackStack!!.size + 1, destId))
                true
            }
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack!!.add(destId)
            destination
        } else {
            null
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}