package com.example.watermatters.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.watermatters.data.User


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UsersScreen(
    users: List<User>,
    modifier: Modifier = Modifier,
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    // Fade in entry animation for the entire list
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        ),
        exit = fadeOut()
    ) {
        LazyColumn {
            itemsIndexed(users) { index, user ->
                UserListItem(
                    user = user,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessVeryLow,
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * (index + 1) } // staggered entrance
                            )
                        ),
//                    isCurrentUser = currentUserName == user.userName
                )
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean = false,
) {
    Card(
        elevation = 2.dp,
        modifier = modifier,
//        backgroundColor = if (isCurrentUser) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
    ) {
        val badgeNumber = user.drops.toString()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column {
                BadgedBox(badge = { Badge { Text(badgeNumber) } }) {
                    Text(
                        text = user.userName,
                        style = MaterialTheme.typography.h3
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                LinearProgressIndicator(
                    progress = user.drops / 100f,
                    modifier = Modifier
                        .height(8.dp)
                        .clip(RoundedCornerShape(16.dp)),
                )
            }
            Spacer(modifier = Modifier.weight(1F))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))

            ) {
                Image(
                    painter = painterResource(user.icon),
                    contentDescription = null,
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}
