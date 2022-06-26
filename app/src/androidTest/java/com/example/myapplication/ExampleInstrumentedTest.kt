package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var uiDevice: UiDevice
    private lateinit var context: Context

    @Before
    fun setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context = InstrumentationRegistry.getInstrumentation().targetContext
        uiDevice.pressHome()
        //adb shell input keyevent KEYCODE_APP_SWITCH && input swipe 522 1647 522 90
//        uiDevice.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)
//        uiDevice.swipe(522, 1467, 522, 90, 10)
//        uiDevice.pressHome()
        uiDevice.executeShellCommand("am force-stop com.google.android.youtube")
    }

    @Test
    fun watchAds() {
        // Open youtube app
        val componentName = ComponentName("com.google.android.youtube", "com.google.android.apps.youtube.app.WatchWhileActivity")
        val intent = Intent()
        intent.component = componentName
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        uiDevice.waitForIdle()
        // Search video by name
        val searchButton = uiDevice.findObject(UiSelector().resourceId("com.google.android.youtube:id/menu_item_0"))
        searchButton.clickAndWaitForNewWindow();
        val searchBox = uiDevice.findObject(UiSelector().resourceId("com.google.android.youtube:id/search_edit_text"))
        val videoTitle = "BLACKPINK - 'How You Like That' M/V"
        searchBox.text = videoTitle
        uiDevice.pressEnter();
        uiDevice.waitForIdle();

        val recyclerView = uiDevice.findObject(UiSelector().resourceId("com.google.android.youtube:id/results"))
        val videoView = recyclerView.getChild(UiSelector().descriptionContains(videoTitle))
        videoView.clickAndWaitForNewWindow()
        uiDevice.waitForIdle()

        val adView = getAdView()
        adView.clickAndWaitForNewWindow()
    }

    private fun getAdView() : UiObject {
        // Shopee / Lazada
        var view = uiDevice.findObject(UiSelector().description("INSTALL"))
        if (!view.exists()) {
            view = uiDevice.findObject(UiSelector().description("MUA NGAY"))
        }
        return view
    }
}