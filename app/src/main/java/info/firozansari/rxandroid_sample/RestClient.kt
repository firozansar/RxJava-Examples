package info.firozansari.rxandroid_sample

import android.content.Context
import info.firozansari.rxandroid_sample.model.User
import info.firozansari.rxandroid_sample.model.UserDetails
import java.util.Locale

/**
 * This is a mock REST Client. It simulates making blocking calls to an REST endpoint.
 */
class RestClient(private val context: Context) {

    /**
     * TV Shows
     */
    val favoriteTvShows: List<String>
        get() {
            try {
                // "Simulate" the delay of network.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return createTvShowList()
        }
    val favoriteTvShowsWithException: List<String>
        get() {
            try {
                // "Simulate" the delay of network.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            throw RuntimeException("Failed to load")
        }

    private fun createTvShowList(): List<String> {
        val tvShows: MutableList<String> = ArrayList()
        tvShows.add("Rick & Morty")
        tvShows.add("The X-Files")
        tvShows.add("Star Trek: The Next Generation")
        tvShows.add("Breaking Bad")
        tvShows.add("Parks and Recreation")
        tvShows.add("House of Cards")
        tvShows.add("Game of Thrones")
        tvShows.add("The Simpsons")
        tvShows.add("Futurama")
        return tvShows
    }

    /**
     * Search Cities
     */
    fun searchForCity(searchString: String): List<String> {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return getMatchingCities(searchString)
    }

    private fun getMatchingCities(searchString: String): List<String> {
        if (searchString.isEmpty()) {
            return ArrayList()
        }
        val cities = context.resources.getStringArray(R.array.city_array)
        val toReturn: MutableList<String> = ArrayList()
        for (city in cities) {
            if (city.lowercase(Locale.getDefault())
                    .startsWith(searchString.lowercase(Locale.getDefault()))
            ) {
                toReturn.add(city)
            }
        }
        return toReturn
    }

    /**
     * Get Users
     */
    val userList: List<User>
        get() {
            val userList: MutableList<User> = ArrayList()
            val userOne = User(1, "Firoz", "Ansari", false)
            userList.add(userOne)
            val userTwo = User(2, "Rob", "Terry", true)
            userList.add(userTwo)
            val userThree = User(3, "John", "Doe", true)
            userList.add(userThree)
            return userList
        }
    val getUserDetails: UserDetails
        get() = UserDetails(1, "Firoz", "Ansari", true, true)


    fun convertUserDetailsListToUserList(userDetailsList: List<UserDetails>): List<User> {
        val userList: MutableList<User> = ArrayList()
        for (apiUser in userDetailsList) {
            val user = User(
                id = apiUser.id,
                firstname = apiUser.firstname,
                lastname = apiUser.lastname
            )
            userList.add(user)
        }
        return userList
    }

    fun cricketLoverUsers(): List<User> {
        val userList: MutableList<User> = ArrayList()
        val userOne = User(
            id = 5,
            firstname = "Rob",
            lastname = "Terry"
        )
        userList.add(userOne)
        val userTwo = User(
            id = 6,
            firstname = "Matt",
            lastname = "Carron"
        )
        userList.add(userTwo)
        val userThree = User(
            id = 1,
            firstname = "Firoz",
            lastname = "Ansari"
        )
        userList.add(userThree)
        return userList
    }

    fun footballLoverUsers(): List<User> {
        val userList: MutableList<User> = ArrayList()
        val userOne = User(
            id = 8,
            firstname = "Tom",
            lastname = "Terry"
        )
        userList.add(userOne)
        val userTwo = User(
            id = 9,
            firstname = "Harry",
            lastname = "Carron"
        )
        userList.add(userTwo)
        val userThree = User(
            id = 1,
            firstname = "Firoz",
            lastname = "Ansari"
        )
        userList.add(userThree)
        return userList
    }

    fun filterUserWhoLovesBoth(cricketFans: List<User>, footballFans: List<User>): List<User> {
        val userWhoLovesBoth: MutableList<User> = ArrayList()
        for (cricketFan in cricketFans) {
            for (footballFan in footballFans) {
                if (cricketFan.id == footballFan.id) {
                    userWhoLovesBoth.add(cricketFan)
                }
            }
        }
        return userWhoLovesBoth
    }
}
