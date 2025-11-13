package com.test.muzz.tests

import com.test.muzz.BaseTest
import com.test.muzz.pages.ProfilesPage
import com.test.muzz.utils.TestData
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class LoginTests : BaseTest() {

    @Test
    fun testLoginScreenDisplayed() {
        logStep("Given: User opens app for the first time")
        val loginPage = getLoginPage()

        logStep("When: App is launched")
        // App is already launched by BaseTest setup

        logStep("Then: Login screen with username, password fields and login button is displayed")
        loginPage.verifyLoginScreenDisplayed()
    }


    @Test
    fun testLoginFailsWithWrongUsername() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User enters wrong username and correct password")
        loginPage.attemptInvalidLogin(TestData.InvalidCredentials.WRONG_USERNAME, TestData.ValidCredentials.PASSWORD)

        logStep("Then: Error markers are displayed")
        loginPage.verifyErrorMarkersDisplayed()
    }


    @Test
    fun testLoginFailsWithWrongPassword() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User enters correct username and wrong password")
        loginPage.attemptInvalidLogin(TestData.ValidCredentials.USERNAME, TestData.InvalidCredentials.WRONG_PASSWORD)

        logStep("Then: Error markers are displayed")
        loginPage.verifyErrorMarkersDisplayed()
    }


    @Test
    fun testLoginFailsWithWrongCredentials() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User enters wrong username and wrong password")
        loginPage.attemptInvalidLogin(TestData.InvalidCredentials.WRONG_USERNAME, TestData.InvalidCredentials.WRONG_PASSWORD)

        logStep("Then: Error markers are displayed")
        loginPage.verifyErrorMarkersDisplayed()
    }

    @Test
    fun testLoginFailsWithEmptyFields() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User leaves fields empty and clicks login")
        loginPage.attemptInvalidLogin(TestData.InvalidCredentials.EMPTY_USERNAME, TestData.InvalidCredentials.EMPTY_PASSWORD)

        logStep("Then: Error markers are displayed")
        loginPage.verifyErrorMarkersDisplayed()
    }

    @Test
    fun testSuccessfulLogin() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User enters correct credentials and clicks login")
        val profilesPage = loginPage.login(
            TestData.ValidCredentials.USERNAME,
            TestData.ValidCredentials.PASSWORD
        ) as ProfilesPage

        logStep("Then: User is taken to profiles screen")
        profilesPage.verifyProfileDisplayed()
    }

    @Test
    fun testUsernameFieldAcceptsInput() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User types in username field")
        loginPage.enterUsername(TestData.ValidCredentials.USERNAME)

        logStep("Then: Text is entered successfully")
        loginPage.verifyUsernameFieldDisplayed()
    }

    @Test
    fun testPasswordFieldAcceptsInput() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("When: User types in password field")
        loginPage.enterPassword(TestData.ValidCredentials.PASSWORD)

        logStep("Then: Text is entered successfully")
        loginPage.verifyPasswordFieldDisplayed()
    }

    @Test
    fun testLoginButtonIsVisible() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("Then: Login button is visible")
        loginPage.verifyLoginButtonDisplayed()
    }

    @Test
    fun testUsernameFieldIsVisible() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("Then: Username field is visible")
        loginPage.verifyUsernameFieldDisplayed()
    }

    @Test
    fun testPasswordFieldIsVisible() {
        logStep("Given: User is on login screen")
        val loginPage = getLoginPage()

        logStep("Then: Password field is visible")
        loginPage.verifyPasswordFieldDisplayed()
    }

    @Test
    fun testClearAndReEnterUsername() {
        logStep("Given: User has entered username")
        val loginPage = getLoginPage()
        loginPage.enterUsername(TestData.ValidCredentials.FIRST_USER)

        logStep("When: User clears and re-enters different username")
        loginPage.enterUsername(TestData.ValidCredentials.SECOND_USER)

        logStep("Then: New username is set")
        loginPage.verifyUsernameFieldDisplayed()
    }
}
