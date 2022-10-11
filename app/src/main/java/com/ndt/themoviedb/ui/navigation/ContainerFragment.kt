package com.ndt.themoviedb.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ndt.themoviedb.R
import com.ndt.themoviedb.databinding.FragmentContainerBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.home.HomeFragment

class ContainerFragment :
    BaseFragment<FragmentContainerBinding>(FragmentContainerBinding::inflate) {
    private var startingPosition = 0

    override fun initData() {
        //TODO implement later
    }

    override fun initAdapter() {
        //TODO implement later
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment(HomeFragment(), startingPosition)
    }

    private fun loadFragment(fragment: Fragment, position: Int): Boolean {
        activity?.run {
            when {
                startingPosition > position -> {
                    slideLeftToRight(fragment)
                }
                startingPosition < position -> {
                    slideRightToLeft(fragment)
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_frame_layout, fragment)
                        .commit()
                }
            }
        }
        startingPosition = position
        return true
    }
}

private fun FragmentActivity.slideRightToLeft(fragment: Fragment) {
    supportFragmentManager.beginTransaction().setCustomAnimations(
        R.anim.right_to_left,
        R.anim.exit_right_to_left,
        R.anim.left_to_right,
        R.anim.exit_left_to_right
    )
        .replace(R.id.container_frame_layout, fragment)
        .commit()
}

private fun FragmentActivity.slideLeftToRight(fragment: Fragment) {
    supportFragmentManager.beginTransaction().setCustomAnimations(
        R.anim.left_to_right,
        R.anim.exit_left_to_right,
        R.anim.right_to_left,
        R.anim.exit_right_to_left
    )
        .replace(R.id.container_frame_layout, fragment)
        .commit()
}
