package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var exampleList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupList()
    }

    private fun setupList() {
        exampleList = findViewById<View>(R.id.example_list) as RecyclerView
        exampleList.setHasFixedSize(true)
        exampleList.layoutManager = LinearLayoutManager(this)
        exampleList.adapter = ExampleAdapter(this, getExamples())
    }

    private fun getExamples(): List<ExampleActivityAndName> {
        val exampleActivityAndNames: MutableList<ExampleActivityAndName> = ArrayList()
        exampleActivityAndNames.add(
            ExampleActivityAndName(
                OperatorActivity::class.java,
                "Operator Examples"
            )
        )
        exampleActivityAndNames.add(
            ExampleActivityAndName(
                SubjectActivity::class.java,
                "Subject Examples"
            )
        )
        exampleActivityAndNames.add(
            ExampleActivityAndName(
                NetworkActivity::class.java,
                "Network Examples"
            )
        )
        exampleActivityAndNames.add(
            ExampleActivityAndName(
                SearchActivity::class.java,
                "Search Examples"
            )
        )
        return exampleActivityAndNames
    }
}
