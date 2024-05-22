package info.firozansari.rxjava.model

data class UserDetails(
    var id: Long = 0,
    var firstname: String,
    var lastname: String,
    var homeOwner: Boolean = false,
    var carOwner: Boolean = false
)
