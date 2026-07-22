package network.marsys.smarthome.shared.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.ModalBottomSheetState
import com.composeunstyled.SheetDetent
import com.composeunstyled.rememberModalBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.Res
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.connected_backend
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.logout_modal_accept
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.logout_modal_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.logout_modal_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_appearance_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_appearance_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_logout_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_logout_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_notifications_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_notifications_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_reset_onboarding_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.menu_item_reset_onboarding_title
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.modal_cancel
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.profile_header
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.reset_onboarding_modal_accept
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.reset_onboarding_modal_description
import network.marsys.smarthome.shared.feature.profile.profile.generated.resources.reset_onboarding_modal_title
import network.marsys.smarthome.shared.library.core.SmartHomeConfig
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.ModalBottomSheet
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.icons.Bell
import network.marsys.smarthome.shared.library.design.icons.ChevronRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LogOut
import network.marsys.smarthome.shared.library.design.icons.Reset
import network.marsys.smarthome.shared.library.design.icons.Shield
import network.marsys.smarthome.shared.library.design.icons.SunMoon
import network.marsys.smarthome.shared.library.design.icons.TriangleAlert
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
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(
            SheetDetent.Hidden,
            ConfirmLogoutDetent,
            ResetOnboardingDetent,
        ),
    )

    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is ProfileScreenEffect.DisplayConfirmLogoutDialog ->
                modalBottomSheetState.animateTo(ConfirmLogoutDetent)

            is ProfileScreenEffect.DisplayConfirmResetOnboardingDialog ->
                modalBottomSheetState.animateTo(ResetOnboardingDetent)

            is ProfileScreenEffect.Navigate ->
                onNavigate(effect.target)
        }
    }

    ProfileScreenViewContent(
        name = state.user,
        email = state.email,
        connectedBackend = state.connectedBackend,
        onAction = viewModel.accept,
        modifier = modifier,
        modalBottomSheetState = modalBottomSheetState,
    )
}

private val ConfirmLogoutDetent = SheetDetent("ConfirmLogout") { _, height -> height }
private val ResetOnboardingDetent = SheetDetent("ResetOnboarding") { _, height -> height }

@Composable
private fun ProfileScreenViewContent(
    name: String,
    email: String,
    connectedBackend: String?,
    onAction: (ProfileScreenAction) -> Unit,
    modifier: Modifier = Modifier,
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(
            SheetDetent.Hidden,
            ConfirmLogoutDetent,
            ResetOnboardingDetent,
        ),
    ),
) {
    val blurModifier = if (modalBottomSheetState.currentDetent != SheetDetent.Hidden) {
        Modifier.blur(4.dp)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .then(blurModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = Breakpoints.MEDIUM.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
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

    ModalBottomSheet(
        state = modalBottomSheetState,
    ) {
        when (modalBottomSheetState.currentDetent) {
            ConfirmLogoutDetent -> ConfirmLogoutModalBottomSheetContent(
                modalBottomSheetState = modalBottomSheetState,
                onAction = onAction,
            )

            ResetOnboardingDetent -> ResetOnboardingModalBottomSheetContent(
                modalBottomSheetState = modalBottomSheetState,
                onAction = onAction,
            )

            else -> Unit
        }
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
            .fillMaxWidth()
            .padding(top = 32.dp),
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
            .fillMaxWidth()
            .padding(top = 32.dp),
        colors = CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessPrimary]),
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderSuccessSubtle],
        ),
        border = Border.Solid(1.dp),
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
        modifier = modifier
            .padding(top = 24.dp),
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
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

        ProfileMenuItem(
            title = stringResource(Res.string.menu_item_reset_onboarding_title),
            description = stringResource(Res.string.menu_item_reset_onboarding_description),
            icon = Icons.Reset,
            border = false,
            onClick = {
                onAction.invoke(ProfileScreenAction.ResetOnboarding)
            },
            modifier = Modifier
                .padding(top = 32.dp),
            colors = ProfileMenuItemDefaults.actionColors(
                backgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundInfoPrimary],
                titleColor = SmartHomeTheme.colors[ColorKeyToken.TextInfoPrimary],
                descriptionColor = SmartHomeTheme.colors[ColorKeyToken.TextInfoSecondary],
                iconBackgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundInfoSecondary],
                iconContentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundInfoPrimary],
            ),
        )

        ProfileMenuItem(
            title = stringResource(Res.string.menu_item_logout_title),
            description = stringResource(Res.string.menu_item_logout_description),
            icon = Icons.LogOut,
            border = false,
            onClick = {
                onAction.invoke(ProfileScreenAction.Logout)
            },
            enabled = false,
            colors = ProfileMenuItemDefaults.actionColors(
                backgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorPrimary],
                titleColor = SmartHomeTheme.colors[ColorKeyToken.TextErrorPrimary],
                descriptionColor = SmartHomeTheme.colors[ColorKeyToken.TextErrorSecondary],
                iconBackgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorSecondary],
                iconContentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundErrorPrimary],
            ),
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
    border: Boolean = true,
    colors: ProfileMenuItemColors = ProfileMenuItemDefaults.colors(),
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    right: @Composable ((Color) -> Unit)? = {
        Icon(
            icon = Icons.ChevronRight,
            size = 20.dp,
            tint = it,
        )
    },
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        colors = CardDefaults.colors(
            backgroundColor = colors.backgroundColor(enabled).value,
            borderColor = colors.titleColor(enabled).value
                .copy(alpha = .7f),
        ),
        border = when (enabled) {
            false if border -> Border.Dashed(1.dp)
            else -> Border.None
        },
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCard(
                icon = icon,
                colors = CardDefaults.colors(
                    backgroundColor = colors.iconBackgroundColor(enabled).value,
                    contentColor = colors.iconContentColor(enabled).value,
                ),
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
                    color = colors.titleColor(enabled).value,
                )

                Text(
                    text = description,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = colors.descriptionColor(enabled).value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            right?.invoke(colors.iconContentColor(enabled).value)
        }
    }
}

@Composable
private fun ResetOnboardingModalBottomSheetContent(
    modalBottomSheetState: ModalBottomSheetState,
    onAction: (ProfileScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        IconCard(
            icon = Icons.TriangleAlert,
            size = 56.dp,
            modifier = Modifier
                .padding(bottom = 16.dp),
            colors = CardDefaults.colors(
                backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundInfoPrimary]),
                contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundInfoPrimary],
            ),
        )

        Text(
            text = stringResource(Res.string.reset_onboarding_modal_title),
            style = TextDefaults.sectionHeader,
            modifier = Modifier
                .padding(bottom = 8.dp),
            color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        )

        Text(
            text = stringResource(Res.string.reset_onboarding_modal_description),
            style = TextDefaults.description,
            modifier = Modifier
                .padding(bottom = 24.dp),
        )

        ConfirmModalBottomSheetButtons(
            modalBottomSheetState = modalBottomSheetState,
        ) {
            Button(
                onClick = {
                    it.launch {
                        onAction.invoke(ProfileScreenAction.ConfirmResetOnboarding)
                        modalBottomSheetState.animateTo(SheetDetent.Hidden)
                    }
                },
                style = ButtonStyle.info(),
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.reset_onboarding_modal_accept),
                )
            }
        }
    }
}

@Composable
private fun ConfirmLogoutModalBottomSheetContent(
    modalBottomSheetState: ModalBottomSheetState,
    onAction: (ProfileScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        IconCard(
            icon = Icons.LogOut,
            size = 56.dp,
            modifier = Modifier
                .padding(bottom = 16.dp),
            colors = CardDefaults.colors(
                backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorPrimary]),
                contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundErrorPrimary],
            ),
        )

        Text(
            text = stringResource(Res.string.logout_modal_title),
            style = TextDefaults.sectionHeader,
            modifier = Modifier
                .padding(bottom = 8.dp),
            color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        )

        Text(
            text = stringResource(Res.string.logout_modal_description),
            style = TextDefaults.description,
            modifier = Modifier
                .padding(bottom = 24.dp),
        )

        ConfirmModalBottomSheetButtons(
            modalBottomSheetState = modalBottomSheetState,
        ) {
            Button(
                onClick = {
                    it.launch {
                        onAction.invoke(ProfileScreenAction.ConfirmLogout)
                        modalBottomSheetState.animateTo(SheetDetent.Hidden)
                    }
                },
                style = ButtonStyle.error(),
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.logout_modal_accept),
                )
            }
        }
    }
}

@Composable
private fun ConfirmModalBottomSheetButtons(
    modalBottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    acceptButtonContent: @Composable (CoroutineScope) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    @OptIn(ExperimentalFoundationStyleApi::class)
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    modalBottomSheetState.animateTo(SheetDetent.Hidden)
                }
            },
            style = ButtonStyle.secondary(),
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = stringResource(Res.string.modal_cancel),
            )
        }

        acceptButtonContent.invoke(coroutineScope)
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

    @Composable
    fun actionColors(
        backgroundColor: Color,
        titleColor: Color,
        descriptionColor: Color,
        iconBackgroundColor: Color,
        iconContentColor: Color,
    ): ProfileMenuItemColors = ProfileMenuItemColors(
        backgroundColor = SolidColor(backgroundColor),
        titleColor = titleColor,
        descriptionColor = descriptionColor,
        iconBackgroundColor = SolidColor(iconBackgroundColor),
        iconContentColor = iconContentColor,
        disabledBackgroundColor = SolidColor(backgroundColor.copy(alpha = .5f)),
        disabledTitleColor = titleColor.copy(alpha = .5f),
        disabledDescriptionColor = descriptionColor.copy(alpha = .5f),
        disabledIconBackgroundColor = SolidColor(iconBackgroundColor.copy(alpha = .5f)),
        disabledIconContentColor = iconContentColor.copy(alpha = .5f),
    )
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

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ProfileScreenConfirmLogoutModalPreview(
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
            modalBottomSheetState = rememberModalBottomSheetState(
                initialDetent = ConfirmLogoutDetent,
                detents = listOf(ConfirmLogoutDetent),
            )
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ProfileScreenResetOnboardingModalPreview(
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
            modalBottomSheetState = rememberModalBottomSheetState(
                initialDetent = ResetOnboardingDetent,
                detents = listOf(ResetOnboardingDetent),
            )
        )
    }
}
