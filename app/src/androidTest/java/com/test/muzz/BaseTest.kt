package com.test.muzz

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.muzz.pages.LoginPage
import com.test.muzz.utils.TestLogger
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    // Hilt rule for dependency injection
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Compose test rule for UI testing
    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    // Test name
    @get:Rule(order = 2)
    val testName = TestName()

    // Utilities
    protected val logger = TestLogger()


    @Before
    fun setUp() {
        hiltRule.inject()
        // Get test name from current test method
        logger.testStart(testName.methodName)
    }

    @After
    open fun tearDown() {
        try {
            logger.testPassed(testName.methodName)
        } catch (e: Exception) {
            // Test failed - capture screenshot
            logger.testFailed(testName.methodName, e.message ?: "Unknown error")
            throw e
        }
    }

    protected fun getLoginPage(): LoginPage {
        return LoginPage(composeTestRule)
    }


    private fun getTestMethodName(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            val testMethod = stackTrace.firstOrNull {
                it.className.contains("Test") && it.methodName.startsWith("given")
            }
            testMethod?.methodName ?: "UnknownTest"
        } catch (e: Exception) {
            "UnknownTest"
        }
    }

    protected fun logStep(step: String) {
        logger.step(step)
    }

    protected fun waitForIdle() {
        composeTestRule.waitForIdle()
    }
}
