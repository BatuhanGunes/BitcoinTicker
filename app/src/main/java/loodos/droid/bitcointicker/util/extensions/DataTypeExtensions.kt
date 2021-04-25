package loodos.droid.bitcointicker.util.extensions

import java.text.DecimalFormat

fun String?.emptyIfNull(): String {
    return this ?: ""
}

fun String?.trimParanthesis(): String {
    return this?.replace(Regex("[()]"), "") ?: ""
}

fun Double?.dollarString(): String {
    return this?.let {
        val numberFormat = DecimalFormat("#,##0.00")
        "US$ ${numberFormat.format(this)}"
    } ?: ""
}