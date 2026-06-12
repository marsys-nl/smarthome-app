package network.smarthome.shared.library

import androidx.compose.ui.window.ComposeUIViewController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import network.marsys.smarthome.shared.library.store.datastore.createDataStore
import org.koin.dsl.module

val applicationModule = module {
    single<DataStore<Preferences>> {
        createDataStore()
    }
}

@Suppress("unused")
fun mainViewController() = ComposeUIViewController {
    SmartHomeApp(
        applicationModule = applicationModule,
    )
}
