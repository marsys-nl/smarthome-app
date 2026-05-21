package network.marsys.smarthome.shared.library.design.adaptive

internal sealed interface LayoutMode {
    data object SinglePane : LayoutMode
    data object SplitPane : LayoutMode
}
