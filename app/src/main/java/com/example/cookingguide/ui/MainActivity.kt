package com.example.cookingguide.ui

import android.os.Handler
import com.example.cookingguide.R
import com.example.cookingguide.R.id.FragmentLayout
import com.example.cookingguide.base.BaseActivity
import com.example.cookingguide.databinding.ActivityMainBinding
import com.example.cookingguide.ui.favourite.FragmentFavourite
import com.example.cookingguide.ui.home.FragmentHome
import com.example.cookingguide.ui.filter.FragmentFilter
import com.example.cookingguide.ui.lookup.FragmentLookUp
import com.example.cookingguide.ui.send.FragmentSend
import com.example.cookingguide.utils.Common
import com.example.cookingguide.utils.Common.addFragment
import com.example.cookingguide.utils.Common.category
import com.example.cookingguide.utils.Common.categoryScreen
import com.example.cookingguide.utils.Common.replaceFragment

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    private var isOnBackPress = 0

    override fun initAction() {
        addFragment(this@MainActivity, FragmentLayout, FragmentHome.newInstance())

        binding.NavigationHome.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_home -> {
                    val fragment = supportFragmentManager.findFragmentById(FragmentLayout)
                    if (fragment !is FragmentHome) {
                        replaceFragment(
                            this@MainActivity,
                            FragmentLayout,
                            FragmentHome.newInstance()
                        )
                    }
                    true
                }

                R.id.menu_item_favorite -> {
                    val fragment = supportFragmentManager.findFragmentById(FragmentLayout)
                    if (fragment !is FragmentFavourite) {
                        replaceFragment(
                            this@MainActivity,
                            FragmentLayout,
                            FragmentFavourite.newInstance()
                        )
                    }
                    true
                }

                else -> {
                    val fragment = supportFragmentManager.findFragmentById(FragmentLayout)
                    if (fragment !is FragmentSend) {
                        replaceFragment(
                            this@MainActivity,
                            FragmentLayout,
                            FragmentSend.newInstance()
                        )
                    }
                    true
                }
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(FragmentLayout)
        if (fragment is FragmentHome) {
            isOnBackPress++
            Common.createCustomToast(
                this@MainActivity,
                "Nhấn quay lại một lần nữa để thoát ứng dụng",
                layoutInflater
            )
            Handler().postDelayed({
                isOnBackPress = 0
            }, 3000)
            if (isOnBackPress == 2) {
                finish()
            }
        } else if (fragment is FragmentFilter || fragment is FragmentSend || fragment is FragmentFavourite) {
            binding.NavigationHome.menu.findItem(R.id.menu_item_home).isChecked = true
            replaceFragment(this@MainActivity, FragmentLayout, FragmentHome.newInstance())
        } else if (fragment is FragmentLookUp) {
            when (categoryScreen) {
                "send" -> {
                    replaceFragment(this@MainActivity, FragmentLayout, FragmentSend.newInstance())
                }

                "favourite" -> {
                    replaceFragment(this@MainActivity, FragmentLayout, FragmentFavourite.newInstance())
                }

                else -> {
                    binding.NavigationHome.menu.findItem(R.id.menu_item_home).isChecked = true
                    replaceFragment(
                        this@MainActivity,
                        FragmentLayout,
                        FragmentFilter.dataInstance(category.toString())
                    )
                }
            }
        }
    }
}