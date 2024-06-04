package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.ListFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.databinding.ActivityMainBinding
import kr.cosine.groupfinder.presentation.view.list.FreeFragment
import kr.cosine.groupfinder.presentation.view.list.GroupFragment
import kr.cosine.groupfinder.presentation.view.list.NormalFragment
import kr.cosine.groupfinder.presentation.view.list.SoloFragment
import kr.cosine.groupfinder.presentation.view.list.WindFragment
import kr.cosine.groupfinder.presentation.view.profile.ProfileFragment
import kr.cosine.groupfinder.presentation.view.test.model.PostViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var firestore: FirebaseFirestore

    private val postViewModel by viewModels<PostViewModel>()

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firestore = Firebase.firestore
        firestore.collection("posts").addSnapshotListener { it, _ ->
            val querySnapshot = it ?: return@addSnapshotListener
            if (querySnapshot.metadata.isFromCache) return@addSnapshotListener
            querySnapshot.documentChanges.forEach { documentChange ->
                if (documentChange.type == DocumentChange.Type.ADDED) {
                    val postResponse = documentChange.document.toObject(PostResponse::class.java)
                    Log.d("GroupFinderTest", "[ADDED] PostModel : $postResponse")
                }
            }
        }
        registerView()
        navigationSetting(savedInstanceState)
    }

    private fun registerView() {
        binding.sendButton.setOnClickListener {
            postViewModel.createPost(listOf("태그1", "태그2", "태그4"))
            Log.d("GroupFinderTest2", "눌렀음")
            postViewModel.getPosts() { list ->
                list.forEach {
                    Log.d("GroupFinderTest2", it.toString())
                }
            }
        }
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
                .replace(R.id.fragment_container, GroupFragment()).commit()
            navigationView.setCheckedItem(R.id.navigation_All)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_All -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GroupFragment()).commit()
            }
            R.id.navigation_Normal -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NormalFragment()).commit()
            }
            R.id.navigation_Wind -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, WindFragment()).commit()
            }
            R.id.navigation_Solo -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SoloFragment()).commit()
            }
            R.id.navigation_Free -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FreeFragment()).commit()
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