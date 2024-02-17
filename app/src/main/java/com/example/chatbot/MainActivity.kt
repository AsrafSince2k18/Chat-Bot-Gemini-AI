package com.example.chatbot

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.Send
import androidx.compose.material.icons.sharp.AttachFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.chatbot.root.presentance.gemini.screen.mainScreen.BotMessage
import com.example.chatbot.root.presentance.gemini.screen.mainScreen.GeminiTopBar
import com.example.chatbot.root.presentance.gemini.screen.mainScreen.ResponseMessage
import com.example.chatbot.root.presentance.gemini.stateEvent.GeminiEvent
import com.example.chatbot.root.presentance.gemini.stateEvent.GeminiState
import com.example.chatbot.root.presentance.gemini.viewModel.GeminiViewModel
import com.example.chatbot.ui.theme.ChatBotTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private val pickImage = MutableStateFlow(GeminiState().uri)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object{
        @Composable
        fun GetImage() : Bitmap? {
            val context = LocalContext.current
            val asd= pickImage.collectAsState().value

            val asyncImage : AsyncImagePainter.State= rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(asd)
                    .size(Size.ORIGINAL)
                    .build()).state
            return if(asyncImage is AsyncImagePainter.State.Success){
                asyncImage.result.drawable.toBitmap()
            }else{
                null
            }
        }
    }
    private val pick = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        uri?.let {uris->
            pickImage.update {
                uris.toString()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ChatBotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<GeminiViewModel>()
                    val state by viewModel.state.collectAsState()
                    MainScreen(geminiState = state,
                        event = viewModel::onEvent,
                        pickVisualMediaRequest = {
                            pick.launch(
                                PickVisualMediaRequest
                                    .Builder()
                                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    .build()
                            )
                        },
                    )


                }
            }
        }

    }

}

@Composable
fun MainScreen(
    geminiState: GeminiState,
    event: (GeminiEvent) -> Unit,
    pickVisualMediaRequest: () -> Unit,
) {


    if(geminiState.isError!=null) {
        geminiState.isError.let {
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            GeminiTopBar()
        }
    ) { paddingValues ->
            SetItem(geminiState = geminiState,
                event = event,
                paddingValues = paddingValues,
                pickVisualMediaRequest = {
                    pickVisualMediaRequest()
                }
            )

    }

}


@Composable
fun SetItem(
    geminiState: GeminiState,
    event: (GeminiEvent) -> Unit,
    paddingValues: PaddingValues,
    pickVisualMediaRequest: () -> Unit,
) {
    val bitmap = MainActivity.GetImage()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(9f),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = true
            ) {
                itemsIndexed(geminiState.chatList) { index, chat ->
                    if (chat.isUser) {
                        ResponseMessage(geminiData = chat)
                    } else {
                        BotMessage(message = chat.prompt)
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 4.dp,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        bitmap?.let {
                            Image(
                                bitmap =it.asImageBitmap(),
                                contentDescription ="",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                        IconButton(onClick = {
                            pickVisualMediaRequest()
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.AttachFile,
                                contentDescription = "",
                                modifier = Modifier
                                    .rotate(45f)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = geminiState.prompt ?: "",
                        onValueChange = {
                            event(GeminiEvent.Message(it))
                        },
                        placeholder = {
                            Text(text = "Ask me anything...")
                        },
                        modifier = Modifier
                            .width(250.dp)
                    )

                    IconButton(onClick = {
                        event(
                            GeminiEvent.MessageAndBitmap(message = geminiState.prompt ?:"",
                                bitmap =bitmap))
                        pickImage.update { "" }

                    }) {
                        if (geminiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Sharp.Send,
                                contentDescription = ""
                            )
                        }
                    }

                }
            }

        }

    }


}