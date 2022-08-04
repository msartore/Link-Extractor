package dev.msartore.linkextractor.ui.compose.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItem(
    title: String,
    icon: Painter,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(5f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title,
                overflow = Ellipsis,
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content?.invoke()
        }
    }
}

@Composable
fun SettingsItemSwitch(
    title: String,
    icon: Painter,
    item: MutableState<Boolean>
) {
    SettingsItem(
        title = title,
        icon = icon,
        onClick = {
            item.value = !item.value
        }
    ) {
        Switch(
            checked = item.value,
            onCheckedChange = {
                item.value = it
            }
        )
    }
}