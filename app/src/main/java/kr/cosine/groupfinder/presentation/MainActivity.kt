package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navigationSetting(savedInstanceState)
    }

    private fun navigationSetting(savedInstanceState: Bundle?) {
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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroupFragment(Mode.NORMAL)).commit()
            navigationView.setCheckedItem(R.id.navigation_All)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_All -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment(null)).commit()
            }
            R.id.navigation_Normal -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment(Mode.NORMAL)).commit()
            }
            R.id.navigation_Aram -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment(Mode.ARAM)).commit()
            }
            R.id.navigation_Duo -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment(Mode.DUO_RANK)).commit()
            }
            R.id.navigation_Flex -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment(Mode.FLEX_RANK)).commit()
            }
            R.id.navigation_myProfile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}