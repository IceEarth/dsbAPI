package dsb.request

import java.text.SimpleDateFormat
import java.util.*

internal class DataUtil {
    companion object{
        fun getDate(input: String /*In this format: "19.5.2023 Freitag"*/): Date {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
            return dateFormat.parse(input.split(" ")[0])
        }

        /**
         * @param input String von folgendem Format: "3 - 5" oder "5"
         * @return gibt eine IntRange zurück
         *
         */
        fun getRange(input: String): IntRange {
            val range = input.split(" - ").map { it.trim().toInt() }

            return range[0]..(if (range.size == 2) range[1] else range[0]);
        }
    }
}