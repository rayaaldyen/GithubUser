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
import com.example.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _followingBinding: FragmentFollowBinding? = null
    private val followingBinding get() = _followingBinding!!
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        _followingBinding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        followingViewModel.getFollowing().observe(viewLifecycleOwner) {
            if (it != null) {
                showFollowing(it)
            } else {
                val username = arguments?.getString(ARG_USERNAME) ?: ""
                followingViewModel.setFollowing(username)
            }
        }
        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        return followingBinding.root
    }

    override fun onDestroy() {
        _followingBinding = null
        super.onDestroy()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            followingBinding.progressBar.visibility = View.VISIBLE
        } else {
            followingBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showFollowing(users: ArrayList<DisplayUser>) {
        if (users.size > 0) {
            followingBinding.tvAvailability.visibility = View.INVISIBLE
            followingBinding.rvFollow.visibility = View.VISIBLE
            val userAdapter = UserAdapter(users)
            followingBinding.rvFollow.apply {
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
            followingBinding.tvAvailability.visibility = View.VISIBLE
            followingBinding.rvFollow.visibility = View.INVISIBLE
        }

    }


}