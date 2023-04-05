package com.example.watermatters.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.magnifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatterMattersTopAppBar(
    title: String,
    @DrawableRes logoIcon : Int? = null,
    infoClicked : () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Filled.ThumbUp,
//                    contentDescription = title,
//                )
//                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
//        navigationIcon = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack,
//                    contentDescription = "Localized description"
//                )
//            }
//        },
        actions = {
            IconButton(onClick = { infoClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Watter Matters Information",
                )
            }
        }
    )
}