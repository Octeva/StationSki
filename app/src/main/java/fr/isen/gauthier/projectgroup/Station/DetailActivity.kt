package fr.isen.gauthier.projectgroup.Station

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import fr.isen.gauthier.projectgroup.Network.Pistes
import fr.isen.gauthier.projectgroup.R
import fr.isen.gauthier.projectgroup.ui.theme.ProjectGroupTheme

enum class PistesType {
    VERTE, BLEUE, ROUGE, NOIRE;


    @Composable
    fun title(): String {
        return when(this) {
            VERTE -> stringResource(id = R.string.piste_verte)
            BLEUE -> stringResource(id = R.string.piste_bleue)
            ROUGE -> stringResource(id = R.string.piste_rouge)
            NOIRE -> stringResource(id = R.string.piste_noire)
        }
    }
}

interface PistesInterface {
    fun pistePressed(pistesType: PistesType)
}

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjectGroupTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //SetupView(this)



                    //Haut de la page = titre + texte
                    Column(horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Informations sur la pistes",
                            fontSize = 30.sp
                            )
                        Text(text = "Renseignez les informations actuelles sur pistes pour les autres utilisateurs",
                            fontSize = 20.sp
                            )
                    }


                    //Affichage des details des pistes



                    //Rentrer les informations des details des pistes
                    Column(horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Piste verte",
                            fontSize = 20.sp
                            )
                        Text(text = "Informations sur la piste verte",
                            fontSize = 15.sp
                            )
                    }

                }

            }
        }
    }

}

@Composable

