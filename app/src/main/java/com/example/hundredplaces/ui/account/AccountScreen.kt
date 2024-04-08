package com.example.hundredplaces.ui.account

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination

object AccountDestination : NavigationDestination {
    override val route = "Account"
    override val iconRes = R.drawable.baseline_account_circle_24
}

/**
 * Entry route for Account screen
 */
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier
){
    Row {
        Text(text = "This is the account screen")
    }
}