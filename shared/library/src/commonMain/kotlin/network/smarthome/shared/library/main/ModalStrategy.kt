package network.smarthome.shared.library.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import network.marsys.smarthome.shared.library.design.component.Modal
import network.marsys.smarthome.shared.library.design.component.ModalProperties

internal data class ModalScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    internal val properties: ModalProperties,
    internal val onBack: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        Modal(
            onDismissRequest = onBack,
            content = entry::Content,
        )
    }
}

internal class ModalSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        return lastEntry.metadata[ModalKey]?.let { properties ->
            @Suppress("UNCHECKED_CAST")
            ModalScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                properties = properties,
                onBack = onBack,
            )
        }
    }

    companion object {
        fun modal(properties: ModalProperties = ModalProperties()) =
            metadata {
                put(ModalKey, properties)
            }

        object ModalKey : NavMetadataKey<ModalProperties>
    }
}
