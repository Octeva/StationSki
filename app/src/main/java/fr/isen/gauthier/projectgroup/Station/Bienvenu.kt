package fr.isen.gauthier.projectgroup.Station

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import fr.isen.gauthier.projectgroup.R
import androidx.compose.ui.input.pointer.pointerInput



class Bienvenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
}

@Composable fun BasketView() {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = " Bienvenue ",
                fontFamily = FontFamily.Default,
                fontSize = 44.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painterResource(R.drawable.plan_des_pistes_alpin_molines_saint_veran_domaine_beauregard),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(350.dp) // Augmentation de la taille de l'image
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(elevation = 8.dp)
                .background(color = colorResource(id = R.color.purple_500))
        ) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(context, PisteActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable._200px_alpine_skiing_pictogram_svg),
                    contentDescription = null
                )
            }
            OutlinedButton(
                onClick = {
                    val intent = Intent(context, RemonteActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable._205016),
                    contentDescription = null
                )
            }
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "ici", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable.picto_maison_png),
                    contentDescription = null
                )
            }
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "blabla", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable._58656_chat_icon_free_clipart_hd),
                    contentDescription = null
                )
            }
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "rafraiche", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable.refresh_icon),
                    contentDescription = null
                )
            }
        }
    }
}
