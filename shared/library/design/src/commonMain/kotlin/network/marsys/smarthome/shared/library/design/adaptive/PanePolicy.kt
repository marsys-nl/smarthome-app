package network.marsys.smarthome.shared.library.design.adaptive

sealed interface PanePolicy {
    data class AllowSplit(
        val minimumWidthDp: Int = Breakpoints.MEDIUM,
    ) : PanePolicy

    data object NeverSplit : PanePolicy
    data object ForceSplit : PanePolicy
}
