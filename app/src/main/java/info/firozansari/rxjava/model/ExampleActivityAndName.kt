package info.firozansari.rxjava.model

import android.app.Activity

/**
 * Pair consisting of the name of an example and the activity corresponding to the example.
 */
data class ExampleActivityAndName(
    val mExampleActivityClass: Class<out Activity?>,
    val mExampleName: String
)
