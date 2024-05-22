package info.firozansari.rxjava.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.firozansari.rxjava.R
import info.firozansari.rxjava.model.ExampleActivityAndName

/**
 * Adapter for mapping a set of example activities to views.
 */
class ExampleAdapter(
    private val mContext: Context,
    private val mExamples: List<ExampleActivityAndName>
) : RecyclerView.Adapter<ExampleAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
            .from(mContext)
            .inflate(R.layout.example_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mNameDisplay.text = mExamples[position].mExampleName
        holder.itemView.setOnClickListener {
            val exampleIntent = Intent(mContext, mExamples[position].mExampleActivityClass)
            mContext.startActivity(exampleIntent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mNameDisplay: TextView

        init {
            mNameDisplay = itemView.findViewById<View>(R.id.name_display) as TextView
        }
    }

    override fun getItemCount(): Int {
       return mExamples.size
    }
}
