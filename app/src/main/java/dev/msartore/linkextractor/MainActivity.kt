package dev.msartore.linkextractor

import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import dev.msartore.linkextractor.MainActivity.MActivity.dataStore
import dev.msartore.linkextractor.model.Settings
import dev.msartore.linkextractor.ui.compose.AboutUI
import dev.msartore.linkextractor.ui.compose.SettingsUI
import dev.msartore.linkextractor.ui.compose.basic.IconButtonE
import dev.msartore.linkextractor.ui.compose.basic.TextButtonE
import dev.msartore.linkextractor.ui.compose.basic.TextUrl
import dev.msartore.linkextractor.ui.theme.LinkExtractorTheme
import dev.msartore.linkextractor.utils.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    object MActivity {
        val Context.dataStore by preferencesDataStore(
        name = "user_preferences_settings"
        )
    }

    private var settings: Settings? = null

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val urlFlow = MutableSharedFlow<String>()

        settings = Settings(dataStore)

        setContent {

            val isDarkTheme = remember { mutableStateOf(false) }
            val urlString = remember { SnapshotStateList<String>() }
            val text = remember { mutableStateOf("") }
            val loading = remember { mutableStateOf(true) }
            val pageVisibility = remember { mutableStateOf(0) }
            val scope = rememberCoroutineScope()
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current
            val urlExtractionLambda: suspend (String) -> Unit = {
                urlString.clear()
                urlString.addAll(urlFinder(it))
                if (urlString.isNotEmpty() && text.value.isNotEmpty()) {
                    text.value = ""
                }

                cor {
                    urlString.forEach { url ->

                        val (result, name) = checkIfUrlIsMedia(url)

                        if (result && settings?.downloadMedia?.value == true)
                            downloadManager?.downloadFile(it, name, context)
                    }
                }
            }

            LaunchedEffect(key1 = true) {

                settings?.update()

                if (intent.action == Intent.ACTION_SEND) {

                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                        urlExtractionLambda(it)
                    }

                    if (settings?.startApp?.value != true) {
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
                }

                loading.value = false
            }

            cor {
                urlFlow.collect {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                }
            }

            LinkExtractorTheme(
                isDarkTheme = isDarkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AnimatedVisibility(
                        visible = pageVisibility.value == 2 && !loading.value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        AboutUI {
                            pageVisibility.value = 1
                        }
                    }

                    AnimatedVisibility(
                        visible = pageVisibility.value == 1 && !loading.value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        SettingsUI(
                            urlFlow = urlFlow,
                            pageVisibility = pageVisibility,
                            settings = settings!!,
                        )
                    }

                    AnimatedVisibility(
                        visible = pageVisibility.value == 0 && !loading.value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
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
                                            focusManager.clearFocus()

                                            if (text.value.isNotBlank())
                                                scope.launch { urlExtractionLambda(text.value) }
                                        }
                                    },
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()

                                            if (text.value.isNotBlank())
                                                scope.launch { urlExtractionLambda(text.value) }
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
                                        AnimatedVisibility(visible = urlString.isNotEmpty()) {
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

                                        IconButtonE(
                                            painter = painterResource(id = R.drawable.settings_24px),
                                            contentDescription = getString(R.string.settings)
                                        ) {
                                            pageVisibility.value = 1
                                        }
                                    }
                                }

                                AnimatedVisibility(visible = urlString.isNotEmpty()) {
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
                                                        scope.launch {
                                                            urlFlow.emit(it)
                                                        }
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

                                AnimatedVisibility(visible = urlString.isEmpty()) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.no_data_cuate),
                                        contentDescription = stringResource(id = R.string.no_url_found)
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

                    AnimatedVisibility(
                        visible = loading.value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(64.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        cor {
            settings?.save()
        }
    }
}