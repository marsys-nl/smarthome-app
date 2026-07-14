package network.marsys.smarthome.shared.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.Res
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.connected_backend
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_appearance_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_appearance_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_notifications_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_notifications_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.profile_header
import network.marsys.smarthome.shared.library.core.SmartHomeConfig
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
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
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Bell
import network.marsys.smarthome.shared.library.design.icons.ChevronRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Shield
import network.marsys.smarthome.shared.library.design.icons.SunMoon
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenView(
    onNavigate: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is ProfileScreenEffect.Navigate -> onNavigate(effect.target)
        }
    }

    ProfileScreenViewContent(
        name = state.user,
        email = state.email,
        connectedBackend = state.connectedBackend,
        onAction = viewModel.accept,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreenViewContent(
    name: String,
    email: String,
    connectedBackend: String?,
    onAction: (ProfileScreenAction) -> Unit,
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
            ProfileMenuItems(
                onAction = onAction,
            )
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
            IconCard(
                icon = Icons.User,
                size = 64.dp,
                colors = CardDefaults.colors(
                    backgroundColor = SmartHomeTheme.colors[GradientKeyToken.BrandPrimaryToSecondary],
                    contentColor = PaletteTokens.Base.White,
                ),
            )

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
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderSuccessSubtle],
        ),
        borderWidth = 1.dp,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCard(
                icon = Icons.Shield,
                size = 32.dp,
                colors = CardDefaults.colors(
                    backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessSecondary]),
                    contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundSuccessPrimary],
                ),
            )

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.connected_backend),
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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

@Composable
private fun ProfileMenuItems(
    onAction: (ProfileScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        ProfileMenuItem(
            title = stringResource(Res.string.menu_item_notifications_title),
            description = stringResource(Res.string.menu_item_notifications_description),
            icon = Icons.Bell,
            enabled = false,
            onClick = {
                // No-op
            },
        )

        ProfileMenuItem(
            title = stringResource(Res.string.menu_item_appearance_title),
            description = stringResource(Res.string.menu_item_appearance_description),
            icon = Icons.SunMoon,
            onClick = {
                onAction.invoke(ProfileScreenAction.ChangeAppAppearance)
            },
        )
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ProfileMenuItemColors = ProfileMenuItemDefaults.colors(),
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    right: @Composable (() -> Unit)? = {
        Icon(
            icon = Icons.ChevronRight,
            size = 20.dp,
            tint = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )
    },
) {
    val cardColors = CardDefaults.colors(
        backgroundColor = colors.backgroundColor(enabled).value,
    )

    val iconColors = CardDefaults.colors(
        backgroundColor = colors.iconBackgroundColor(enabled).value,
        contentColor = colors.iconContentColor(enabled).value,
    )

    val titleColor = colors.titleColor(enabled).value
    val descriptionColor = colors.descriptionColor(enabled).value

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        colors = cardColors,
        borderWidth = 1.dp,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCard(
                icon = icon,
                colors = iconColors,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = titleColor,
                )

                Text(
                    text = description,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = descriptionColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            right?.invoke()
        }
    }
}

@Immutable
data class ProfileMenuItemColors(
    private val backgroundColor: Brush,
    private val titleColor: Color,
    private val descriptionColor: Color,
    private val iconBackgroundColor: Brush,
    private val iconContentColor: Color,
    private val disabledBackgroundColor: Brush,
    private val disabledTitleColor: Color,
    private val disabledDescriptionColor: Color,
    private val disabledIconBackgroundColor: Brush,
    private val disabledIconContentColor: Color,
) {
    @Composable
    internal fun backgroundColor(enabled: Boolean): State<Brush> =
        rememberUpdatedState(
            if (enabled) backgroundColor else disabledBackgroundColor,
        )

    @Composable
    internal fun titleColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) titleColor else disabledTitleColor,
        )

    @Composable
    internal fun descriptionColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) descriptionColor else disabledDescriptionColor,
        )

    @Composable
    internal fun iconBackgroundColor(enabled: Boolean): State<Brush> =
        rememberUpdatedState(
            if (enabled) iconBackgroundColor else disabledIconBackgroundColor,
        )

    @Composable
    internal fun iconContentColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) iconContentColor else disabledIconContentColor,
        )
}

object ProfileMenuItemDefaults {
    @Composable
    fun colors(
        backgroundColor: Brush = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondary]),
        titleColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        descriptionColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        iconBackgroundColor: Brush = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryDisabled]),
        iconContentColor: Color = SmartHomeTheme.colors[ColorKeyToken.ForegroundSecondary],
        disabledBackgroundColor: Brush = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundDisabledAlternative]),
        disabledTitleColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextDisabled],
        disabledDescriptionColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextDisabled],
        disabledIconBackgroundColor: Brush =
            SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryAlternative]),
        disabledIconContentColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextDisabled],
    ): ProfileMenuItemColors = ProfileMenuItemColors(
        backgroundColor = backgroundColor,
        titleColor = titleColor,
        descriptionColor = descriptionColor,
        iconBackgroundColor = iconBackgroundColor,
        iconContentColor = iconContentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledTitleColor = disabledTitleColor,
        disabledDescriptionColor = disabledDescriptionColor,
        disabledIconBackgroundColor = disabledIconBackgroundColor,
        disabledIconContentColor = disabledIconContentColor,
    )
}

@Composable
private fun IconCard(
    icon: ImageVector,
    colors: CardColors,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    Card(
        modifier = modifier
            .width(size)
            .aspectRatio(1f),
        colors = colors,
        shape = RoundedCornerShape(size / ICON_CARD_CORNER_DIVISOR),
        contentPadding = PaddingValues(size / ICON_CARD_PADDING_DIVISOR),
    ) {
        Icon(
            icon = icon,
            size = size / ICON_CARD_ICON_SIZE_DIVISOR,
        )
    }
}

private const val ICON_CARD_CORNER_DIVISOR = 4
private const val ICON_CARD_PADDING_DIVISOR = 4
private const val ICON_CARD_ICON_SIZE_DIVISOR = 2

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
            onAction = {},
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
            onAction = {},
        )
    }
}
