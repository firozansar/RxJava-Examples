package info.firozansari.rxandroid_sample.model

data class User(
    var id: Long = 0,
    var firstname: String,
    var lastname: String,
    var isFollowing: Boolean = false
)
