package ComplaintBarangay

data class Resident(
    var name: String,
    var contact: String,
    var address: String
)

data class Complaint(
    val id: Int,
    val trackingCode: String,
    val resident: Resident,
    var description: String,
    var status: String,
    var settlementType: String?,
    var settlementDate: String?,
    var assignedOfficer: Officer?,
    val submissionDate: String  // NEW

)
data class Officer(
    val id: Int,
    val name: String,
    var isAvailable: Boolean = true
)