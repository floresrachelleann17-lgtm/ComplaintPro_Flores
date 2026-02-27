package ComplaintBarangay

fun main() {

    val complaintManager = ComplaintManager()
    val officerManager = OfficerManager()


        while (true) {
            println("\n=== Barangay Complaint Management System ===")
            println("1 - Submit Complaint")
            println("2 - View Complaints")
            println("3 - Assign Officer")
            println("4 - Edit Complaint")
            println("5 - Update Status / Change Officer")
            println("6 - View Complaint History")
            println("7 - Track Complaint Status")
            println("8 - Exit")
            print("Choose option: ")

            when (readLine()?.trim()) {
                "1" -> complaintManager.submitComplaint()
                "2" -> complaintManager.viewComplaints()
                "3" -> complaintManager.assignOfficerMenu(officerManager)
                "4" -> complaintManager.editComplaint()
                "5" -> complaintManager.updateOrChangeOfficer(officerManager)
                "6" -> complaintManager.viewHistory()
                "7" -> complaintManager.trackComplaintStatus()
                "8" -> {
                    println("Program terminated.")
                    return
                }

                else -> println("Invalid choice.")
            }
        }
    }
