package com.ndt.themoviedb.ui.navigation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ndt.themoviedb.R
import com.ndt.themoviedb.databinding.FragmentContainerBinding
import com.ndt.themoviedb.databinding.ToolbarBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.favorite.FavoriteFragment
import com.ndt.themoviedb.ui.home.HomeFragment
import com.ndt.themoviedb.ui.mainscreen.MainActivity
import com.ndt.themoviedb.ui.search.SearchFragment
import com.ndt.themoviedb.utils.showSnackBar

class ContainerFragment :
    BaseFragment<FragmentContainerBinding>(FragmentContainerBinding::inflate) {
    private lateinit var binding: ToolbarBinding
    private var startingPosition = 0
    private var changeMenu = true

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun initAdapter() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment(HomeFragment(), startingPosition)
        setHasOptionsMenu(true)
        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener(selectedListener)
        initToolBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (changeMenu) {
            inflater.inflate(R.menu.search_view_home, menu)
        } else {
            activity?.run {
                inflater.inflate(R.menu.search_view_favorite, menu)
                val manager = supportFragmentManager
                val fragment =
                    manager.findFragmentById(R.id.container_frame_layout)
                if (fragment is FavoriteFragment) {
                    val searchView =
                        menu.findItem(R.id.ic_searchView_favorite).actionView as SearchView
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextChange(newText: String): Boolean {
                            fragment.favoriteAdapter?.search(newText) {
                            }
                            return true
                        }

                        override fun onQueryTextSubmit(query: String): Boolean {
                            fragment.favoriteAdapter?.search(query) {
                                activity?.showSnackBar(R.string.nothing_found)
                            }
                            return true
                        }
                    })
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ic_searchView_home) {
            activity?.run {
                supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.right_to_left,
                    R.anim.exit_right_to_left,
                    R.anim.left_to_right,
                    R.anim.exit_left_to_right
                ).replace(R.id.main_frame_layout, SearchFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        return false
    }

    private fun initToolBar() {
        binding = ToolbarBinding.inflate(layoutInflater)
        binding.toolbar.let {
            (activity as? MainActivity)?.run { setSupportActionBar(it) }
        }
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

    private val selectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment(), 1)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.favorite -> {
                    loadFragment(FavoriteFragment(), 2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
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

