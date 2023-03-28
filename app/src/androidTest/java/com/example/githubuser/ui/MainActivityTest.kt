package com.example.githubuser.ui

import androidx.activity.R
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubuser.adapter.UserAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testComponentShowed(){
        onView(withId(com.example.githubuser.R.id.rv_users)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(com.example.githubuser.R.id.settings)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(com.example.githubuser.R.id.search)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(com.example.githubuser.R.id.favorite)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(com.example.githubuser.R.id.favorite)).perform(click())
        onView(withId(com.example.githubuser.R.id.rv_users_fav)).check(ViewAssertions.matches(isDisplayed()))

    }
}