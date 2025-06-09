package com.example.hundredplaces.ui.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAccountDialog(
    accountViewModel: AccountViewModel,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Card {
            AnimatedContent(
                targetState = isFlipped,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(400, 400),
                        initialOffsetX = { it }
                    ) + fadeIn(tween(400, 400)) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(400, 0),
                        targetOffsetX = { -it }
                    ) + fadeOut(tween(400, 0))
                }
            ) { targetState ->
                if (!targetState) {
                    UpdateDetailsForm(
                        accountViewModel = accountViewModel,
                        onCancel = onDismissRequest,
                        onChangePassword = { isFlipped = !isFlipped }
                    )
                } else {
                    UpdatePasswordForm(
                        accountViewModel = accountViewModel,
                        onCancel = onDismissRequest
                    )
                }
            }
        }
    }
}