package com.example.chatbot.root.presentance.gemini.screen.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.R
import com.example.chatbot.root.data.local.GeminiData


@Composable
fun GeminiTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Chat bot",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }

}


@Composable
fun ResponseMessage(
    geminiData: GeminiData,
) {

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 100.dp,
                bottom = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Person,
                        contentDescription = "you"
                    )
                    Text(
                        text = "You",
                        fontWeight = FontWeight.Bold
                    )
                }

                geminiData.image?.let {
                    Image(
                        bitmap =it.asImageBitmap(),
                        contentDescription ="bitmap",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .size(200.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
                Spacer(modifier = Modifier
                    .height(4.dp))

                Text(
                    text = geminiData.prompt,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                )
            }
        }
    }

}

@Composable
fun BotMessage(
    message: String,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = 100.dp,
                bottom = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.robot),
                        contentDescription = "bot",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "Bot",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = message,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                )
            }
        }
    }
}

