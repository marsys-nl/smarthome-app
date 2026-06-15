package network.marsys.smarthome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import network.marsys.smarthome.shared.library.store.datastore.createDataStore
import network.smarthome.shared.library.SmartHomeApp
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private val applicationModule = module {
        single<DataStore<Preferences>> {
            createDataStore(
                context = this@MainActivity,
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SmartHomeApp(
                applicationModule = applicationModule,
            )
        }
    }
}
