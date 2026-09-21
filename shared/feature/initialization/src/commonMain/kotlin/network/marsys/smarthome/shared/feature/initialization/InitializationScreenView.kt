package network.marsys.smarthome.shared.feature.initialization

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardColors
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.domain.preview.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.CircleCheck
import network.marsys.smarthome.shared.library.design.icons.CircleClose
import network.marsys.smarthome.shared.library.design.icons.Close
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.House
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LoaderCircle
import network.marsys.smarthome.shared.library.design.icons.Shield
import network.marsys.smarthome.shared.library.design.icons.Wifi
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.components.CardTokens

@Composable
fun InitializationScreenView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    InitializationScreenViewContent(
        modifier = modifier,
        content = content,
    )
}

@Composable
fun InitializationScreenViewContent(
    modifier: Modifier = Modifier,
    @Suppress("unused")
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = Breakpoints.MEDIUM.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InitializationScreenIcon(
                modifier = Modifier
                    .padding(bottom = 24.dp),
            )

            Text(
                text = "Connecting to your home...",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                lineHeight = 32.sp,
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = "https://example.com",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            )

            InitializationScreenSteps()
        }
    }
}

@Composable
internal fun InitializationScreenIcon(
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier,
    colors = CardDefaults.colors(
        backgroundColor = CardTokens.BackgroundColor,
        contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary],
    ),
) {
    Icon(
        icon = Icons.Wifi,
        modifier = Modifier
            .padding(20.dp),
        size = 40.dp,
    )
}

@Composable
private fun InitializationScreenSteps(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 48.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        InitializationScreenStep(
            title = "Connecting to backend",
            icon = Icons.Wifi,
            state = StepState.Complete,
        )

        InitializationScreenStep(
            title = "Fetching configuration",
            icon = Icons.Component,
            state = StepState.InProgress,
        )

        InitializationScreenStep(
            title = "Checking system health",
            icon = Icons.House,
            state = StepState.Failed,
        )

        InitializationScreenStep(
            title = "Checking authentication",
            icon = Icons.Shield,
            state = StepState.Idle,
        )
    }
}

@Composable
private fun InitializationScreenStep(
    title: String,
    icon: ImageVector,
    state: StepState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = stepCardColors(state = state),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IconCard(
                icon = icon,
                colors = stepIconCardColors(state = state),
            )

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = stepTitleColor(state = state),
            )

            InitializationScreenStepStateIcon(state = state)
        }
    }
}

@Composable
private fun InitializationScreenStepStateIcon(
    state: StepState,
    modifier: Modifier = Modifier,
) {
    if (state != StepState.Idle) {
        val transition = rememberInfiniteTransition("loading-indicator")

        val angle = transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 700,
                    easing = LinearEasing,
                ),
            ),
        )

        Icon(
            icon = when (state) {
                StepState.Complete -> Icons.CircleCheck
                StepState.InProgress -> Icons.LoaderCircle
                StepState.Failed -> Icons.CircleClose
            },
            modifier = modifier
                .graphicsLayer {
                    if (state == StepState.InProgress) {
                        rotationZ = angle.value
                    }
                },
            size = 20.dp,
            tint = when (state) {
                StepState.Complete -> SmartHomeTheme.colors[ColorKeyToken.ForegroundSuccessPrimary]
                StepState.InProgress -> SmartHomeTheme.colors[ColorKeyToken.ForegroundWarningPrimary]
                StepState.Failed -> SmartHomeTheme.colors[ColorKeyToken.ForegroundErrorPrimary]
            },
        )
    }
}

@Composable
private fun stepCardColors(
    state: StepState,
): CardColors = CardDefaults.colors(
    backgroundColor = when (state) {
        StepState.InProgress -> CardTokens.BackgroundColor
        else -> SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundDisabledAlternative])
    },
)

@Composable
private fun stepIconCardColors(
    state: StepState,
): CardColors = CardDefaults.colors(
    backgroundColor = SolidColor(
        value = when (state) {
            StepState.Complete -> SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessSecondary]
            StepState.InProgress -> SmartHomeTheme.colors[ColorKeyToken.BackgroundWarningSecondary]
            StepState.Idle -> SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary]
            StepState.Failed -> SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorSecondary]
        },
    ),
    contentColor = when (state) {
        StepState.Complete -> SmartHomeTheme.colors[ColorKeyToken.ForegroundSuccessPrimary]
        StepState.InProgress -> SmartHomeTheme.colors[ColorKeyToken.ForegroundWarningPrimary]
        StepState.Idle -> SmartHomeTheme.colors[ColorKeyToken.TextDisabled]
        StepState.Failed -> SmartHomeTheme.colors[ColorKeyToken.ForegroundErrorPrimary]
    },
)

@Composable
private fun stepTitleColor(
    state: StepState,
): Color = when (state) {
    StepState.Idle -> SmartHomeTheme.colors[ColorKeyToken.TextDisabled]
    StepState.Failed -> SmartHomeTheme.colors[ColorKeyToken.TextErrorPrimary]
    else -> SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
}

sealed interface StepState {
    data object Idle : StepState
    data object InProgress : StepState
    data object Complete : StepState
    data object Failed : StepState
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun InitializationScreenViewPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        InitializationScreenViewContent(
            content = {},
        )
    }
}
