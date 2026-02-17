package network.marsys.smarthome.shared.feature.onboarding

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.ArrowRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.painterResource

private val OnboardingBackgroundColor = Color(color = 0xFFF1BF42)

private val OnboardingInitialScreenButtonBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingInitialScreenButtonTextColor = PaletteTokens.Base.White
private val OnboardingInitialScreenProgressIndicatorBackgroundColor = PaletteTokens.Amber.Amber600
private val OnboardingInitialScreenProgressIndicatorForegroundColor = PaletteTokens.Slate.Slate800

@Composable
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
) {
    SmartHomeTheme(
        scheme = ColorScheme.lightColorScheme,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OnboardingBackgroundColor),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .padding(40.dp)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingLoadingIndicator(
                    progress = .2f,
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                )

                Text(
                    text = "Welcome to your SmartHome",
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W700,
                )

                Text(
                    text = "Control all your devices from one beautiful app",
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )

                OnboardingNextButton {
                    // NO-OP
                }
            }

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "SmartHome logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(108.dp),
            )
        }
    }
}

@Composable
private fun OnboardingLoadingIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(
                shape = CircleShape,
                color = OnboardingInitialScreenProgressIndicatorBackgroundColor
                    .copy(alpha = .3f),
            )
            .clip(CircleShape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(OnboardingInitialScreenProgressIndicatorForegroundColor),
        )
    }
}

@Composable
private fun OnboardingNextButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.colors(
            backgroundColor = OnboardingInitialScreenButtonBackgroundColor,
            contentColor = OnboardingInitialScreenButtonTextColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val density = LocalDensity.current
            var textHeight by remember { mutableStateOf(0.dp) }

            Text(
                text = "Next",
                modifier = Modifier
                    .onGloballyPositioned {
                        textHeight = with(density) {
                            it.size.height.toDp()
                        }
                    },
                fontWeight = FontWeight.Bold,
            )

            Image(
                imageVector = Icons.ArrowRight,
                contentDescription = "Next button icon",
                modifier = Modifier
                    .height(textHeight),
                colorFilter = ColorFilter.tint(
                    color = OnboardingInitialScreenButtonTextColor,
                ),
            )
        }
    }
}

@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
