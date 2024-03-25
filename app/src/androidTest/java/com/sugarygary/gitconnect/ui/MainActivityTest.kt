package com.sugarygary.gitconnect.ui

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sugarygary.gitconnect.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val usernameAsal = "IQWYGHDIQHWDIUNIUSNIUCFNSIAUDNI123"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertNotFound() {
        //mengecek apakah message not found ditampilkan
        onView(withId(R.id.sv_user)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(
            typeText(usernameAsal)
        )
        onView(withId(R.id.sv_user)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        Thread.sleep(1000L)
        onView(withId(R.id.layoutEmpty)).check(matches(isDisplayed()))
    }
}
