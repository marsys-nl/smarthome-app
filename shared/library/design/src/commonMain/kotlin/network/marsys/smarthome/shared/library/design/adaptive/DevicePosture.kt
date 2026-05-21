package network.marsys.smarthome.shared.library.design.adaptive

internal sealed interface DevicePosture {
    /**
     * Normal phone or tablet - no hinge, no fold in progress.
     */
    object Normal : DevicePosture

    /**
     * Vertical hinge - two panels side by side.
     */
    data class Book(
        val hingeWidthDp: Int = 0,
    ) : DevicePosture

    /**
     * Horizontal hinge - two panels one on top of the other.
     */
    data class TableTop(
        val hingeHeightDp: Int = 0,
    ) : DevicePosture
}
