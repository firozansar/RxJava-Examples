package info.firozansari.rxjava.model

data class User(
    var id: Long = 0,
    var firstname: String,
    var lastname: String,
    var isFollowing: Boolean = false
)
