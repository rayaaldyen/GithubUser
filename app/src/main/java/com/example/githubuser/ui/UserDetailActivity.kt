package com.example.githubuser.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionPagerAdapter
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.viewmodel.UserDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {
    private var _detailBinding: ActivityUserDetailBinding? = null
    private val detailBinding get() = _detailBinding!!
    private lateinit var detailViewModel: UserDetailViewModel
    private lateinit var scrollViewCon: ScrollView
    private var profileLink: String? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATARURL = "extra_avatarurl"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATARURL)
        setContentView(detailBinding.root)

        scrollViewCon = detailBinding.scrollView
        scrollViewCon.post { scrollViewCon.scrollTo(0, 0) }
        detailBinding.actionFavorite.bringToFront()

        val barAction = supportActionBar
        barAction!!.title = "User's Detail"

        detailViewModel = ViewModelProvider(
            this
        ).get(UserDetailViewModel::class.java)

        val sectionsPagerAdapter = SectionPagerAdapter(this@UserDetailActivity, username!!)
        val viewPager: ViewPager2 = detailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = detailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.error.observe(this) { isError ->
            if (isError) {
                errorMessage()
            }
        }

        detailViewModel.setDetailUser(username)

        detailViewModel.getDetailUser().observe(this) {
            if (it != null) {
                detailBinding.apply {
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .into(detailPhoto)
                    tvName.text = it.name
                    tvUsername.text = it.login
                    profileLink = it.htmlUrl
                    if (it.location != null) {
                        detailBinding.tvLoc.visibility = View.VISIBLE
                        tvLoc.text = it.location
                    }
                    if (it.company != null) {
                        detailBinding.tvCompany.visibility = View.VISIBLE
                        tvCompany.text = it.company
                    }
                    if (it.bio != null) {
                        detailBinding.tvBio.visibility = View.VISIBLE
                        tvBio.text = it.bio
                    }
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                }
            } else {
                errorMessage()
            }
        }
        detailBinding.btnView.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(profileLink)
            }.also {
                startActivity(it)
            }
        }

        val fabFavorite = detailBinding.actionFavorite
        var isCheckedFav = false
        CoroutineScope(Dispatchers.IO).launch {
            val countFav = detailViewModel.checkFav(id)
            withContext(Dispatchers.Main) {
                if (countFav != null) {
                    if (countFav > 0) {
                        fabFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                fabFavorite.context,
                                R.drawable.ic_baseline_favorite_24
                            )
                        )
                        isCheckedFav = true
                    } else {
                        fabFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                fabFavorite.context,
                                R.drawable.ic_favorite_border_24
                            )
                        )
                        isCheckedFav = false
                    }
                }
            }
        }

        fabFavorite.setOnClickListener {
            isCheckedFav = !isCheckedFav
            if (isCheckedFav) {
                if (avatarUrl != null) {
                    detailViewModel.insertFavorite(id, username, avatarUrl)
                    fabFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            fabFavorite.context,
                            R.drawable.ic_baseline_favorite_24
                        )
                    )
                    Toast.makeText(
                        this,
                        "${detailBinding.tvUsername.text} added to favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                detailViewModel.deleteFavorite(id)
                fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFavorite.context,
                        R.drawable.ic_favorite_border_24
                    )
                )
                Toast.makeText(
                    this,
                    "${detailBinding.tvUsername.text} deleted from favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onDestroy() {
        _detailBinding = null
        super.onDestroy()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar.visibility = View.GONE
        }
    }

    private fun errorMessage() {
        detailBinding.apply {
            tabs.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
            tvName.visibility = View.INVISIBLE
            tvUsername.visibility = View.INVISIBLE
            tvLoc.visibility = View.INVISIBLE
            tvCompany.visibility = View.INVISIBLE
            tvBio.visibility = View.INVISIBLE
        }
        Toast.makeText(this@UserDetailActivity, "There is an error", Toast.LENGTH_SHORT)
            .show()
    }


}