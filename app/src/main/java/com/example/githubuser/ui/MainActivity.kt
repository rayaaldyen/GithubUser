package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.DisplayUser
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.preferences.SettingPreferences
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_AVATARURL
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_ID
import com.example.githubuser.ui.UserDetailActivity.Companion.EXTRA_USERNAME
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodelfactory.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private var _mainBinding: ActivityMainBinding? = null
    private val mainBinding get() = _mainBinding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val pref = SettingPreferences.getInstance(dataStore)
        val barAction = supportActionBar
        barAction!!.title = "Github Users"
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        ).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.displayUser.observe(this) {
            showSearchResult(it)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.error.observe(this) { isError ->
            if (isError) {
                errorMessage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.findUser(newText)
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                Intent(this@MainActivity,FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.settings -> {
                Intent(this@MainActivity,SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        _mainBinding = null
        super.onDestroy()
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            mainBinding.progressBar.visibility = View.VISIBLE
            mainBinding.progressBar.bringToFront()
        } else {
            mainBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showSearchResult(users: ArrayList<DisplayUser>) {
        if (users.size > 0) {
            mainBinding.tvEmpty.visibility = View.INVISIBLE
            mainBinding.rvUsers.visibility = View.VISIBLE
            userAdapter = UserAdapter(users)
            mainBinding.rvUsers.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = userAdapter
                setHasFixedSize(true)

            }
            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: DisplayUser) {
                    Intent(this@MainActivity, UserDetailActivity::class.java).apply {
                        putExtra(EXTRA_USERNAME, user.login)
                        putExtra(EXTRA_ID, user.id)
                        putExtra(EXTRA_AVATARURL, user.avatarUrl)

                    }.also {
                        startActivity(it)
                    }
                }

            })
        } else {
            mainBinding.tvEmpty.visibility = View.VISIBLE
            mainBinding.rvUsers.visibility = View.INVISIBLE
        }
    }

    private fun errorMessage() {
        Toast.makeText(
            this@MainActivity,
            "There is an error while showing the data",
            Toast.LENGTH_SHORT
        )
            .show()
    }


}