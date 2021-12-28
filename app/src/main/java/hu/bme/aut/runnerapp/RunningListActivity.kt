package hu.bme.aut.runnerapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ait.todorecyclerview.touch.TouchCallback
import hu.bme.aut.runnerapp.adapter.RunAdapter
import hu.bme.aut.runnerapp.data.RunData
import hu.bme.aut.runnerapp.data.RunDatabase
import hu.bme.aut.runnerapp.databinding.ActivityRunningListBinding
import kotlin.concurrent.thread

class RunningListActivity : AppCompatActivity(), RunAdapter.RunDataClickListener {

    private lateinit var binding: ActivityRunningListBinding
    private lateinit var database: RunDatabase
    private lateinit var adapter: RunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = RunDatabase.getDatabase(applicationContext)
        //if we don't start from the main activity
        if (intent.getBooleanExtra("shouldSave", false)) {
            val pace = intent.getFloatExtra("pace", 0f)
            val distance = intent.getFloatExtra("distance", 0f)
            val time = intent.getLongExtra("time", 0)
            onItemAdd(RunData(null, pace, distance, time))
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = RunAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        val touchCallbackList = TouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.rvMain)
        loadItems()
    }

    private fun loadItems() {
        thread {
            val items = database.runDataDao().getAllRunData()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemDelete(item: RunData) {
        thread {
            database.runDataDao().deleteRunData(item)
            runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }

    override fun onItemAdd(item: RunData) {
        thread {
            database.runDataDao().addRunData(item)
            runOnUiThread {
                adapter.addItem(item)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, StartActivity::class.java))
    }
}