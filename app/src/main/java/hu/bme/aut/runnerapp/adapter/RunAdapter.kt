package hu.bme.aut.runnerapp.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.todorecyclerview.touch.TouchHelper
import hu.bme.aut.runnerapp.R
import hu.bme.aut.runnerapp.RunningListActivity
import hu.bme.aut.runnerapp.data.DateConverter
import hu.bme.aut.runnerapp.data.RunDatabase
import hu.bme.aut.runnerapp.data.RunData
import hu.bme.aut.runnerapp.databinding.RunRowViewBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class RunAdapter(private val listener: RunDataClickListener) :
    RecyclerView.Adapter<RunAdapter.RunDataViewHolder>(), TouchHelper {
    private val items = mutableListOf<RunData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RunDataViewHolder(
        RunRowViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RunDataViewHolder, position: Int) {
        val runData = items[position]

        holder.binding.tvPace.text = holder.itemView.context.getString(R.string.pace_d, runData.pace)
        holder.binding.tvDistance.text = holder.itemView.context.getString(
            R.string.distance_f_km,
            runData.distance
        )
        holder.binding.tvTime.text = formatTime(holder, runData.time)

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(DateConverter.toDate(runData.date))
        holder.binding.tvDate.text = formattedDate

    }

    override fun onDismissed(position: Int) {
        listener.onItemDelete(items[position])
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    fun addItem(item: RunData) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(rundatas: List<RunData>) {
        items.clear()
        items.addAll(rundatas)
        notifyDataSetChanged()
    }

    fun deleteItem(item: RunData) {
        notifyItemRemoved(items.indexOf(item))
        items.remove(item)

    }

    interface RunDataClickListener {
        fun onItemDelete(item: RunData)
        fun onItemAdd(item: RunData)
    }

    fun formatTime(holder: RecyclerView.ViewHolder, time: Long): String {
        val minutes: Int = (time / 60).toInt()
        val seconds: Int = (time % 60).toInt()
        var minString : String= ""
        var secString : String= ""
        minString = if (minutes < 10) {
            "0$minutes"
        }else{
            minutes.toString()
        }
        secString = if (seconds < 10) {
            "0$seconds"
        }else{
            seconds.toString()
        }
        return holder.itemView.context.getString(R.string.time_d_d, minString, secString)
    }

    override fun getItemCount(): Int = items.size

    inner class RunDataViewHolder(val binding: RunRowViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
