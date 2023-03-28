package com.example.githubuser.adapter

import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.FollowersFragment
import com.example.githubuser.ui.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val username : String) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
         when (position) {
            0 -> {
                fragment = FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_USERNAME, username)
                    }
                }

            }
            1 -> {
                fragment = FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_USERNAME, username)
                    }
                }
            }
        }
        return fragment as Fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}