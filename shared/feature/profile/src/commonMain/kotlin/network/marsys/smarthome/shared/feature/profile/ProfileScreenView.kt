package network.marsys.smarthome.shared.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.Res
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.profile_header
import network.marsys.smarthome.shared.library.core.SmartHomeConfig
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Shield
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state = viewModel.produceStateWithLifecycle()

    ProfileScreenViewContent(
        name = state.user,
        email = state.email,
        connectedBackend = state.connectedBackend,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreenViewContent(
    name: String,
    email: String,
    connectedBackend: String?,
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
            verticalArrangement = Arrangement
                .spacedBy(32.dp),
        ) {
            ProfileScreenHeader()
            ProfileUserInfo(
                name = name,
                email = email,
            )
            connectedBackend?.let {
                ProfileConnectedBackend(
                    connectedBackend = it,
                )
            }
        }

        DebugInfo()
    }
}

@Composable
private fun ProfileScreenHeader(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(Res.string.profile_header),
        modifier = modifier,
        lineHeight = 32.sp,
        fontSize = 24.sp,
        fontWeight = FontWeight.W700,
    )
}

@Composable
private fun ProfileUserInfo(
    name: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(24.dp),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .width(64.dp)
                    .aspectRatio(1f),
                colors = CardDefaults.colors(
                    backgroundColor = SmartHomeTheme.colors[GradientKeyToken.BrandPrimaryToSecondary],
                ),
            ) {
                Icon(
                    icon = Icons.User,
                    size = 32.dp,
                    tint = PaletteTokens.Base.White,
                )
            }

            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = name,
                    lineHeight = 28.sp,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                )

                Text(
                    text = email,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
                )
            }
        }
    }
}

@Composable
private fun ProfileConnectedBackend(
    connectedBackend: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessPrimary]),
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderSuccessPrimary],
        ),
        borderWidth = 1.dp,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .width(32.dp)
                    .aspectRatio(1f),
                colors = CardDefaults.colors(
                    backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessSecondary]),
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                Icon(
                    icon = Icons.Shield,
                    size = 16.dp,
                    tint = SmartHomeTheme.colors[ColorKeyToken.ForegroundSuccessPrimary],
                )
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = "Connected backend",
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    color = SmartHomeTheme.colors[ColorKeyToken.TextSuccessPrimary],
                )

                Text(
                    text = connectedBackend,
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    color = SmartHomeTheme.colors[ColorKeyToken.TextSuccessSecondary],
                    fontFamily = FontFamily.Monospace,
                )
            }
        }
    }
}

@Composable
private fun DebugInfo(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement
            .spacedBy(4.dp),
    ) {
        Text(
            text = "SmartHome ${SmartHomeConfig.VERSION_NAME}",
            lineHeight = 16.sp,
            fontSize = 12.sp,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ProfileScreenDemoUserPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ProfileScreenViewContent(
            name = "Demo User",
            email = "demo.user@example.com",
            connectedBackend = null,
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ProfileScreenRealUserPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ProfileScreenViewContent(
            name = "Niels Marsman",
            email = "niels.marsman@example.com",
            connectedBackend = "https://example.com",
        )
    }
}
