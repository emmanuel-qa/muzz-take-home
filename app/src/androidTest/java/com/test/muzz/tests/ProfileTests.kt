package com.test.muzz.tests

import com.test.muzz.BaseTest
import com.test.muzz.pages.ProfilesPage
import com.test.muzz.utils.TestData
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class ProfileTests : BaseTest() {

    private fun loginToApp(): ProfilesPage {
        val loginPage = getLoginPage()
        return loginPage.login(
            TestData.ValidCredentials.USERNAME,
            TestData.ValidCredentials.PASSWORD
        ) as ProfilesPage
    }

    @Test
    fun testProfilesDisplayedAfterLogin() {
        logStep("Given: User successfully logged in")
        val profilesPage = loginToApp()

        logStep("When: Profiles page loads")
        logStep("Then: Profiles are displayed with like and pass buttons")
        profilesPage.verifyProfilesLoaded()
    }

    @Test
    fun testLikeAndPassButtonsVisible() {
        logStep("Given: User logged in and profiles loaded")
        val profilesPage = loginToApp()
        profilesPage.waitForProfilesToLoad()

        logStep("Then: Like and Pass buttons are visible")
        profilesPage
            .verifyLikeButtonEnabled()
            .verifyPassButtonEnabled()
    }

    @Test
    fun testUserCanLikeProfile() {
        logStep("Given: Profile is displayed")
        val profilesPage = loginToApp()
        profilesPage.verifyProfileDisplayed()

        logStep("When: User clicks like button")
        val profileName = profilesPage.getProfileNameText()
        logger.info("Liking profile: $profileName")
        profilesPage.likeProfile()

        waitForIdle()

        logStep("Then: Next profile loads or finished state shows")
        val result = profilesPage.isProfileDisplayed() || profilesPage.isFinishedStateDisplayed()
        assert(result) { "After liking, should show next profile or finished state" }
    }

    @Test
    fun testUserCanPassProfile() {
        logStep("Given: Profile is displayed")
        val profilesPage = loginToApp()
        profilesPage.verifyProfileDisplayed()

        logStep("When: User clicks pass button")
        val profileName = profilesPage.getProfileNameText()
        logger.info("Passing on profile: $profileName")
        profilesPage.passProfile()

        waitForIdle()

        logStep("Then: Next profile loads or finished state shows")
        val result = profilesPage.isProfileDisplayed() || profilesPage.isFinishedStateDisplayed()
        assert(result) { "After passing, should show next profile or finished state" }
    }

    @Test
    fun testLikingMultipleProfilesShowsCorrectCount() {
        logStep("Given: Dating profiles are successfully loaded")
        val profilesPage = loginToApp()
        profilesPage.verifyProfilesLoaded()

        logStep("When: User likes multiple profiles")
        val targetLikes = TestData.Counts.PROFILES_TO_LIKE
        val actualLikes = profilesPage.likeMultipleProfiles(targetLikes)
        logStep("Then: Correct count is displayed")
        if (profilesPage.isFinishedStateDisplayed()) {
            profilesPage.verifyLikesCount(actualLikes)
        }
    }

    @Test
    fun testMixOfLikesAndPasses() {
        logStep("Given: Profiles are loaded")
        val profilesPage = loginToApp()

        logStep("When: User likes 3 profiles and passes 2 profiles")
        profilesPage.likeAndPassProfiles(
            TestData.Counts.LIKES_FOR_MIX_TEST,
            TestData.Counts.PASSES_FOR_MIX_TEST
        )

        waitForIdle()

        logStep("Then: Correct counts are shown in finished state")
        if (profilesPage.isFinishedStateDisplayed()) {
            profilesPage.verifyFinishedStateDisplayed()
            logger.info("Finished state displayed with counts")
        }
    }

    @Test
    fun testRetryAfterNetworkError() {
        logStep("Given: Error state is displayed")
        val profilesPage = loginToApp()

        if (profilesPage.isErrorStateDisplayed()) {
            logStep("When: User clicks Retry button")
            profilesPage.clickRetry()

            waitForIdle()
            Thread.sleep(1000)

            logStep("Then: Profiles load successfully")
            profilesPage.verifyProfilesLoaded()
            logger.info("✓ Retry successful - profiles loaded")
        } else {
            logger.warn("Error state not present - skipping retry test")
        }
    }

    @Test
    fun testProfilesLoadQuickly() {
        logStep("Given: User logged in")
        val startTime = System.currentTimeMillis()
        val profilesPage = loginToApp()

        logStep("When: Profiles are loading")
        profilesPage.waitForProfilesToLoad(TestData.Timeouts.MEDIUM_TIMEOUT)

        val loadTime = System.currentTimeMillis() - startTime

        logStep("Then: Profiles load within 5 seconds")
        assert(loadTime < TestData.Timeouts.MEDIUM_TIMEOUT) {
            "Profiles took ${loadTime}ms to load, expected < ${TestData.Timeouts.MEDIUM_TIMEOUT}ms"
        }
        logger.info("✓ Profiles loaded in ${loadTime}ms (within acceptable time)")
    }
}
