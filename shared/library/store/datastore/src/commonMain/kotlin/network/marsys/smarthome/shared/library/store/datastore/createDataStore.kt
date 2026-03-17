@file:Suppress("ktlint:standard:filename")

package network.marsys.smarthome.shared.library.store.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath.invoke().toPath() },
    )

internal const val DATASTORE_FILE_NAME = "network.marsys.smarthome.preferences_pb"
