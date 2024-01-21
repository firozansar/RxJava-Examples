package info.firozansari.rxandroid_sample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter used to map a String to a text view.
 */
class SimpleAdapter(private val mContext: Context) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder?>() {
    private val mStrings: MutableList<String> = ArrayList()
    fun setStrings(newStrings: List<String>) {
        mStrings.clear()
        mStrings.addAll(newStrings)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.string_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mStrings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mColorDisplay.text = mStrings[position]
        holder.itemView.setOnClickListener {
            Toast.makeText(
                mContext,
                mStrings[position],
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mColorDisplay: TextView

        init {
            mColorDisplay = view.findViewById<View>(R.id.color_display) as TextView
        }
    }
}
