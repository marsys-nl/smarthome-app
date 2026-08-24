package network.marsys.smarthome.shared.feature.zone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.IconButton
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.IconOnlyButton
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.icons.ChevronLeft
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ZoneScreenView(
    zone: EntityIdentifier,
    onNavigate: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ZoneViewModel>(
        key = "zone-screen-view-$zone",
    ) {
        parametersOf(zone)
    }

    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is ZoneScreenEffect.Navigate -> onNavigate(effect.target)
        }
    }

    ZoneScreenViewContent(
        state = state,
        onAction = viewModel.accept,
        modifier = modifier,
    )
}

@Composable
private fun ZoneScreenViewContent(
    state: ZoneScreenState,
    onAction: (ZoneScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = Breakpoints.MEDIUM.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            ZoneScreenHeader(
                state = state,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun ZoneScreenHeader(
    state: ZoneScreenState,
    onAction: (ZoneScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconOnlyButton(
            icon = Icons.ChevronLeft,
            onClick = {
                onAction.invoke(ZoneScreenAction.NavigateToZones)
            },
            modifier = Modifier
                .padding(end = 16.dp),
            foregroundColor = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            style = ButtonStyle.secondary(),
        )

        Column(
            modifier = Modifier,
        ) {
            Text(
                text = stringResource(state.zone),
                style = TextDefaults.title then TextStyles.bold,
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )

            Text(
                text = "5 entities",
                style = TextDefaults.description,
            )
        }
    }
}
