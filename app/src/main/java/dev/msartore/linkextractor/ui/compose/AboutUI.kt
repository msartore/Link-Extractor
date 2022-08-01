package dev.msartore.linkextractor.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import dev.msartore.linkextractor.R
import dev.msartore.linkextractor.ui.compose.basic.IconButtonE
import dev.msartore.linkextractor.ui.compose.basic.TextButtonE


@Composable
fun AboutUI(
    onBackPressed: () -> Unit
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    BackHandler {
        onBackPressed()
    }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonE(
                painter = painterResource(id = R.drawable.arrow_back_24px),
                contentDescription = stringResource(id = R.string.back),
            ) {
                onBackPressed()
            }

            Text(text = stringResource(R.string.about))
        }

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(35.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = stringResource(R.string.link_extractor_title),
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(text = stringResource(id = R.string.link_extractor_description))
            }

            Divider(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.third_party_licenses)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(35.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = stringResource(R.string.kotlin_title),
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.kotlin_descriptionTitle) + stringResource(
                        id = R.string.apache_license
                    ),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(35.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = stringResource(R.string.accompanist_title),
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.accompanist_descriptionTitle) + stringResource(
                        id = R.string.apache_license
                    ),
                )
            }

            Divider(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            TextButtonE(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.illustrations_credit)
            ) {
                startActivity(
                    context,
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://storyset.com/")
                    },
                    null
                )
            }
        }
    }
}