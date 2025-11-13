package com.test.muzz.pages

import androidx.compose.ui.test.junit4.ComposeTestRule

/**
 * LoginPage - Page Object for Login Screen
 */
class LoginPage(composeTestRule: ComposeTestRule) : BasePage(composeTestRule) {

    init {
        logger.info("=== Navigated to: Login Page ===")
    }

    // ========== LOCATORS ==========

    private fun getUsernameField() = findByTag("username_field")
    private fun getPasswordField() = findByTag("password_field")
    private fun getLoginButton() = findByTag("login_button")
    private fun getErrorMessage() = findByTag("error_message")
    private fun getAppTitle() = findByTag("app_title")
    private fun getAppTagline() = findByTag("app_tagline")

    // ========== ACTIONS ==========

    fun enterUsername(username: String): LoginPage {
        logger.info("Entering username: '$username'")
        val usernameField = getUsernameField()
        clearText(usernameField)
        typeText(usernameField, username)
        return this
    }

    fun enterPassword(password: String): LoginPage {
        logger.info("Entering password: '${password.replace(Regex("."), "*")}'")
        val passwordField = getPasswordField()
        clearText(passwordField)
        typeText(passwordField, password)
        return this
    }

    fun clickLogin(): LoginPage {
        logger.info("Clicking login button")
        click(getLoginButton())
        waitForIdle()
        return this
    }

    fun login(username: String, password: String): Any {
        logger.info("Attempting login with username: '$username'")
        enterUsername(username)
        enterPassword(password)
        clickLogin()

        Thread.sleep(1500)

        return if (isLoginSuccessful()) {
            logger.info("Login successful - navigating to Profiles Page")
            ProfilesPage(composeTestRule)
        } else {
            logger.info("Login failed - remaining on Login Page")
            this
        }
    }

    fun attemptInvalidLogin(username: String, password: String): LoginPage {
        logger.info("Attempting invalid login")
        enterUsername(username)
        enterPassword(password)
        clickLogin()
        Thread.sleep(1000)
        return this
    }

    fun verifyLoginScreenDisplayed(): LoginPage {
        logger.info("Verifying login screen is displayed")
        assertIsDisplayed(getAppTitle())
        assertIsDisplayed(getAppTagline())
        assertIsDisplayed(getUsernameField())
        assertIsDisplayed(getPasswordField())
        assertIsDisplayed(getLoginButton())
        logger.info("✓ Login screen verified")
        return this
    }

    fun verifyUsernameFieldDisplayed(): LoginPage {
        logger.info("Verifying username field is displayed")
        assertIsDisplayed(getUsernameField())
        logger.info("✓ Username field verified")
        return this
    }

    fun verifyPasswordFieldDisplayed(): LoginPage {
        logger.info("Verifying password field is displayed")
        assertIsDisplayed(getPasswordField())
        logger.info("✓ Password field verified")
        return this
    }

    fun verifyLoginButtonDisplayed(): LoginPage {
        logger.info("Verifying login button is displayed")
        assertIsDisplayed(getLoginButton())
        logger.info("✓ Login button verified")
        return this
    }

    fun verifyErrorMarkersDisplayed(): LoginPage {
        logger.info("Verifying error markers are displayed")

        try {
            assertIsDisplayed(getErrorMessage())
            logger.info("✓ Error markers verified")
        } catch (e: Exception) {
            logger.error("✗ Error markers not found")
            throw AssertionError("Error markers not displayed after invalid login")
        }

        return this
    }

    private fun isLoginSuccessful(): Boolean {
        return try {
            waitForIdle()
            !isDisplayed(getLoginButton())
        } catch (e: Exception) {
            false
        }
    }
}
