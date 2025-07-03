package com.example.hundredplaces.ui.camera

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.hundredplaces.R
import com.example.hundredplaces.data.services.landmark.RequestResponse

@Composable
fun LandmarkAlertDialog(
    requestResponse: RequestResponse,
    placeId: Long?,
    onDismissRequest: () -> Unit,
    navigateToPlace: (Long, Boolean) -> Unit,
    searchWeb: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSuccess = requestResponse.isSuccess
    val landmark = requestResponse.landmark
    val hasLandmark = landmark != null
    val hasPlaceId = placeId != null

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,

        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = if (isSuccess) stringResource(R.string.dismiss) else stringResource(R.string.try_again)
                )
            }
        },

        confirmButton = {
            if (!isSuccess || !hasLandmark) return@AlertDialog

            val (label, icon) = when {
                hasPlaceId -> Pair(stringResource(R.string.read_more), R.drawable.rounded_arrow_forward_24)
                else -> Pair(stringResource(R.string.search_more), R.drawable.rounded_open_in_new_24)
            }

            TextButton(
                onClick = { if (hasPlaceId) navigateToPlace(placeId, false) else searchWeb(landmark.name)}
            ) {
                Text(label)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
        },

        title = {
            when {
                isSuccess && hasLandmark -> Text(landmark.name)
                isSuccess && !hasLandmark -> Text(stringResource(R.string.not_found))
                else -> Text(stringResource(R.string.error))
            }
        },

        text = {
            when {
                isSuccess && hasLandmark && hasPlaceId ->
                    Text(stringResource(R.string.click_to_learn_more))
                isSuccess && hasLandmark ->
                    Text(stringResource(R.string.click_to_search_internet))
                isSuccess && !hasLandmark ->
                    Text(stringResource(R.string.landmark_not_recognized))
                else ->
                    Text(stringResource(R.string.error_with_request))
            }
        }
    )
}