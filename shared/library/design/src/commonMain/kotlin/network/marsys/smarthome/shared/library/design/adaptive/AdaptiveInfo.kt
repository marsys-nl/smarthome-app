package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.runtime.Composable

internal data class AdaptiveInfo(
    val windowInfo: WindowInfo,
    val posture: DevicePosture,
)

@Composable
internal expect fun rememberAdaptiveInfo(): AdaptiveInfo

internal fun AdaptiveInfo.layoutMode(policy: PanePolicy): LayoutMode =
    decideLayoutMode(
        widthDp = windowInfo.widthDp,
        heightDp = windowInfo.heightDp,
        widthClass = windowInfo.widthClass,
        posture = posture,
        policy = policy,
    )

private fun decideLayoutMode(
    widthDp: Int,
    heightDp: Int,
    widthClass: WindowWidthClass,
    posture: DevicePosture,
    policy: PanePolicy,
): LayoutMode = when {
    policy is PanePolicy.NeverSplit ->
        LayoutMode.SinglePane

    policy is PanePolicy.ForceSplit ->
        LayoutMode.SplitPane

    posture !is DevicePosture.Normal ->
        LayoutMode.SinglePane

    policy is PanePolicy.AllowSplit && widthDp < policy.minimumWidthDp ->
        LayoutMode.SinglePane

    else ->
        when (widthClass) {
            WindowWidthClass.Compact ->
                LayoutMode.SinglePane

            WindowWidthClass.Medium if widthDp > heightDp ->
                LayoutMode.SplitPane

            WindowWidthClass.Medium ->
                LayoutMode.SinglePane

            WindowWidthClass.Expanded ->
                LayoutMode.SplitPane
        }
}
