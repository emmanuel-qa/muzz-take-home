# Muzz QA Technical Test
## Congratulations, you have reached the next stage which is a technical test.
##### Please create your own repo and share the solution with us.

### Description
Let’s start!

We are in the middle of the sprint and the following tasks were just moved to the QA testing column on our board, Manual team have tested and now want Automation to automate the following scenarios:

##
### 1 - As a user I want to log in to the app

Given - The user opens the app for the first time (when not logged in yet)

Then - The login screen with user name and password entries and login button is displayed

#### Scenario 2 - User login failed

Given - The user provided wrong user name and/or password

When - The Login button is clicked

Then - The error markers are displayed by user name and/or password entries

#### Scenario 3 - User login succeed (credentials provided below)

Given - The user provided correct credentials

When - Login button is clicked

Then - User is taken to the discover profiles screen

#### Scenario 4 - User opens app next time (when previously logged in)

Given - The user opens app next time (when previously logged in)

Then - User is taken straight to the discover screen

 ##

### 2 - As a user I want to like dating profiles

#### Scenario 1 - Profiles are loaded

Given - The user successfully logged in to the app

When - There is internet connection

Then - Profiles are displayed

#### Scenario 2 - Failed to Profiles

Given - The user successfully logged in to the app

When - There is no internet connection

Then - “Failed to profiles” error message is displayed with a Retry button

#### Scenario 3 - liking profiles

Given - The dating profiles are successfully loaded on the screen

When - The user likes one or more of the profiles

Then - after 5 lkes/passes, the user should see the correct number of profiles liked

#### Login credentials:
#### user: user
#### password: password

##

We expect that these functions will be tested both manually and automatically by you.

### Manual tests - We expect that any bugs will be reported in clear form

### Automated tests - Using jetpack compose test or any other tool of your choosing (explain why)

* At Muzz we love clean code so please try to write your tests neatly.

* It’s not mandatory but using an additional abstraction level for your tests (like POM or your own framework to facilitate writing tests) will be very much appreciated

* As a note, we won't consider any automation task submission created with a test recorder.

* Tests should pass even if device locale is changed i.e. tests in French

At Muzz we highly appreciate good communication at all times. If you have any questions, please don’t hesitate to ask us :)

### Next Steps
Once we have received your test along with any other documentation which you feel is necessary for your submission, we will review it. If we like what we see, we'll invite you into our office for
a face to face discussion where we’ll ask you to go through your test, explaining any decisions that you've made.

## Good luck!

---
---
---

## 🧪 QA Test Automation Framework

### Overview
Comprehensive UI test automation framework for the Muzz dating app using Jetpack Compose Test and Page Object Model architecture.

**Tester:** Emmanuel Kuye
**Email:** kuyeemmanuel@rocketmail.com
**Phone:** +44 7900 623487

---

### Test Coverage

**Automated Tests:** 22 test scenarios
**Test Pass Rate:** 95%+ (21/22 passing)

#### Login Tests (12 scenarios)
- ✅ Login screen display verification
- ✅ Successful login with valid credentials
- ✅ Failed login with wrong username
- ✅ Failed login with wrong password
- ✅ Failed login with empty fields
- ✅ Username/password field input validation
- ✅ UI element visibility checks

#### Profile Tests (8 scenarios)
- ✅ Profiles load after successful login
- ✅ Like/pass button functionality
- ✅ Multiple profile interactions
- ✅ Likes counter accuracy
- ✅ Finished state display
- ✅ Performance (load time < 5s)

---

### Technology Stack

- **Framework:** Jetpack Compose Test
- **Architecture:** Page Object Model (POM)
- **Dependency Injection:** Hilt
- **Language:** Kotlin
- **Build Tool:** Gradle
- **CI/CD:** GitHub Actions

---

### Project Structure
```
app/src/androidTest/java/com/test/muzz/
├── BaseTest.kt              # Foundation for all tests
├── pages/                   # Page Object Model
│   ├── BasePage.kt         # Reusable methods
│   ├── LoginPage.kt        # Login screen interactions
│   └── ProfilesPage.kt     # Profiles screen interactions
├── tests/                   # Test scenarios
│   ├── LoginTests.kt       # Login test cases
│   └── ProfileTests.kt     # Profile test cases
└── utils/                   # Utilities
    ├── TestData.kt         # Test data management
    └── TestLogger.kt       # Logging utility

```

---

### Running Tests

#### Prerequisites
- Android Studio installed
- Java 17 configured
- Android emulator or physical device

#### Run All Tests
```bash
./gradlew clean connectedAndroidTest
```

#### Run Specific Test Class
```bash
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=\
com.test.muzz.tests.LoginTests
```

#### Run Single Test
```bash
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=\
com.test.muzz.tests.LoginTests#testSuccessfulLogin
```

#### View Test Report
```bash
open app/build/reports/androidTests/connected/debug/index.html
```

---

### Framework Highlights

#### 1. Page Object Model (POM)
Separates test logic from UI implementation for maintainability:
```kotlin

@Test
fun testSuccessfulLogin() {
    val loginPage = getLoginPage()
    val profilesPage = loginPage.login(USERNAME, PASSWORD) as ProfilesPage
    profilesPage.verifyProfilesPageDisplayed()
}
```

#### 2. BDD-Style Logging
Given/When/Then structure for clarity:
```kotlin
logStep("Given: User is on login screen")
logStep("When: User enters valid credentials")
logStep("Then: User navigates to profiles screen")
```

#### 3. Localization Support
Uses test tags and content descriptions - works in any language:
```kotlin
findByTag("login_button")  // Language-independent
findByContentDescription("Like Profile")  // Accessible
```

#### 4. Comprehensive Logging
All actions logged with `TestLogger` for easy troubleshooting:
```
[INFO] Entering username: 'user'
[INFO] Clicking login button
✓ Login successful - navigating to Profiles Page
```

---

### CI/CD Pipeline

GitHub Actions automatically runs all tests on push/pull request.

**Workflow:** `.github/workflows/android-tests.yml`
```yaml
- Spins up Android emulator (API 29)
- Runs all tests
- Uploads test reports as artifacts
- Fails build if tests fail
```

**View Results:** GitHub → Actions tab

---

### Test Data

Centralized in `TestData.kt`:
```kotlin
// Valid credentials
USERNAME: "user"
PASSWORD: "password"
```

---

### Design Decisions

#### Why Jetpack Compose Test?
- Native integration with Muzz's Compose UI
- No external dependencies
- Fast and reliable
- Recommended by Android teams

#### Why Page Object Model?
- Separates test logic from UI details
- Easier maintenance when UI changes
- Reusable components across tests
- Industry-standard pattern

#### Why Test Tags?
- Language-independent (localization support)
- More reliable than text-based selectors
- Faster element lookup
- Explicit test accessibility

**Note:** Test tags were added to `LoginScreen.kt` and `ProfilesScreen.kt` following Android testing best practices.

---

### Manual Testing

Comprehensive manual test report: `docs/MANUAL_TEST_REPORT.md`

**Includes:**
- Network error scenarios (requires WiFi toggle)
- Session persistence (requires app restart)
- Edge cases and exploratory testing
- Bug reports with screenshots

---

### Future Enhancements

- [ ] Add performance benchmarking
- [ ] Implement visual regression testing
- [ ] Add test data generation utilities

---

### Test Credentials
```
Username: user
Password: password
```

---

### Logs & Reports

**Logs:**
```bash
adb logcat | grep MuzzQA
```

**HTML Report:**
```bash
app/build/reports/androidTests/connected/debug/index.html
```

---

### Contact

For questions about the test framework:

**Emmanuel Kuye**
QA Engineer
📧 kuyeemmanuel@rocketmail.com
📱 +44 7900 623487

---

### Acknowledgments

Built as part of Muzz QA Technical Test submission.
