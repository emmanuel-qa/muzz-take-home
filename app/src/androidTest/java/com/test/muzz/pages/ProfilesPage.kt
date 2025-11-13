package com.test.muzz.pages

import androidx.compose.ui.test.junit4.ComposeTestRule

/**
 * ProfilesPage - Page Object for Profiles Screen
 */
class ProfilesPage(composeTestRule: ComposeTestRule) : BasePage(composeTestRule) {

    init {
        logger.info("=== Navigated to: Profiles Page ===")
    }

    // ========== LOCATORS ==========
    private fun getLogoutButton() = findByTag("button_logout")
    private fun getProfileCard() = findByTag("profile_card")
    private fun getProfileName() = findByTag("profile_name")
    private fun getFinishedState() = findByTag("finished_state")
    private fun getErrorState() = findByTag("error_state")
    private fun getRetryButton() = findByTag("button_retry")
    private fun getLikeButton() = findByTag("button_like")
    private fun getPassButton() = findByTag("button_pass")
    private fun getLikesCountText() = findByTag("likes_count_text")

    // ========== ACTIONS ==========

    fun likeProfile(): ProfilesPage {
        logger.info("Liking profile")
        click(getLikeButton())
        waitForIdle()
        return this
    }

    fun passProfile(): ProfilesPage {
        logger.info("Passing on profile")
        click(getPassButton())
        waitForIdle()
        return this
    }

    fun likeMultipleProfiles(count: Int): Int {
        logger.info("Liking $count profiles")
        var successfulLikes = 0

        for (i in 1..count) {
            try {
                logger.info("Liking profile $i of $count")

                if (isFinishedStateDisplayed()) {
                    logger.info("Reached end of profiles at $successfulLikes likes")
                    break
                }

                if (isProfileDisplayed()) {
                    likeProfile()
                    successfulLikes++
                    Thread.sleep(500)
                } else {
                    logger.warn("Profile $i not displayed, stopping")
                    break
                }
            } catch (e: Exception) {
                logger.error("Failed to like profile $i: ${e.message}")
                break
            }
        }

        logger.info("Successfully liked $successfulLikes out of $count profiles")
        return successfulLikes
    }

    fun likeAndPassProfiles(likes: Int, passes: Int): ProfilesPage {
        logger.info("Performing $likes likes and $passes passes")

        repeat(likes) {
            if (isProfileDisplayed() && !isFinishedStateDisplayed()) {
                likeProfile()
            }
        }

        repeat(passes) {
            if (isProfileDisplayed() && !isFinishedStateDisplayed()) {
                passProfile()
            }
        }

        return this
    }

    fun clickRetry(): ProfilesPage {
        logger.info("Clicking retry button")
        click(getRetryButton())
        waitForIdle()
        return this
    }

    fun clickLogout(): ProfilesPage {
        logger.info("Clicking logout button")
        click(getLogoutButton())
        waitForIdle()
        return this
    }
    // ========== VERIFICATIONS ==========

    fun verifyProfileDisplayed(): ProfilesPage {
        logger.info("Verifying profile is displayed")
        assertIsDisplayed(getProfileCard())
        logger.info("✓ Profile verified")
        return this
    }

    fun verifyProfilesLoaded(): ProfilesPage {
        logger.info("Verifying profiles are loaded")
        waitForIdle()
        assertIsDisplayed(getProfileCard())
        assertIsDisplayed(getLikeButton())
        assertIsDisplayed(getPassButton())
        logger.info("✓ Profiles loaded verified")
        return this
    }

    fun verifyFinishedStateDisplayed(): ProfilesPage {
        logger.info("Verifying finished state is displayed")
        assertIsDisplayed(getFinishedState())
        assertIsDisplayed(findByText("You're all caught up!"))
        logger.info("✓ Finished state verified")
        return this
    }

    fun verifyLikesCount(expectedLikes: Int): ProfilesPage {
        logger.info("Verifying likes count: $expectedLikes")

        try {
            if (!isFinishedStateDisplayed()) {
                logger.warn("Not in finished state yet, waiting...")
                Thread.sleep(1000)
            }

            val likesText = getLikesCountText()
            val text = getText(likesText)

            logger.info("Likes count text: '$text'")

            // Extract only the number after "Liked:"
            val actualCount = Regex("""Liked:\s*(\d+)""")
                .find(text)
                ?.groupValues?.get(1)?.toIntOrNull() ?: 0

            if (actualCount == expectedLikes) {
                logger.info("✓ Likes count verified: $actualCount")
            } else {
                logger.error("✗ Likes count mismatch - Expected: $expectedLikes, Actual: $actualCount")
                throw AssertionError("Expected $expectedLikes likes, but found $actualCount")
            }
        } catch (e: Exception) {
            logger.error("Failed to verify likes count: ${e.message}")
            throw e
        }

        return this
    }

    fun verifyLikeButtonEnabled(): ProfilesPage {
        logger.info("Verifying like button is enabled")
        assertIsDisplayed(getLikeButton())
        logger.info("✓ Like button enabled")
        return this
    }

    fun verifyPassButtonEnabled(): ProfilesPage {
        logger.info("Verifying pass button is enabled")
        assertIsDisplayed(getPassButton())
        logger.info("✓ Pass button enabled")
        return this
    }

    // ========== GETTERS & CHECKERS ==========

    fun isProfileDisplayed(): Boolean {
        return try {
            isDisplayed(getProfileCard())
        } catch (e: Exception) {
            false
        }
    }

    fun isErrorStateDisplayed(): Boolean {
        return try {
            isDisplayed(getErrorState())
        } catch (e: Exception) {
            false
        }
    }

    fun isFinishedStateDisplayed(): Boolean {
        return try {
            isDisplayed(getFinishedState())
        } catch (e: Exception) {
            false
        }
    }

    fun getProfileNameText(): String {
        return try {
            getText(getProfileName())
        } catch (e: Exception) {
            logger.warn("Could not get profile name: ${e.message}")
            ""
        }
    }

    fun waitForProfilesToLoad(timeoutMillis: Long = 5000): ProfilesPage {
        logger.info("Waiting for profiles to load")
        waitForElement(getProfileCard(), timeoutMillis)
        logger.info("Profiles loaded")
        return this
    }
}
