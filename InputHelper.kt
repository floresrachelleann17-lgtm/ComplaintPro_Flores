package ComplaintBarangay

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object InputHelper {
    fun readNonEmpty(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim() ?: ""
            if (input.isNotEmpty()) return input
            println("Input cannot be empty.")
        }
    }

    fun readContact(): String {
        while (true) {
            print("Enter contact number (11 digits)(b to back, c for menu): ")
            val input = readLine()?.trim() ?: ""

            if (input.equals("b", true) || input.equals("c", true)) return input
            if (input.matches(Regex("\\d{11}"))) return input

            println("Contact must be exactly 11 digits.")
        }
    }

    fun readYesNo(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()?.lowercase() ?: ""
            if (input in listOf("yes", "y", "no", "n", "b", "c")) return input
            println("Enter yes/no/y/n only.")
        }
    }

    fun readDate(prompt: String): String {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
        val now = LocalDateTime.now()

        while (true) {
            print(prompt)
            val input = readLine()?.trim() ?: ""

            if (input.equals("b", true) || input.equals("c", true)) return input

            if (!input.matches(Regex("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}"))) {
                println("Date and time must be in MM/DD/YYYY HH:MM format (e.g., 02/27/2026 14:30).")
                continue
            }

            try {
                val dateTime = LocalDateTime.parse(input, formatter)
                if (dateTime.isBefore(now)) {
                    println("Date and time cannot be in the past.")
                    continue
                }
                return input
            } catch (e: Exception) {
                println("Invalid date and time. Please enter a valid date and time in MM/DD/YYYY HH:MM format.")
            }
        }
    }

    fun calculateDaysAndTime(submissionDate: String): Pair<String, String> {
        return try {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
            val submitted = LocalDateTime.parse(submissionDate, formatter)
            val now = LocalDateTime.now()
            val days = ChronoUnit.DAYS.between(submitted, now)
            val date = submitted.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            Pair(days.toString(), date)
        } catch (e: Exception) {
            Pair("-", "-")
        }
    }
}