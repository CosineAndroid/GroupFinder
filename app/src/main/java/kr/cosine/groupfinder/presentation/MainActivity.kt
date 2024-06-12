package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityMainBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.GroupFragment
import kr.cosine.groupfinder.presentation.view.profile.ProfileFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        navigationView.setCheckedItem(R.id.navigationAll)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.navigationAll -> GroupFragment()

            R.id.navigationNormal -> GroupFragment(Mode.NORMAL)

            R.id.navigationAram -> GroupFragment(Mode.ARAM)

            R.id.navigationDuoRank -> GroupFragment(Mode.DUO_RANK)

            R.id.navigationFlexRank -> GroupFragment(Mode.FLEX_RANK)

            R.id.navigationProfile -> ProfileFragment()

            else -> null
        } ?: return false
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        binding.toolbar.title = when (fragment) {
            is GroupFragment -> fragment.mode?.displayName ?: ALL_CATEGORY
            is ProfileFragment -> PROFILE_CATEGORY
            else -> EMPTY_CATEGORY
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
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private companion object {
        const val ALL_CATEGORY = "전체"
        const val PROFILE_CATEGORY = "프로필"
        const val EMPTY_CATEGORY = " "
    }
}