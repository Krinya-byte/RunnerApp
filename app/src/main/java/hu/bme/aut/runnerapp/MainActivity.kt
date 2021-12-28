package hu.bme.aut.runnerapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.runnerapp.data.LocationData
import hu.bme.aut.runnerapp.data.RunData
import hu.bme.aut.runnerapp.databinding.ActivityMainBinding
import hu.bme.aut.runnerapp.fragments.ActivityChangeFragment
import hu.bme.aut.runnerapp.location.MainLocationManager

class MainActivity : AppCompatActivity(), MainLocationManager.OnNewLocationAvailable,
    ActivityChangeFragment.SaveRunDataListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainLocationManager: MainLocationManager
    private var start: Long = 0
    private var difference: Long = 0
    private var locationDataList = mutableListOf<LocationData>()
    private var subtime: Long = 0
    private var locationData = LocationData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mainLocationManager = MainLocationManager(this, this)

        binding.chronometer.setBase(SystemClock.elapsedRealtime())

        requestNeededPermission()
        binding.btnStop.setOnClickListener {
            ActivityChangeFragment().show(
                supportFragmentManager,
                ActivityChangeFragment.TAG
            )
        }

        binding.togglePause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onLocationStop()
            } else {
                handleLocationStart()
            }
        }
    }

    fun onLocationStop() {
        mainLocationManager.stopLocationMonitoring()
        setRunDataView(locationData, true)
        subtime = SystemClock.elapsedRealtime() - binding.chronometer.base
        binding.chronometer.stop()
    }

    private fun GetRunData(): RunData {
        var sumPace: Float = 0.0F
        val time: Long = (SystemClock.elapsedRealtime() - binding.chronometer.base) / 1000
        var finalDistance: Float = locationData.distance
        locationDataList.forEach {
            sumPace += locationData.speed
        }
        val avgPace = (sumPace / locationDataList.size) * 0.06f
        finalDistance *= 0.001f
        return RunData(null, avgPace, finalDistance, time)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.prev_runs -> {
                ActivityChangeFragment().show(
                    supportFragmentManager,
                    ActivityChangeFragment.TAG
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        } else handleLocationStart()
    }

    private fun handleLocationStart() {
        mainLocationManager.startLocationMonitoring()
        binding.chronometer.base = SystemClock.elapsedRealtime() - subtime
        setRunDataView(locationData, false)
        binding.chronometer.start()
    }

    var previousLocation: Location? = null
    var distance: Float = 0f

    override fun onNewLocation(location: Location) {
        if (previousLocation != null && location.accuracy < 20) {
            if (previousLocation!!.time < location.time) {
                distance += previousLocation!!.distanceTo(location)

            }
        }
        previousLocation = location
        val difference = System.currentTimeMillis() - start
        locationData = LocationData(distance, location.speed, 1)
        locationDataList.add(locationData)
        setRunDataView(locationData, false)
    }

    override fun onBackPressed() {
        ActivityChangeFragment().show(
            supportFragmentManager,
            ActivityChangeFragment.TAG
        )
    }

    private fun setRunDataView(location: LocationData, paused: Boolean) {
        binding.tvDistance.text =
            getString(R.string.distance_f_km, locationData.distance * 0.001)
        if (paused)
            binding.tvPace.text = getString(R.string.pace_d, 0f)
        else
            binding.tvPace.text = getString(
                R.string.pace_d, locationData.speed
                        * 0.06
            )
    }

    override fun saveRunData() {
        val runData = GetRunData()
        val intent = Intent(this, RunningListActivity::class.java)
        intent.putExtra("pace", runData.pace)
        intent.putExtra("distance", runData.distance)
        intent.putExtra("time", runData.time)
        intent.putExtra("shouldSave", true)
        onLocationStop()
        startActivity(intent)
    }
}