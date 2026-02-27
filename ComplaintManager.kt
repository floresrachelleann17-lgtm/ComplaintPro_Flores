package ComplaintBarangay

class ComplaintManager {
    private val complaints = mutableListOf<Complaint>()
    private val history = mutableListOf<Complaint>()
    private var nextId = 1

    fun submitComplaint() {
        var step = 0
        var name = ""
        var contact = ""
        var address = ""
        var description = ""
        var settlementType: String? = null
        var settlementDate: String? = null

        while (true) {
            when (step) {
                0 -> {
                    val r = InputHelper.readNonEmpty("Enter resident name (c for menu): ")
                    if (r == "c") return
                    name = r; step++
                }

                1 -> {
                    val r = InputHelper.readContact()
                    when (r.lowercase()) {
                        "b" -> step--
                        "c" -> return
                        else -> {
                            contact = r; step++
                        }
                    }
                }

                2 -> {
                    val r = InputHelper.readNonEmpty("Enter address (b to back, c for menu): ")
                    when (r.lowercase()) {
                        "b" -> step--
                        "c" -> return
                        else -> {
                            address = r; step++
                        }
                    }
                }

                3 -> {
                    val r = InputHelper.readNonEmpty("Enter description for complaint (b to back, c for menu): ")
                    when (r.lowercase()) {
                        "b" -> step--
                        "c" -> return
                        else -> {
                            description = r; step++
                        }
                    }
                }

                4 -> {
                    when (InputHelper.readYesNo("Is this complaint serious? (yes/no)/(b to back, c for menu): ")) {
                        "b" -> step--
                        "c" -> return
                        "yes", "y" -> {
                            settlementType = "Hearing"
                            // In the hearing date prompt:
                            val d =
                                InputHelper.readDate("Enter hearing date (MM/dd/yyyy HH:mm)(b to back, m for menu): ")
                            when (d.lowercase()) {
                                "b" -> {

                                }

                                "c" -> return
                                else -> {
                                    settlementDate = d
                                    saveComplaint(name, contact, address, description, settlementType, settlementDate)
                                    return
                                }
                            }
                        }

                        "no", "n" -> step++
                    }
                }

                5 -> {
                    when (InputHelper.readYesNo("Can this be resolved without settlement? (yes/no)/(b to back, c for menu): ")) {
                        "b" -> step--
                        "c" -> return
                        "yes", "y" -> {
                            saveComplaint(name, contact, address, description, null, null)
                            return
                        }

                        "no", "n" -> {
                            settlementType = "Amicable Settlement"
                            val d =
                                InputHelper.readDate("Enter settlement date (MM/dd/yyyy HH:mm)(b to back, m for menu): ")
                            when (d.lowercase()) {
                                "b" -> {

                                }

                                "c" -> return
                                else -> {
                                    settlementDate = d
                                    saveComplaint(name, contact, address, description, settlementType, settlementDate)
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveComplaint(
        name: String,
        contact: String,
        address: String,
        description: String,
        settlementType: String?,
        settlementDate: String?
    ) {
        val trackingCode = "CMP-" + (100000..999999).random() + "-" + java.time.LocalDate.now().year
        val now = java.time.LocalDateTime.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
        val submissionDate = now.format(formatter)

        complaints.add(
            Complaint(
                nextId++,
                trackingCode,
                Resident(name, contact, address),
                description,
                "Pending",
                settlementType,
                settlementDate,
                null,
                submissionDate
            )
        )
        println("Complaint submitted successfully.")
        println("Your tracking code is: $trackingCode")
        println("Please save this code to track your complaint.")
    }

    fun viewComplaints() {
        if (complaints.isEmpty()) {
            println("No active complaints.")
            return
        }

        println("1 - Pending")
        println("2 - In Progress")
        println("3 - All Active")
        print("Choose (c to menu): ")

        when (readLine()?.trim()) {
            "1" -> {
                val pending = complaints.filter { it.status == "Pending" }
                if (pending.isEmpty()) {
                    println("No pending complaints.")
                } else {
                    displayComplaints(pending)
                }
            }

            "2" -> {
                val inProgress = complaints.filter { it.status == "In Progress" }
                if (inProgress.isEmpty()) {
                    println("No in progress complaints.")
                } else {
                    displayComplaints(inProgress)
                }
            }

            "3" -> {
                if (complaints.isEmpty()) {
                    println("No active complaints.")
                } else {
                    displayComplaints(complaints)
                }
            }

            "c" -> return
            else -> println("Invalid choice.")
        }
    }

    fun updateOrChangeOfficer(officerManager: OfficerManager) {
        if (complaints.isEmpty()) {
            println("No active complaints.")
            return
        }

        displayComplaints(complaints)

        val req = getComplaint("Enter Complaint ID (b to back, c for menu):") ?: return


        while (true) {
            println("1 - Update Status")
            println("2 - Change Officer")
            print("Choose (b to back, c for menu): ")

            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "1" -> {
                    updateStatus(req)
                    break
                }

                "2" -> {
                    if (req.assignedOfficer == null) {
                        println("No officer assigned to this complaint. Use 'Assign Officer' from the main menu to assign one first.")
                        continue  // Loop back to submenu
                    }
                    officerManager.assignOfficer(req)
                    break
                }

                "b" -> {

                    updateOrChangeOfficer(officerManager)
                    return
                }

                "c" -> {

                    return
                }

                else -> println("Invalid choice. Please enter 1 or 2 (or b/c).")
            }
        }
    }


    private fun getComplaint(prompt: String): Complaint? {
        while (true) {
            print(prompt + " ")
            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "b" -> return null
                "c" -> return null
                else -> {
                    val id = input.toIntOrNull()
                    val req = complaints.find { it.id == id }
                    if (req != null) return req
                    println("Complaint not found. Please enter an existing ID number.")
                }
            }
        }
    }

    private fun updateStatus(req: Complaint) {
        while (true) {
            println("1 - Pending")
            println("2 - In Progress")
            println("3 - Resolved")
            print("Choose (b to back, c for menu): ")

            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "1" -> {
                    if (req.status == "Pending") {
                        println("Status is already Pending.")
                        continue
                    }
                    req.status = "Pending"
                    println("Status updated to Pending.")
                    return
                }

                "2" -> {
                    if (req.status == "In Progress") {
                        println("Status is already In Progress.")
                        continue
                    }
                    req.status = "In Progress"
                    println("Status updated to In Progress.")
                    return
                }

                "3" -> {
                    req.status = "Resolved"
                    req.assignedOfficer?.isAvailable = true
                    complaints.remove(req)
                    history.add(req)
                    println("Complaint resolved.")
                    return
                }

                "b" -> {

                    return
                }

                "c" -> {

                    return
                }

                else -> println("Invalid choice. Please enter 1, 2, or 3 (or b/c).")
            }
        }
    }

    fun assignOfficerMenu(officerManager: OfficerManager) {
        if (complaints.isEmpty()) {
            println("No active complaints.")
            return
        }

        while (true) {

            displayComplaints(complaints)
            print("Enter Complaint ID (b to back, c for menu): ")
            val input = readLine()?.trim()?.lowercase() ?: continue

            val complaint = when (input) {
                "b" -> break
                "c" -> return
                else -> {
                    val id = input.toIntOrNull()
                    if (id == null) {
                        println("Invalid number. Enter again.")
                        continue
                    }
                    val found = complaints.find { it.id == id }
                    if (found == null) {
                        println("Complaint not found. Enter again.")
                        continue
                    }
                    found
                }
            }


            if (complaint.assignedOfficer != null) {
                println("This complaint already has an assigned officer. Use 'Update Status / Change Officer' to change it.")
                continue
            }


            while (true) {
                officerManager.displayOfficers()
                print("Choose Officer ID (b to back, c for menu): ")
                val officerInput = readLine()?.trim()?.lowercase() ?: continue

                when (officerInput) {
                    "b" -> break
                    "c" -> return
                    else -> {
                        val officerId = officerInput.toIntOrNull()
                        val officer = officerManager.getOfficerById(officerId)

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
                        return
                    }
                }
            }
        }
    }

    fun editComplaint() {
        if (complaints.isEmpty()) {
            println("No active complaints to edit.")
            return
        }

        displayComplaints(complaints)


        val req = getComplaint("Enter Complaint ID to edit (b to back, c for menu):") ?: return

        if (req.status == "Resolved") {
            println("Cannot edit resolved complaints. View history instead.")
            return
        }


        while (true) {
            println("\nWhat would you like to edit?")
            println("1 - Name")
            println("2 - Contact")
            println("3 - Address")
            println("4 - Description")
            println("5 - Settlement Options")
            println("6 - Cancel Edit")
            print("Choose (b to back, c for menu): ")

            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "1" -> {
                    val newName = InputHelper.readNonEmpty("Enter new resident name (c for menu): ")
                    if (newName == "c") return
                    req.resident.name = newName
                    println("Name updated successfully.")
                }

                "2" -> {
                    val newContact = InputHelper.readContact()
                    when (newContact.lowercase()) {
                        "b" -> continue  // Stay in submenu
                        "c" -> return
                        else -> {
                            req.resident.contact = newContact
                            println("Contact updated successfully.")
                        }
                    }
                }

                "3" -> {
                    val newAddress = InputHelper.readNonEmpty("Enter new address (b to back, c for menu): ")
                    when (newAddress.lowercase()) {
                        "b" -> continue
                        "c" -> return
                        else -> {
                            req.resident.address = newAddress
                            println("Address updated successfully.")
                        }
                    }
                }

                "4" -> {
                    val newDescription = InputHelper.readNonEmpty("Enter new description (b to back, c for menu): ")
                    when (newDescription.lowercase()) {
                        "b" -> continue
                        "c" -> return
                        else -> {
                            req.description = newDescription
                            println("Description updated successfully.")
                        }
                    }
                }

                "5" -> editSettlement(req)
                "6" -> return
                "b" -> return
                "c" -> return
                else -> println("Invalid choice. Please enter 1-6 (or b/c).")
            }
        }
    }

    private fun editSettlement(req: Complaint) {
        while (true) {
            println("\nCurrent Settlement: ${req.settlementType ?: "None"} (${req.settlementDate ?: "No date"})")
            println("1 - Change to Hearing")
            println("2 - Change to Amicable Settlement")
            println("3 - Remove Settlement (No settlement)")
            println("4 - Cancel")
            print("Choose (b to back, c for menu): ")

            val input = readLine()?.trim()?.lowercase() ?: continue

            when (input) {
                "1" -> {
                    val newDateTime =
                        InputHelper.readDate("Enter new hearing date and time (MM/dd/yyyy HH:mm)(b to back, c for menu): ")
                    when (newDateTime.lowercase()) {
                        "b" -> continue
                        "c" -> return
                        else -> {
                            req.settlementType = "Hearing"
                            req.settlementDate = newDateTime
                            println("Settlement updated to Hearing with new date and time.")
                            return
                        }
                    }
                }

                "2" -> {
                    val newDateTime =
                        InputHelper.readDate("Enter new settlement date and time (MM/dd/yyyy HH:mm)(b to back, c for menu): ")
                    when (newDateTime.lowercase()) {
                        "b" -> continue
                        "c" -> return
                        else -> {
                            req.settlementType = "Amicable Settlement"
                            req.settlementDate = newDateTime
                            println("Settlement updated to Amicable Settlement with new date and time.")
                            return
                        }
                    }
                }

                "3" -> {
                    req.settlementType = null
                    req.settlementDate = null
                    println("Settlement removed.")
                    return
                }

                "4" -> return
                "b" -> return
                "c" -> return
                else -> println("Invalid choice. Please enter 1-4 (or b/c).")
            }
        }
    }

    fun viewHistory() {
        if (history.isEmpty()) {
            println("No complaint history.")
            return
        }
        displayComplaints(history)
    }

    private fun displayComplaints(list: List<Complaint>) {
        println(
            String.format(
                "%-5s %-30s %-12s %-30s %-40s %-12s %-6s %-20s %-20s %-20s %-15s",
                "ID", "Name", "Contact", "Address", "Description", "Status", "Days", "Submitted", "Settlement", "Settlement Date", "Officer"
            )
        )
        println("-".repeat(230))

        for (c in list) {
            // Call from InputHelper
            val daysAndTime = InputHelper.calculateDaysAndTime(c.submissionDate)

            val nameLines = wrapText(c.resident.name, 30)
            val addressLines = wrapText(c.resident.address, 30)
            val descLines = wrapText(c.description, 40)
            val maxLines = maxOf(nameLines.size, addressLines.size, descLines.size)

            for (i in 0 until maxLines) {
                println(
                    String.format(
                        "%-5s %-30s %-12s %-30s %-40s %-12s %-6s %-20s %-20s %-20s %-15s",
                        if (i == 0) c.id.toString() else "",
                        if (i < nameLines.size) nameLines[i] else "",
                        if (i == 0) c.resident.contact else "",
                        if (i < addressLines.size) addressLines[i] else "",
                        if (i < descLines.size) descLines[i] else "",
                        if (i == 0) c.status else "",
                        if (i == 0) daysAndTime.first else "",
                        if (i == 0) daysAndTime.second else "",
                        if (i == 0) c.settlementType ?: "-" else "",
                        if (i == 0) c.settlementDate ?: "-" else "",
                        if (i == 0) c.assignedOfficer?.name ?: "-" else ""
                    )
                )
            }
            println("-".repeat(230))
        }
    }

    fun trackComplaintStatus() {
        if (complaints.isEmpty()) {
            println("No active complaints to track.")
            return
        }

        while (true) {
            println("\n--- Track Complaint Status ---")
            println("1 - Search by Complaint ID")
            println("2 - Search by Contact Number")
            println("3 - Search by Tracking Code")
            println("4 - Search by Resident Name")
            print("Choose (c to menu): ")

            when (readLine()?.trim()?.lowercase()) {
                "1" -> {
                    print("Enter Complaint ID: ")
                    val id = readLine()?.trim()?.toIntOrNull()
                    val complaint = complaints.find { it.id == id }
                    if (complaint != null) {
                        displayComplaints(listOf(complaint))
                    } else {
                        println("Complaint not found.")
                    }
                }
                "2" -> {
                    print("Enter Contact Number: ")
                    val contact = readLine()?.trim() ?: ""
                    val found = complaints.filter { it.resident.contact == contact }
                    if (found.isNotEmpty()) {
                        displayComplaints(found)
                    } else {
                        println("No complaints found for this contact number.")
                    }
                }
                "3" -> {
                    print("Enter Tracking Code: ")
                    val code = readLine()?.trim() ?: ""
                    val found = complaints.find { it.trackingCode == code }
                    if (found != null) {
                        displayComplaints(listOf(found))
                    } else {
                        println("Complaint not found.")
                    }
                }
                "4" -> {
                    print("Enter Resident Name: ")
                    val name = readLine()?.trim() ?: ""
                    if (name.isEmpty()) {
                        println("Name cannot be empty.")
                        continue
                    }
                    val found = complaints.filter {
                        it.resident.name.contains(name, ignoreCase = true)
                    }
                    if (found.isNotEmpty()) {
                        displayComplaints(found)
                    } else {
                        println("No complaints found for resident name: $name")
                    }
                }
                "c" -> return
                else -> {
                    println("Invalid choice. Please enter 1, 2, 3, or 4.")
                    continue
                }
            }
        }
    }

    private fun wrapText(text: String, width: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var line = ""
        for (word in words) {
            if ((line + word).length > width) {
                lines.add(line)
                line = "$word "
            } else {
                line += "$word "
            }
        }
        if (line.isNotEmpty()) lines.add(line)
        return lines
    }
}