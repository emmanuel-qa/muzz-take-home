package com.test.muzz.utils

import android.util.Log

class TestLogger {

    companion object {
        private const val TAG = "MuzzQA"
        private const val SEPARATOR = "==================================================="
    }

    /**
     * Log informational message
     * Used for test flow and actions
     */
    fun info(message: String) {
        Log.i(TAG, message)
        println("[INFO] $message")
    }

    /**
     * Log warning message
     * Used for non-critical issues
     */
    fun warn(message: String) {
        Log.w(TAG, message)
        println("[WARN] $message")
    }

    /**
     * Log error message
     * Used for test failures and exceptions
     */
    fun error(message: String) {
        Log.e(TAG, message)
        println("[ERROR] $message")
    }

    /**
     * Log test start
     * Creates a clear visual separator
     */
    fun testStart(testName: String) {
        val message = """

            $SEPARATOR
            ▶ TEST STARTED: $testName
            $SEPARATOR
        """.trimIndent()
        info(message)
    }

    /**
     * Log test completion (passed)
     */
    fun testPassed(testName: String) {
        val message = """
            $SEPARATOR
            ✓ TEST PASSED: $testName
            $SEPARATOR

        """.trimIndent()
        info(message)
    }

    /**
     * Log test failure
     */
    fun testFailed(testName: String, error: String) {
        val message = """
            $SEPARATOR
            ✗ TEST FAILED: $testName
            Error: $error
            $SEPARATOR

        """.trimIndent()
        error(message)
    }

    /**
     * Log step execution
     * Used for BDD-style step logging
     */
    fun step(stepDescription: String) {
        info("  → $stepDescription")
    }

}
