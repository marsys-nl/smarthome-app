package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreens
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_error_empty
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_error_invalid
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_label
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_title
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_get_started
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_skip_setup
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_title
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendUriError
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextField
import network.marsys.smarthome.shared.library.design.component.TextFieldDecorationBox
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LoaderCircle
import network.marsys.smarthome.shared.library.design.icons.Server
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

@Composable
@Suppress("LongMethod")
fun ConfigurationOnboardingScreenView(
    state: ConfigurationOnboardingState,
    uriTextFieldState: TextFieldState,
    finishOnboarding: () -> Unit,
    skipToDemo: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    val numberOfScreens: Int = OnboardingScreens.SCREEN_COUNT
    val indexOfScreen: Int = OnboardingScreens.indexOf(OnboardingScreens.Configuration)

    OnboardingScreenScaffold(
        modifier = modifier
            .imePadding(),
        header = {
            OnboardingProgressIndicator(
                numberOfScreens = numberOfScreens,
                indexOfScreen = indexOfScreen,
                colors = OnboardingProgressIndicatorDefaults.colors(
                    foreground = BrandPrimaryToSecondaryGradient,
                ),
                modifier = Modifier
                    .padding(bottom = 40.dp),
            )

            OnboardingScreenIcon(
                icon = Icons.Server,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )

            Text(
                text = stringResource(Res.string.onboarding_configuration_title),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 22.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = stringResource(Res.string.onboarding_configuration_subtitle),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )
        },
        footer = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OnboardingBackButton(
                    onClick = navigateBack,
                )

                FinishOnboardingButton(
                    state = state,
                    onClick = finishOnboarding,
                )
            }

            OnboardingSkipConfigurationButton(
                onClick = skipToDemo,
                modifier = Modifier,
            )

            OnboardingScreenIndicator(
                screen = indexOfScreen,
                screens = numberOfScreens,
                modifier = Modifier
                    .padding(top = 24.dp),
                colors = OnboardingScreenIndicatorDefaults.colors(
                    activeScreenIndicatorColor = SolidColor(PaletteTokens.Emerald.Emerald500),
                    currentScreenIndicatorColor = BrandPrimaryToSecondaryGradient,
                ),
            )
        },
    ) {
        ConfigurationOnboardingScreenContent {
            Text(
                text = stringResource(Res.string.onboarding_configuration_backend_label),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )

            TextField(
                state = uriTextFieldState,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state is ConfigurationOnboardingState.Idle,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                    showKeyboardOnFocus = true,
                ),
                onKeyboardAction = {
                    if (state !is ConfigurationOnboardingState.Processing) {
                        finishOnboarding.invoke()
                    }
                }
            ) {
                TextFieldDecorationBox(
                    placeholder = {
                        Text("https://api.yourdomain.com")
                    },
                    supportingText = {
                        if (state is ConfigurationOnboardingState.Idle && state.backendUriError != null) {
                            val message = when (state.backendUriError) {
                                is BackendUriError.Empty ->
                                    stringResource(Res.string.onboarding_configuration_backend_error_empty)

                                is BackendUriError.Invalid ->
                                    stringResource(Res.string.onboarding_configuration_backend_error_invalid)
                            }

                            CompositionLocalProvider(
                                LocalContentColor provides Color.Red,
                            ) {
                                Text(text = message)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ConfigurationOnboardingScreenContent(
    modifier: Modifier = Modifier,
    textFieldContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_configuration_backend_title),
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = stringResource(Res.string.onboarding_configuration_backend_description),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )
            }

            textFieldContent.invoke()
        }
    }
}

@Composable
private fun FinishOnboardingButton(
    state: ConfigurationOnboardingState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            if (state !is ConfigurationOnboardingState.Processing) {
                onClick.invoke()
            }
        },
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.colors(
            backgroundColor = BrandPrimaryToSecondaryGradient,
            contentColor = PaletteTokens.Base.White,
        ),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state is ConfigurationOnboardingState.Processing) {
                FinishOnboardingLoadingIndicator()
            } else {
                val density = LocalDensity.current
                var textHeight by remember { mutableStateOf(0.dp) }

                Text(
                    text = stringResource(Res.string.onboarding_configuration_get_started),
                    modifier = Modifier
                        .onGloballyPositioned {
                            textHeight = with(density) {
                                it.size.height.toDp()
                            }
                        },
                    fontWeight = FontWeight.Bold,
                )

                Image(
                    imageVector = Icons.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .height(textHeight),
                    colorFilter = ColorFilter.tint(
                        color = LocalContentColor.current,
                    ),
                )
            }
        }
    }
}

@Composable
private fun FinishOnboardingLoadingIndicator() {
    val transition = rememberInfiniteTransition("loading-indicator")

    val angle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing,
            ),
        ),
    )

    Icon(
        icon = Icons.LoaderCircle,
        modifier = Modifier
            .graphicsLayer {
                rotationZ = angle.value
            },
        size = 16.dp,
    )
}

@Composable
private fun OnboardingSkipConfigurationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val skipConfigButtonStyle = if (!isPressed) {
        LocalTextStyle.current.copy(
            textDecoration = TextDecoration.Underline,
        )
    } else {
        LocalTextStyle.current
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .padding(top = 24.dp),
        colors = ButtonDefaults.colors(
            backgroundColor = SolidColor(Color.Transparent),
        ),
        indication = null,
        interactionSource = interactionSource,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_configuration_skip_setup),
            style = skipConfigButtonStyle,
            lineHeight = 20.sp,
            fontSize = 14.sp,
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ConfigurationOnboardingScreenIdlePreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Idle(),
            uriTextFieldState = rememberTextFieldState(),
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ConfigurationOnboardingScreenEmptyBackendUriPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Idle(
                backendUriError = BackendUriError.Empty,
            ),
            uriTextFieldState = rememberTextFieldState(),
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ConfigurationOnboardingScreenInvalidBackendUriPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Idle(
                backendUriError = BackendUriError.Invalid,
            ),
            uriTextFieldState = rememberTextFieldState(
                initialText = "invalid-uri",
            ),
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ConfigurationOnboardingScreenProcessingPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Processing,
            uriTextFieldState = rememberTextFieldState(),
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}
