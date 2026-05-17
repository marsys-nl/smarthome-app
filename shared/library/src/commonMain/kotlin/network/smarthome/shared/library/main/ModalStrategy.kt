package network.smarthome.shared.library.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

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
        CompositionLocalProvider(
            LocalModalDismissal provides onBack,
        ) {
            ModalOverlay(
                content = entry::Content,
            )
        }
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

@Immutable
internal data class ModalProperties(
    val overlayColor: Color = Color.Black.copy(alpha = 0.5f),
    val overlayPadding: PaddingValues = PaddingValues(16.dp),
)

internal val LocalModalDismissal = compositionLocalOf<() -> Unit> {
    error("No modal dismissal lambda provided")
}

@Composable
private fun ModalScene<*>.ModalOverlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onBack,
            )
            .background(properties.overlayColor)
            .padding(properties.overlayPadding),
        contentAlignment = Alignment.Center,
        content = { content.invoke() },
    )
}
