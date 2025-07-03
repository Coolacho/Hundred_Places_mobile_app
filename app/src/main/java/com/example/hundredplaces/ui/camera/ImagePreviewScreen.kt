package com.example.hundredplaces.ui.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.hundredplaces.R

@Composable
fun ImagePreviewScreen(
    image: ImageBitmap,
    onDiscardClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            bitmap = image,
            contentDescription = "Captured image",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .zIndex(1.2f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            FilledTonalButton (
                onClick = onDiscardClick
            ) {
                Text(
                    text = stringResource(R.string.discard)
                )
            }
            Spacer(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.padding_small))
            )
            Button(
                onClick = onSubmitClick
            ) {
                Text(
                    text = stringResource(R.string.submit)
                )
                Spacer(
                    modifier = Modifier
                        .size(ButtonDefaults.IconSpacing)
                )
                Icon(
                    painter = painterResource(R.drawable.rounded_send_24),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }
    }
}