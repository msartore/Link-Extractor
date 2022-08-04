package dev.msartore.linkextractor.ui.compose

import android.content.pm.PackageInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.msartore.linkextractor.R
import dev.msartore.linkextractor.model.Settings
import dev.msartore.linkextractor.ui.compose.basic.IconButtonE
import dev.msartore.linkextractor.ui.compose.basic.SettingsItem
import dev.msartore.linkextractor.ui.compose.basic.SettingsItemSwitch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun SettingsUI(
    urlFlow: MutableSharedFlow<String>,
    pageVisibility: MutableState<Int>,
    settings: Settings
) {

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var info: PackageInfo?

    LocalContext.current.apply {
        info = packageManager.getPackageInfo(packageName, 0)
    }

    BackHandler {
        pageVisibility.value = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonE(
                painter = painterResource(id = R.drawable.arrow_back_24px),
                contentDescription = stringResource(id = R.string.back),
            ) {
                pageVisibility.value = 0
            }

            Text(text = stringResource(R.string.settings))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.app),
                style = MaterialTheme.typography.bodySmall
            )

            SettingsItemSwitch(
                title = stringResource(id = R.string.download_media),
                icon = painterResource(id = R.drawable.file_download_24px),
                item = settings.downloadMedia,
            )

            SettingsItemSwitch(
                title = stringResource(id = R.string.start_app),
                icon = painterResource(id = R.drawable.start_24px),
                item = settings.startApp,
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.about),
                style = MaterialTheme.typography.bodySmall
            )

            SettingsItem(
                title = stringResource(R.string.licenses),
                icon = painterResource(id = R.drawable.description_24px),
                onClick = {
                    pageVisibility.value = 2
                }
            )

            SettingsItem(
                title = stringResource(R.string.illustrations_credit),
                icon = painterResource(id = R.drawable.draw_24px),
                onClick = {
                    scope.launch {
                        urlFlow.emit("http://storyset.com/")
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.contribute),
                icon = painterResource(id = R.drawable.handshake_24px),
                onClick = {
                    scope.launch {
                        urlFlow.emit("https://github.com/msartore/Link-Extractor")
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.donate),
                icon = painterResource(id = R.drawable.volunteer_activism_24px),
                onClick = {
                    scope.launch {
                        urlFlow.emit("https://msartore.dev/donation/")
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.more_about_me),
                icon = painterResource(id = R.drawable.favorite_24px),
                onClick = {
                    scope.launch {
                        urlFlow.emit("https://msartore.dev/#projects")
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Gallery v${info?.versionName}",
                    fontSize = 10.sp
                )
            }
        }
    }
}