package dev.msartore.linkextractor.ui.compose.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import dev.msartore.linkextractor.R

@Composable
fun TextUrl(
    urlData: String,
    onOpenUrl: (String) -> Unit,
    onCopy: (String) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 24.dp)
            .padding(bottom = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.weight(5f),
            maxLines = 2,
            text = urlData,
            overflow = Ellipsis,
        )

        IconButtonE(
            painter = painterResource(id = R.drawable.open_in_new_24px),
            contentDescription = stringResource(id = R.string.open_in_app),
        ) {
            onOpenUrl(urlData)
        }

        IconButtonE(
            painter = painterResource(id = R.drawable.content_copy_24px),
            contentDescription = stringResource(id = R.string.copy_url)
        ) {
            onCopy(urlData)
        }
    }
}

@Composable
fun TextButtonE(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}