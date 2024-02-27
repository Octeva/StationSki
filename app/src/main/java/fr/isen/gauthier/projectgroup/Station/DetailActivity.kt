package fr.isen.gauthier.projectgroup.Station

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import fr.isen.gauthier.projectgroup.R
import fr.isen.gauthier.projectgroup.ui.theme.ProjectGroupTheme



class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

        }
    }

    override fun onPause(){
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
