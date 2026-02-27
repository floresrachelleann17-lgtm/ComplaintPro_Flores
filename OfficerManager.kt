package ComplaintBarangay

class OfficerManager {
    private val officers = listOf(
        Officer(1, "Officer Reyes"),
        Officer(2, "Officer Cruz"),
        Officer(3, "Officer Santos"),
        Officer(4, "Officer Baluran"),
        Officer(5, "Officer Flores"),
        Officer(6, "Officer Sariba"),
        Officer(7, "Officer Lavador"),
        Officer(8, "Officer Ancajas")
    )

    fun displayOfficers() {
        println("\n--- Officers ---")
        officers.forEach {
            val status = if (it.isAvailable) "Available" else "Busy"
            println("${it.id} - ${it.name} ($status)")
        }
    }


    fun getOfficerById(id: Int?): Officer? {
        return officers.find { it.id == id }
    }


    fun assignOfficer(complaint: Complaint): Boolean {
        while (true) {
            displayOfficers()
            print("Choose Officer ID (b to back, c for menu): ")
            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "b" -> return false
                "c" -> return false
                else -> {
                    val id = input.toIntOrNull()
                    val officer = officers.find { it.id == id }

                    if (officer == null) {
                        println("Officer not found. Enter again.")
                        continue
                    }

                    if (!officer.isAvailable) {
                        println("Officer is busy. Choose another.")
                        continue
                    }


                    complaint.assignedOfficer?.isAvailable = true
                    officer.isAvailable = false
                    complaint.assignedOfficer = officer

                    println("Officer assigned successfully.")
                    return true
                }
            }
        }
    }
}