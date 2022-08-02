package dev.msartore.linkextractor

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.msartore.linkextractor.ui.compose.AboutUI
import dev.msartore.linkextractor.ui.compose.basic.IconButtonE
import dev.msartore.linkextractor.ui.compose.basic.TextButtonE
import dev.msartore.linkextractor.ui.compose.basic.TextUrl
import dev.msartore.linkextractor.ui.theme.LinkExtractorTheme
import dev.msartore.linkextractor.utils.copyToClipboard
import dev.msartore.linkextractor.utils.cor
import dev.msartore.linkextractor.utils.urlFinder

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        setContent {

            val isDarkTheme = remember { mutableStateOf(false) }

            LinkExtractorTheme(
                isDarkTheme = isDarkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val uiVisibility = remember { mutableStateOf(false) }
                    val urlString = remember { SnapshotStateList<String>() }
                    val text = remember { mutableStateOf("") }
                    val loading = remember { mutableStateOf(false) }
                    val aboutVisibility = remember { mutableStateOf(false) }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val context = LocalContext.current

                    LaunchedEffect(key1 = true) {

                        if (intent.action == Intent.ACTION_SEND) {

                            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                                urlString.addAll(urlFinder(it))
                            }

                            val toastText: String = when (urlString.size) {
                                0 -> {
                                    getString(R.string.no_url_found)
                                }
                                1 -> {
                                    clipboard.copyToClipboard(context, urlString[0])

                                    getString(R.string.url_copied)
                                }
                                else -> {
                                    clipboard.copyToClipboard(context, urlString.joinToString("\n"))

                                    getString(R.string.multiple_url_found)
                                }
                            }

                            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

                            finish()
                        }

                        if (urlString.size != 1) uiVisibility.value = true
                    }

                    if (uiVisibility.value) {

                        AnimatedVisibility(
                            visible = aboutVisibility.value,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { -it },
                        ) {
                            AboutUI {
                                aboutVisibility.value = false
                            }
                        }

                        AnimatedVisibility(
                            visible = !aboutVisibility.value,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { -it },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.TopCenter),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        colors = TextFieldDefaults.textFieldColors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        maxLines = 4,
                                        singleLine = false,
                                        shape = RoundedCornerShape(16.dp),
                                        value = text.value,
                                        onValueChange = { text.value = it },
                                        leadingIcon = {
                                            IconButtonE(
                                                painter = painterResource(id = R.drawable.content_paste_24px),
                                                contentDescription = getString(R.string.paste_text)
                                            ) {
                                                clipboard.primaryClip?.let {
                                                    text.value = it.getItemAt(0).text.toString()
                                                }
                                            }
                                        },
                                        trailingIcon = {
                                            IconButtonE(
                                                painter = painterResource(id = R.drawable.search_24px),
                                                contentDescription = getString(R.string.search)
                                            ) {
                                                keyboardController?.hide()

                                                if (text.value.isNotBlank())
                                                    cor {
                                                        loading.value = true
                                                        urlString.clear()
                                                        urlString.addAll(urlFinder(text.value))
                                                        text.value = ""
                                                        loading.value = false
                                                    }
                                            }
                                        },
                                        keyboardActions = KeyboardActions(
                                            onSearch = {

                                                keyboardController?.hide()

                                                if (text.value.isNotBlank())
                                                    cor {
                                                        loading.value = true
                                                        urlString.clear()
                                                        urlString.addAll(urlFinder(text.value))
                                                        text.value = ""
                                                        loading.value = false
                                                    }
                                            }
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search
                                        ),
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp, end = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = stringResource(id = R.string.url_count) + ": " + urlString.size,
                                            style = MaterialTheme.typography.labelLarge,
                                        )

                                        Row(
                                            modifier = Modifier
                                                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                                        ) {
                                            AnimatedVisibility(visible = !loading.value && urlString.isNotEmpty()) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(start = 16.dp),
                                                ) {
                                                    TextButtonE(stringResource(id = R.string.copy_all)) {
                                                        cor {
                                                            clipboard.copyToClipboard(context, urlString.joinToString("\n"))
                                                        }
                                                    }
                                                    TextButtonE(stringResource(id = R.string.clear_all)) {
                                                        cor {
                                                            urlString.clear()
                                                        }
                                                    }
                                                }
                                            }
                                            TextButtonE(stringResource(id = R.string.about)) {
                                                aboutVisibility.value = true
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = !loading.value && urlString.isNotEmpty()) {
                                        Column {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 16.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                items(
                                                    count = urlString.size,
                                                ) { it ->

                                                    TextUrl(
                                                        urlData = urlString[it],
                                                        onOpenUrl = {
                                                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                                                        }
                                                    ) { textCp ->
                                                        clipboard.copyToClipboard(context, textCp)
                                                        Toast.makeText(context, getString(R.string.url_copied), Toast.LENGTH_SHORT).show()
                                                    }

                                                    if (it == urlString.size - 1) {
                                                        Spacer(modifier = Modifier.height(80.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = !loading.value && urlString.isEmpty()) {
                                        Image(
                                            modifier = Modifier.fillMaxSize(),
                                            painter = painterResource(id = R.drawable.no_data_cuate),
                                            contentDescription = stringResource(id = R.string.no_url_found)
                                        )
                                    }

                                    AnimatedVisibility(visible = loading.value) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(64.dp),
                                        )
                                    }
                                }

                                if (urlString.isNotEmpty())
                                    IconButtonE(
                                        modifier = Modifier
                                            .padding(bottom = 16.dp)
                                            .align(Alignment.BottomCenter)
                                            .background(
                                                color = MaterialTheme.colorScheme.background,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                RoundedCornerShape(16.dp)
                                            ),
                                        painter = painterResource(id = R.drawable.share_24px),
                                        contentDescription = stringResource(id = R.string.share)
                                    ) {

                                        if (urlString.isNotEmpty()) {
                                            val shareIntent = Intent(Intent.ACTION_SEND)
                                            shareIntent.type = "text/plain"
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, urlString.joinToString("\n"))
                                            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
                                        }
                                        else {
                                            Toast.makeText(context, getString(R.string.no_url_found), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}