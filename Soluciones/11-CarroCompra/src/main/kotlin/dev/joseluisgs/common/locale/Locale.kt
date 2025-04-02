package dev.joseluisgs.common.locale

import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.pow

@Singleton
class LocaleService(@Property("locale") private val myLocale: String = "es-ES") : KoinComponent {
    private val locale = Locale.forLanguageTag(myLocale)
    fun getLocale() = locale
}

object LocaleFormatter : KoinComponent {
    private val localeService: LocaleService by inject()
    
    fun LocalDate.toLocalDate(): String {
        return this.format(
            DateTimeFormatter
                .ofLocalizedDate(FormatStyle.SHORT).withLocale(localeService.getLocale())
        )
    }
    
    fun LocalDateTime.toLocalDateTime(): String {
        return this.format(
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT).withLocale(localeService.getLocale())
        )
    }
    
    fun LocalTime.toLocalTime(): String {
        return this.format(
            DateTimeFormatter
                .ofLocalizedTime(FormatStyle.SHORT).withLocale(localeService.getLocale())
        )
    }
    
    fun Double.toLocalMoney(): String {
        return NumberFormat.getCurrencyInstance(localeService.getLocale()).format(this)
    }
    
    fun Double.toLocalDecimal(): String {
        return NumberFormat.getInstance(localeService.getLocale()).format(this)
    }
    
    fun Int.toLocalInteger(): String {
        return NumberFormat.getInstance(localeService.getLocale()).format(this)
    }
    
    fun Double.roundTo(decimals: Int): Double {
        return Math.round(this * 10.0.pow(decimals)) / 10.0.pow(decimals)
    }
}