package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityMainBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.common.GroupFinderActivity
import kr.cosine.groupfinder.presentation.view.dialog.Dialog
import kr.cosine.groupfinder.presentation.view.group.GroupFragment
import kr.cosine.groupfinder.presentation.view.profile.ProfileFragment

@AndroidEntryPoint
class MainActivity : GroupFinderActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerNavigationDrawer()
        registerBackPressedCallback()
    }

    private fun registerNavigationDrawer() {
        drawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        val navigationView = binding.navigationView

        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_navigation,
            R.string.close_navigation
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(GroupFragment())
        navigationView.setCheckedItem(R.id.all)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.all -> GroupFragment()

            R.id.normal -> GroupFragment(Mode.NORMAL)

            R.id.aram -> GroupFragment(Mode.ARAM)

            R.id.duo_rank -> GroupFragment(Mode.DUO_RANK)

            R.id.flex_rank -> GroupFragment(Mode.FLEX_RANK)

            R.id.profile -> ProfileFragment()

            else -> null
        } ?: return false
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        binding.toolbar.title = when (fragment) {
            is GroupFragment -> fragment.mode?.displayName ?: getString(R.string.group_all_category)
            is ProfileFragment -> getString(R.string.group_profile_category)
            else -> getString(R.string.group_unknown_category)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun registerBackPressedCallback() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val fragments = supportFragmentManager.fragments
                    val hasGroupFragment = fragments.any { it is GroupFragment && it.mode == null }
                    if (fragments.isEmpty() || hasGroupFragment) {
                        showExitDialog()
                    } else {
                        replaceFragment(GroupFragment())
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun showExitDialog() {
        Dialog(
            message = getString(R.string.group_exit_message),
        ) {
            finish()
        }.show(supportFragmentManager, Dialog.TAG)
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fragment_container)
    }

    fun showForceExitDialog() {
        Dialog(
            title = "강제 퇴장",
            message = "강제 퇴장되었습니다.",
            cancelButtonVisibility = View.GONE,
            onConfirmClick = {
                val currentFragment = getCurrentFragment()
                if (currentFragment is GroupFragment) {
                    currentFragment.doRefresh()
                }
            }
        ).show(supportFragmentManager, Dialog.TAG)
    }
}