@file:Suppress("TooManyFunctions")

package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_api_key_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_api_key_error_invalid
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_api_key_label
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_api_key_placeholder
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_optional
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_title
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_uri_error_empty
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_uri_error_invalid
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_uri_label
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_backend_uri_placeholder
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_get_started
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_skip_setup
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_title
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendValidationError
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
import network.marsys.smarthome.shared.library.design.component.TextFieldScope
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.ChevronDown
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LoaderCircle
import network.marsys.smarthome.shared.library.design.icons.Server
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

@Composable
@Suppress("LongMethod")
fun ConfigurationOnboardingScreenView(
    state: ConfigurationOnboardingState,
    uriTextFieldState: TextFieldState,
    apiKeyTextFieldState: TextFieldState,
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
        ConfigurationOnboardingScreenContent(
            state = state,
            uriTextFieldState = uriTextFieldState,
            apiKeyTextFieldState = apiKeyTextFieldState,
            finishOnboarding = finishOnboarding,
        )
    }
}

@Composable
private fun ConfigurationOnboardingScreenContent(
    state: ConfigurationOnboardingState,
    uriTextFieldState: TextFieldState,
    apiKeyTextFieldState: TextFieldState,
    finishOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column {
            BackendConnectionConfigurationExplainer()

            BackendConnectionUriTextField(
                state = state,
                uriTextFieldState = uriTextFieldState,
                finishOnboarding = finishOnboarding,
            )

            BackendConnectionApiKeyTextField(
                state = state,
                apiKeyTextFieldState = apiKeyTextFieldState,
                finishOnboarding = finishOnboarding,
            )
        }
    }
}

@Composable
private fun BackendConnectionConfigurationExplainer() {
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
}

@Composable
private fun ColumnScope.BackendConnectionUriTextField(
    state: ConfigurationOnboardingState,
    uriTextFieldState: TextFieldState,
    finishOnboarding: () -> Unit,
) {
    Text(
        text = stringResource(Res.string.onboarding_configuration_backend_uri_label),
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
        },
    ) {
        UriTextFieldDecorationBox(
            state = state,
        )
    }
}

@Composable
private fun TextFieldScope.UriTextFieldDecorationBox(
    state: ConfigurationOnboardingState,
    modifier: Modifier = Modifier,
) {
    TextFieldDecorationBox(
        modifier = modifier,
        placeholder = {
            Text(
                text = stringResource(
                    resource = Res.string.onboarding_configuration_backend_uri_placeholder,
                ),
            )
        },
        supportingText = {
            if (state is ConfigurationOnboardingState.Idle && state.backendValidationError != null) {
                val message = when (state.backendValidationError) {
                    is BackendValidationError.Empty ->
                        stringResource(Res.string.onboarding_configuration_backend_uri_error_empty)

                    is BackendValidationError.InvalidUri ->
                        stringResource(Res.string.onboarding_configuration_backend_uri_error_invalid)

                    else -> null
                }

                message?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides Color.Red,
                    ) {
                        Text(text = message)
                    }
                }
            }
        },
    )
}

@Composable
private fun ColumnScope.BackendConnectionApiKeyTextField(
    state: ConfigurationOnboardingState,
    apiKeyTextFieldState: TextFieldState,
    finishOnboarding: () -> Unit,
) {
    var displayApiKeyTextField by remember(state) {
        mutableStateOf(
            value = apiKeyTextFieldState.text.isNotBlank() || (
                state is ConfigurationOnboardingState.Idle &&
                    state.backendValidationError is BackendValidationError.InvalidApiKey
                ),
        )
    }

    BackendConnectionApiKeyTextFieldLabel(
        displayApiKeyTextField = displayApiKeyTextField,
        onApiKeyTextFieldLabelClick = {
            displayApiKeyTextField = it
        },
    )

    if (displayApiKeyTextField) {
        TextField(
            state = apiKeyTextFieldState,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = state is ConfigurationOnboardingState.Idle,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                showKeyboardOnFocus = true,
            ),
            onKeyboardAction = {
                if (state !is ConfigurationOnboardingState.Processing) {
                    finishOnboarding.invoke()
                }
            },
        ) {
            ApiKeyTextFieldDecorationBox(
                state = state,
            )
        }

        Text(
            text = stringResource(Res.string.onboarding_configuration_backend_api_key_description),
            modifier = Modifier
                .padding(top = 8.dp),
            lineHeight = 20.sp,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun BackendConnectionApiKeyTextFieldLabel(
    displayApiKeyTextField: Boolean,
    onApiKeyTextFieldLabelClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition = updateTransition(displayApiKeyTextField, "chevron-animation")
    val rotation by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 300)
        },
        label = "chevron-rotation-animation",
    ) {
        if (it) 0f else -90f
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onApiKeyTextFieldLabelClick.invoke(!displayApiKeyTextField)
                },
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_configuration_backend_api_key_label),
            lineHeight = 20.sp,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = stringResource(Res.string.onboarding_configuration_backend_optional),
            modifier = Modifier
                .padding(start = 8.dp),
            lineHeight = 20.sp,
            fontSize = 12.sp,
        )

        Icon(
            icon = Icons.ChevronDown,
            modifier = Modifier
                .padding(start = 8.dp)
                .graphicsLayer {
                    rotationZ = rotation
                },
            size = 10.dp,
        )
    }
}

@Composable
private fun TextFieldScope.ApiKeyTextFieldDecorationBox(
    state: ConfigurationOnboardingState,
    modifier: Modifier = Modifier,
) {
    TextFieldDecorationBox(
        modifier = modifier,
        placeholder = {
            Text(stringResource(Res.string.onboarding_configuration_backend_api_key_placeholder))
        },
        supportingText = {
            if (
                state is ConfigurationOnboardingState.Idle &&
                state.backendValidationError is BackendValidationError.InvalidApiKey
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides Color.Red,
                ) {
                    Text(
                        text = stringResource(
                            resource = Res.string.onboarding_configuration_backend_api_key_error_invalid,
                        ),
                    )
                }
            }
        },
    )
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
        tint = PaletteTokens.Base.White,
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
            apiKeyTextFieldState = rememberTextFieldState(),
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
                backendValidationError = BackendValidationError.Empty,
            ),
            uriTextFieldState = rememberTextFieldState(),
            apiKeyTextFieldState = rememberTextFieldState(),
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
                backendValidationError = BackendValidationError.InvalidUri,
            ),
            uriTextFieldState = rememberTextFieldState(
                initialText = "invalid-uri",
            ),
            apiKeyTextFieldState = rememberTextFieldState(),
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
private fun ConfigurationOnboardingScreenFillingApiKeyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Idle(),
            uriTextFieldState = rememberTextFieldState(
                initialText = "valid-uri",
            ),
            apiKeyTextFieldState = rememberTextFieldState(
                initialText = "valid-api-key",
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
private fun ConfigurationOnboardingScreenInvalidApiKeyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            state = ConfigurationOnboardingState.Idle(
                backendValidationError = BackendValidationError.InvalidApiKey,
            ),
            uriTextFieldState = rememberTextFieldState(
                initialText = "valid-uri",
            ),
            apiKeyTextFieldState = rememberTextFieldState(
                initialText = "invalid-api-key",
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
            apiKeyTextFieldState = rememberTextFieldState(),
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}
