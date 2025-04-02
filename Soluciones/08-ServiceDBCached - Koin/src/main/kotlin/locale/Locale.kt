package dev.joseluisgs.locale

import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.pow

@Singleton
fun provideLocale(@Property("locale") myLocale: String = Locale.getDefault().language): Locale {
    return Locale.of(myLocale)
}

fun LocalDate.toLocalDate(): String {
    return this.format(
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT).withLocale(provideLocale())
    )
}

fun Double.toLocalMoney(): String {
    return NumberFormat.getCurrencyInstance(provideLocale()).format(this)
}

fun Double.toLocalDecimal(): String {
    return NumberFormat.getInstance(provideLocale()).format(this)
}

fun Int.toLocalInteger(): String {
    return NumberFormat.getInstance(provideLocale()).format(this)
}

fun Double.roundTo(decimals: Int): Double {
    return Math.round(this * 10.0.pow(decimals)) / 10.0.pow(decimals)
}