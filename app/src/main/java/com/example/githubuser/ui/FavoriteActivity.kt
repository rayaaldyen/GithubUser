package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.DisplayUser
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.entity.FavoriteUser
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.viewmodel.FavoriteViewModel


class FavoriteActivity : AppCompatActivity() {
    private var _favoriteBinding: ActivityFavoriteBinding? = null
    private val favoriteBinding get() = _favoriteBinding!!
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var userAdapter: UserAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        val barAction = supportActionBar
        barAction!!.title = "Favorite User"
        favoriteViewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)

        favoriteViewModel.getFavoriteUser.observe(this) { users: List<FavoriteUser> ->
                val items = ArrayList<DisplayUser>()
                users.map {
                    val item = DisplayUser(id = it.id, login = it.username, avatarUrl = it.avatarUrl)
                    items.add(item)
                }
            showFavUser(items)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> {
                Intent(this@FavoriteActivity,SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        _favoriteBinding = null
        super.onDestroy()
    }
    private fun showFavUser(users: ArrayList<DisplayUser>) {
        if (users.size > 0) {
            favoriteBinding.tvEmpty.visibility = View.INVISIBLE
            favoriteBinding.rvUsersFav.visibility = View.VISIBLE
           userAdapter = UserAdapter(users)
            userAdapter.notifyDataSetChanged()
            favoriteBinding.rvUsersFav.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = userAdapter
                setHasFixedSize(true)

            }
            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: DisplayUser) {
                    Intent(this@FavoriteActivity, UserDetailActivity::class.java).apply {
                        putExtra(UserDetailActivity.EXTRA_USERNAME, user.login)
                        putExtra(UserDetailActivity.EXTRA_ID, user.id)
                        putExtra(UserDetailActivity.EXTRA_AVATARURL, user.avatarUrl)

                    }.also {
                        startActivity(it)
                    }
                }

            })
        } else {
            favoriteBinding.tvEmpty.visibility = View.VISIBLE
            favoriteBinding.rvUsersFav.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        favoriteViewModel.getFavoriteUser.observe(this) { users: List<FavoriteUser> ->
            val items = ArrayList<DisplayUser>()
            users.map {
                val item = DisplayUser(id = it.id, login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            showFavUser(items)
        }
        super.onResume()
    }

}