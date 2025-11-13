package com.test.muzz.utils

object TestData {


    object ValidCredentials {
        const val USERNAME = "user"
        const val PASSWORD = "password"
        const val FIRST_USER = "first_user"
        const val SECOND_USER = "second_user"
    }


    object InvalidCredentials {
        const val WRONG_USERNAME = "wrong_user"
        const val WRONG_PASSWORD = "wrong_pass"
        const val EMPTY_USERNAME = ""
        const val EMPTY_PASSWORD = ""
    }


    object Counts {
        const val PROFILES_TO_LIKE = 5
        const val PROFILES_TO_PASS = 2
        const val LIKES_FOR_MIX_TEST = 3
        const val PASSES_FOR_MIX_TEST = 2
    }


    object Timeouts {
        const val MEDIUM_TIMEOUT = 5000L
    }
}
