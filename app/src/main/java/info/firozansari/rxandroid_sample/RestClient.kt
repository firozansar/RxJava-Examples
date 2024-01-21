package info.firozansari.rxandroid_sample

import android.content.Context
import java.util.Locale

/**
 * This is a mock REST Client. It simulates making blocking calls to an REST endpoint.
 */
class RestClient(private val mContext: Context) {
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
        tvShows.add("The Joy of Painting")
        tvShows.add("The Simpsons")
        tvShows.add("Futurama")
        tvShows.add("Rick & Morty")
        tvShows.add("The X-Files")
        tvShows.add("Star Trek: The Next Generation")
        tvShows.add("Archer")
        tvShows.add("30 Rock")
        tvShows.add("Bob's Burgers")
        tvShows.add("Breaking Bad")
        tvShows.add("Parks and Recreation")
        tvShows.add("House of Cards")
        tvShows.add("Game of Thrones")
        tvShows.add("Law And Order")
        return tvShows
    }

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
        val cities = mContext.resources.getStringArray(R.array.planets_array)
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
}
