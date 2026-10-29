package arc.window

/**
 * Window geometry sampled by the host.
 *
 * @property logicalWidth Width in cursor/window units.
 * @property logicalHeight Height in cursor/window units.
 * @property framebufferWidth Width of the drawable framebuffer in pixels.
 * @property framebufferHeight Height of the drawable framebuffer in pixels.
 * @property scaleX Horizontal content scale reported by the host.
 * @property scaleY Vertical content scale reported by the host.
 * @property focused Whether the window accepts keyboard input.
 * @property minimized Whether the host has minimized the window.
 * @property generation Monotonic geometry revision.
 */
data class WindowMetrics(
    val logicalWidth: Int,
    val logicalHeight: Int,
    val framebufferWidth: Int,
    val framebufferHeight: Int,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val focused: Boolean = true,
    val minimized: Boolean = false,
    val generation: Long = 0
)

/**
 * Physical key or pointer-button transition.
 */
enum class KeyAction {

    /**
     * Begins a held gesture.
     */
    PRESS,

    /**
     * Repeats an already held gesture.
     */
    REPEAT,

    /**
     * Ends the gesture.
     */
    RELEASE
}

/**
 * Standard host cursor shapes.
 */
enum class CursorShape {

    /**
     * Restores the platform default.
     */
    DEFAULT,

    /**
     * Indicates an actionable element.
     */
    POINTER,

    /**
     * Indicates text selection or editing.
     */
    TEXT,

    /**
     * Indicates precise positioning.
     */
    CROSSHAIR,

    /**
     * Indicates horizontal resizing.
     */
    RESIZE_HORIZONTAL,

    /**
     * Indicates vertical resizing.
     */
    RESIZE_VERTICAL
}

/**
 * Portable key symbols used for navigation and common editing shortcuts.
 */
enum class KeySymbol {

    /**
     * Focus traversal key.
     */
    TAB,

    /**
     * Confirmation key.
     */
    ENTER,

    /**
     * Space key.
     */
    SPACE,

    /**
     * Cancellation key.
     */
    ESCAPE,

    /**
     * Left arrow.
     */
    LEFT,

    /**
     * Right arrow.
     */
    RIGHT,

    /**
     * Up arrow.
     */
    UP,

    /**
     * Down arrow.
     */
    DOWN,

    /**
     * Start-of-line key.
     */
    HOME,

    /**
     * End-of-line key.
     */
    END,

    /**
     * Backward deletion key.
     */
    BACKSPACE,

    /**
     * Forward deletion key.
     */
    DELETE,

    /**
     * Letter A.
     */
    A,

    /**
     * Letter C.
     */
    C,

    /**
     * Letter V.
     */
    V,

    /**
     * Letter X.
     */
    X,

    /**
     * Letter Y.
     */
    Y,

    /**
     * Letter Z.
     */
    Z,

    /**
     * A key without a portable symbol in this set.
     */
    UNKNOWN
}

/**
 * Modifier snapshot associated with an input event.
 *
 * @property shift Shift modifier is held.
 * @property control Control modifier is held.
 * @property alt Alt modifier is held.
 * @property meta Platform meta modifier is held.
 */
data class KeyModifiers(
    val shift: Boolean = false,
    val control: Boolean = false,
    val alt: Boolean = false,
    val meta: Boolean = false
)

/**
 * An event in a window's ordered transport.
 */
interface WindowEvent {

    /**
     * Monotonic sequence assigned by the owning window.
     */
    val sequence: Long

    /**
     * Host timestamp in nanoseconds.
     */
    val timeNanos: Long

    /**
     * Native window identity associated with the event.
     */
    val windowId: Long
}

/**
 * Cursor movement in logical window units.
 *
 * @property x Horizontal cursor position.
 * @property y Vertical cursor position.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class PointerMotion(
    val x: Double,
    val y: Double,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Pointer-button transition with the cursor position at delivery.
 *
 * @property button Host physical button code.
 * @property action Button transition.
 * @property x Horizontal cursor position in logical units.
 * @property y Vertical cursor position in logical units.
 * @property modifiers Modifier snapshot.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class PointerButton(
    val button: Int,
    val action: KeyAction,
    val x: Double,
    val y: Double,
    val modifiers: KeyModifiers,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Fractional wheel movement without conversion to pixel distances.
 *
 * @property x Horizontal cursor position in logical units.
 * @property y Vertical cursor position in logical units.
 * @property deltaX Horizontal wheel delta.
 * @property deltaY Vertical wheel delta.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class PointerWheel(
    val x: Double,
    val y: Double,
    val deltaX: Double,
    val deltaY: Double,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Keyboard transition; committed text arrives separately through [UnicodeCommit].
 *
 * @property symbol Portable navigation/editing symbol.
 * @property physicalCode Host physical key code.
 * @property scanCode Platform scan code.
 * @property action Key transition.
 * @property modifiers Modifier snapshot.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class KeyboardEvent(
    val symbol: KeySymbol,
    val physicalCode: Int,
    val scanCode: Int,
    val action: KeyAction,
    val modifiers: KeyModifiers,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Text committed by the host's text-input pipeline.
 *
 * @property text Committed Unicode text.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class UnicodeCommit(
    val text: String,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Window focus transition.
 *
 * @property focused Whether the window gained focus.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class FocusEvent(
    val focused: Boolean,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * A new geometry snapshot.
 *
 * @property metrics Updated logical and framebuffer geometry.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class MetricsEvent(
    val metrics: WindowMetrics,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent

/**
 * Cursor entry or exit from the window.
 *
 * @property entered Whether the cursor entered the window.
 * @property sequence Ordered window event number.
 * @property timeNanos Host timestamp in nanoseconds.
 * @property windowId Owning native window identity.
 */
data class PointerBoundary(
    val entered: Boolean,
    override val sequence: Long,
    override val timeNanos: Long,
    override val windowId: Long
) : WindowEvent
