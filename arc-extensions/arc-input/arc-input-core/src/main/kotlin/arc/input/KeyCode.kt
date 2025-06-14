package arc.input

/**
 * All keys in keyboard and mouse.
 */
enum class KeyCode(
    val keyType: KeyType,
    val id: Int,
    val char: Char? = null,
) {
    
    ANY(KeyType.UNKNOWN, -1),
    ANY_KEY(KeyType.KEY, -1),
    ANY_MOUSE(KeyType.MOUSE, -1),

    // Mouse
    MOUSE_LEFT(KeyType.MOUSE, 0),
    MOUSE_RIGHT(KeyType.MOUSE, 1),
    MOUSE_MIDDLE(KeyType.MOUSE, 2),
    MOUSE_BACK(KeyType.MOUSE, 3),
    MOUSE_FORWARD(KeyType.MOUSE, 4),
    MOUSE_SCROLL(KeyType.MOUSE, -1), // Not have ID because it processes with other method.

    // Keyboard
    KEY_ESCAPE(KeyType.KEY, 256),
    KEY_LMETA(KeyType.KEY, 343),
    KEY_RMETA(KeyType.KEY, 347),
    KEY_LCONTROL(KeyType.KEY, 341),
    KEY_RCONTROL(KeyType.KEY, 345),
    KEY_LSHIFT(KeyType.KEY, 340),
    KEY_RSHIFT(KeyType.KEY, 344),
    KEY_LMENU(KeyType.KEY, 342),
    KEY_RMENU(KeyType.KEY, 346),
    KEY_MENU(KeyType.KEY, 348),
    KEY_MINUS(KeyType.KEY, 45, '-'),
    KEY_EQUALS(KeyType.KEY, 61, '='),
    KEY_BACKSPACE(KeyType.KEY, 259),
    KEY_ENTER(KeyType.KEY, 257),
    KEY_TAB(KeyType.KEY, 258),
    KEY_LBRACKET(KeyType.KEY, 91, '['),
    KEY_RBRACKET(KeyType.KEY, 93, ']'),
    KEY_SEMICOLON(KeyType.KEY, 59, ';'),
    KEY_APOSTROPHE(KeyType.KEY, 39, '\''),
    KEY_GRAVE(KeyType.KEY, 96, '`'),
    KEY_BACKSLASH(KeyType.KEY, 92, '\\'),
    KEY_COMMA(KeyType.KEY, 44, ','),
    KEY_PERIOD(KeyType.KEY, 46, '.'),
    KEY_SLASH(KeyType.KEY, 47, '/'),
    KEY_MULTIPLY(KeyType.KEY, 332, '*'),
    KEY_SPACE(KeyType.KEY, 32, ' '),
    KEY_CAPITAL(KeyType.KEY, 280),
    KEY_LEFT(KeyType.KEY, 263),
    KEY_UP(KeyType.KEY, 265),
    KEY_RIGHT(KeyType.KEY, 262),
    KEY_DOWN(KeyType.KEY, 264),
    KEY_NUMLOCK(KeyType.KEY, 282),
    KEY_SCROLL(KeyType.KEY, 281),
    KEY_SUBTRACT(KeyType.KEY, 333, '-'),
    KEY_ADD(KeyType.KEY, 334, '+'),
    KEY_DIVIDE(KeyType.KEY, 331, '/'),
    KEY_DECIMAL(KeyType.KEY, 330, '.'),
    KEY_NUMPAD0(KeyType.KEY, 320, '0'),
    KEY_NUMPAD1(KeyType.KEY, 321, '1'),
    KEY_NUMPAD2(KeyType.KEY, 322, '2'),
    KEY_NUMPAD3(KeyType.KEY, 323, '3'),
    KEY_NUMPAD4(KeyType.KEY, 324, '4'),
    KEY_NUMPAD5(KeyType.KEY, 325, '5'),
    KEY_NUMPAD6(KeyType.KEY, 326, '6'),
    KEY_NUMPAD7(KeyType.KEY, 327, '7'),
    KEY_NUMPAD8(KeyType.KEY, 328, '8'),
    KEY_NUMPAD9(KeyType.KEY, 329, '9'),
    KEY_A(KeyType.KEY, 65, 'a'),
    KEY_B(KeyType.KEY, 66, 'b'),
    KEY_C(KeyType.KEY, 67, 'c'),
    KEY_D(KeyType.KEY, 68, 'd'),
    KEY_E(KeyType.KEY, 69, 'e'),
    KEY_F(KeyType.KEY, 70, 'f'),
    KEY_G(KeyType.KEY, 71, 'g'),
    KEY_H(KeyType.KEY, 72, 'h'),
    KEY_I(KeyType.KEY, 73, 'i'),
    KEY_J(KeyType.KEY, 74, 'j'),
    KEY_K(KeyType.KEY, 75, 'k'),
    KEY_L(KeyType.KEY, 76, 'l'),
    KEY_M(KeyType.KEY, 77, 'm'),
    KEY_N(KeyType.KEY, 78, 'n'),
    KEY_O(KeyType.KEY, 79, 'o'),
    KEY_P(KeyType.KEY, 80, 'p'),
    KEY_Q(KeyType.KEY, 81, 'q'),
    KEY_R(KeyType.KEY, 82, 'r'),
    KEY_S(KeyType.KEY, 83, 's'),
    KEY_T(KeyType.KEY, 84, 't'),
    KEY_U(KeyType.KEY, 85, 'u'),
    KEY_V(KeyType.KEY, 86, 'v'),
    KEY_W(KeyType.KEY, 87, 'w'),
    KEY_X(KeyType.KEY, 88, 'x'),
    KEY_Y(KeyType.KEY, 89, 'y'),
    KEY_Z(KeyType.KEY, 90, 'z'),
    KEY_0(KeyType.KEY, 48, '0'),
    KEY_1(KeyType.KEY, 49, '1'),
    KEY_2(KeyType.KEY, 50, '2'),
    KEY_3(KeyType.KEY, 51, '3'),
    KEY_4(KeyType.KEY, 52, '4'),
    KEY_5(KeyType.KEY, 53, '5'),
    KEY_6(KeyType.KEY, 54, '6'),
    KEY_7(KeyType.KEY, 55, '7'),
    KEY_8(KeyType.KEY, 56, '8'),
    KEY_9(KeyType.KEY, 57, '9'),
    KEY_F1(KeyType.KEY, 290),
    KEY_F2(KeyType.KEY, 291),
    KEY_F3(KeyType.KEY, 292),
    KEY_F4(KeyType.KEY, 293),
    KEY_F5(KeyType.KEY, 294),
    KEY_F6(KeyType.KEY, 295),
    KEY_F7(KeyType.KEY, 296),
    KEY_F8(KeyType.KEY, 297),
    KEY_F9(KeyType.KEY, 298),
    KEY_F10(KeyType.KEY, 299),
    KEY_F11(KeyType.KEY, 300),
    KEY_F12(KeyType.KEY, 301),
    KEY_F13(KeyType.KEY, 302),
    KEY_F14(KeyType.KEY, 303),
    KEY_F15(KeyType.KEY, 304),
    KEY_F16(KeyType.KEY, 305),
    KEY_F17(KeyType.KEY, 306),
    KEY_F18(KeyType.KEY, 307),
    KEY_F19(KeyType.KEY, 308),
    KEY_DELETE(KeyType.KEY, 261),
    KEY_HOME(KeyType.KEY, 268),
    KEY_END(KeyType.KEY, 269);

    companion object {

        /**
         * Get key-code from it integer id.
         *
         * @param id ID of key code.
         *
         * @return Some [KeyCode], fallback to [ANY] if not found.
         */
        @JvmStatic
        fun fromId(id: Int): KeyCode {
            return entries.find { it.id == id } ?: ANY
        }

        /**
         * Get all keycodes, that represents some char.
         *
         * @param char Char for finding.
         *
         * @return List of [KeyCode]'s
         */
        @JvmStatic
        fun fromChar(char: Char): List<KeyCode> {
            return entries.filter { it.char == char }
        }

    }
    
}