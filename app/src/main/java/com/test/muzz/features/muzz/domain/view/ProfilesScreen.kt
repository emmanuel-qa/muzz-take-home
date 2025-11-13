package com.test.muzz.features.muzz.domain.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.test.muzz.features.muzz.data.model.Profile
import com.test.muzz.features.muzz.domain.viewmodel.ProfilesViewModel

@Composable
fun ProfilesScreen(
    viewModel: ProfilesViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("profiles_screen"),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top bar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profiles",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag("title_profiles")
                )
                TextButton(
                    onClick = onLogout,
                    modifier = Modifier.testTag("button_logout")
                ) {
                    Text("Logout")
                }
            }

            // Content
            when {
                state.loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag("loading_indicator")
                        )
                    }
                }

                state.error != null -> {
                    ErrorState(
                        message = state.error ?: "Unknown error",
                        onRetry = { viewModel.load() }
                    )
                }

                state.finished -> {
                    FinishedState(
                        liked = state.liked,
                        passed = state.passed,
                        onReset = { viewModel.resetDeck() }
                    )
                }

                else -> {
                    state.current?.let { profile ->
                        ProfileCard(
                            profile = profile,
                            index = state.index + 1,
                            total = state.profiles.size
                        )
                    }
                }
            }

            // Actions
            if (!state.loading && !state.finished && state.error == null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .testTag("action_button_row"),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.pass() },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_pass")
                            .semantics { contentDescription = "Pass Profile" }
                    ) { Text("Pass") }

                    Button(
                        onClick = { viewModel.like() },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_like")
                            .semantics { contentDescription = "Like Profile" }
                    ) { Text("Like") }
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: Profile,
    index: Int,
    total: Int
) {
    Column(
        Modifier
            .fillMaxWidth()
            .testTag("profile_card"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = profile.photoUrl,
            contentDescription = "${profile.name} photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(16.dp))
                .testTag("profile_image")
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("profile_name")
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = profile.age,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.testTag("profile_age")
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "$index of $total",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.testTag("profile_counter")
        )
    }
}

@Composable
private fun FinishedState(liked: Int, passed: Int, onReset: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .testTag("finished_state"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "You're all caught up!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("finished_title")
        )
        Spacer(Modifier.height(8.dp))
        Text("Liked: $liked • Passed: $passed",
            modifier = Modifier.testTag("likes_count_text"))
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onReset,
            modifier = Modifier.testTag("button_reset_deck")
        ) { Text("Reset deck") }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .testTag("error_state"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Oops: $message",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("error_message_text")
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.testTag("button_retry")
        ) { Text("Retry") }
    }
}
