package dev.msartore.linkextractor.ui.compose.basic

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun IconButtonE(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
        )
    }
}