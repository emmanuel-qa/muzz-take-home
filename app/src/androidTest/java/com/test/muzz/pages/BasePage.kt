package com.test.muzz.pages

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import com.test.muzz.utils.TestLogger


abstract class BasePage(protected val composeTestRule: ComposeTestRule) {

    protected val logger = TestLogger()

    protected fun findByText(text: String): SemanticsNodeInteraction {
        logger.info("Finding element with text: '$text'")
        return composeTestRule.onNodeWithText(text, useUnmergedTree = true)
    }

    protected fun findByTag(tag: String): SemanticsNodeInteraction {
        logger.info("Finding element with tag: '$tag'")
        return composeTestRule.onNodeWithTag(tag, useUnmergedTree = true)
    }
    protected fun waitForElement(
        node: SemanticsNodeInteraction,
        timeoutMillis: Long = 5000
    ): SemanticsNodeInteraction {
        logger.info("Waiting for element (timeout: ${timeoutMillis}ms)")
        composeTestRule.waitUntil(timeoutMillis) {
            try {
                node.assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
        return node
    }
    protected fun isDisplayed(node: SemanticsNodeInteraction): Boolean {
        return try {
            node.assertIsDisplayed()
            logger.info("Element is displayed")
            true
        } catch (e: AssertionError) {
            logger.info("Element is not displayed")
            false
        }
    }
    protected fun click(node: SemanticsNodeInteraction) {
        logger.info("Performing click")
        node.performClick()
        composeTestRule.waitForIdle()
    }
    protected fun typeText(node: SemanticsNodeInteraction, text: String) {
        logger.info("Typing text: '$text'")
        node.performTextInput(text)
        composeTestRule.waitForIdle()
    }
    protected fun clearText(node: SemanticsNodeInteraction) {
        logger.info("Clearing text")
        node.performTextClearance()
        composeTestRule.waitForIdle()
    }
    protected fun assertIsDisplayed(node: SemanticsNodeInteraction) {
        logger.info("Asserting element is displayed")
        node.assertIsDisplayed()
    }
    protected fun getText(node: SemanticsNodeInteraction): String {
        return try {
            val semanticsNode = node.fetchSemanticsNode()
            semanticsNode.config.getOrNull(SemanticsProperties.EditableText)?.text
                ?: semanticsNode.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text
                ?: ""
        } catch (e: Exception) {
            logger.warn("Could not get text: ${e.message}")
            ""
        }
    }
    protected fun waitForIdle() {
        composeTestRule.waitForIdle()
    }
}
