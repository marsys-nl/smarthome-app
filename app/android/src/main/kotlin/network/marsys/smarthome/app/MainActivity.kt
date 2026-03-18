package network.marsys.smarthome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.marsys.smarthome.shared.library.store.datastore.SmartHomeStoreRepository
import network.marsys.smarthome.shared.library.store.datastore.createDataStore
import network.smarthome.shared.library.SmartHomeApp
import org.koin.dsl.binds
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private val applicationModule = module {
        single<DataStore<Preferences>> {
            createDataStore(
                context = this@MainActivity,
            )
        }

        single {
            SmartHomeStoreRepository(
                dataStore = get(),
            )
        } binds arrayOf(
            AppearancePreferencesRepository::class,
            ApplicationConfigurationRepository::class,
            OnboardingRepository::class,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SmartHomeApp(
                applicationModules = arrayOf(applicationModule),
            )
        }
    }
}
