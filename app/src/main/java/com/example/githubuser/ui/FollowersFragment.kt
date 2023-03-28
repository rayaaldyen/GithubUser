package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.DisplayUser
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.databinding.FragmentFollowBinding

import com.example.githubuser.viewmodel.FollowersViewModel

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _followersBinding: FragmentFollowBinding? = null
    private val followersBinding get() = _followersBinding!!
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        _followersBinding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        followersViewModel.getFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                showFollowers(it)
            } else {
                val username = arguments?.getString(ARG_USERNAME) ?: ""
                followersViewModel.setFollowers(username)
            }
        }
        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        return followersBinding.root
    }

    override fun onDestroy() {
        _followersBinding = null
        super.onDestroy()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            followersBinding.progressBar.visibility = View.VISIBLE
        } else {
            followersBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showFollowers(users: ArrayList<DisplayUser>) {
        if (users.size > 0) {
            followersBinding.tvAvailability.visibility = View.INVISIBLE
            followersBinding.rvFollow.visibility = View.VISIBLE
            val userAdapter = UserAdapter(users)
            followersBinding.rvFollow.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = userAdapter
                setHasFixedSize(true)
                userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: DisplayUser) {
                        Intent(activity, UserDetailActivity::class.java).apply {
                            putExtra(UserDetailActivity.EXTRA_USERNAME, user.login)

                        }.also {
                            startActivity(it)
                        }
                    }

                })
            }
        } else {
            followersBinding.tvAvailability.visibility = View.VISIBLE
            followersBinding.rvFollow.visibility = View.INVISIBLE
        }
    }


}