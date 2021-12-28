package hu.bme.aut.runnerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.royrodriguez.transitionbutton.TransitionButton
import hu.bme.aut.runnerapp.databinding.ActivityStartAcivityBinding
import hu.bme.aut.runnerapp.fragments.ActivityChangeFragment

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartAcivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartAcivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.transitionButton.setOnClickListener {
            binding.transitionButton.startAnimation();
            val handler = Handler()
            handler.postDelayed(Runnable {
                var isSuccessful = true;
                if (isSuccessful) {
                    binding.transitionButton.stopAnimation(
                        TransitionButton.StopAnimationStyle.EXPAND
                    ) {
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    binding.transitionButton.stopAnimation(
                        TransitionButton.StopAnimationStyle.SHAKE,
                        null
                    )
                }
            }, 1000)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when (item.itemId) {
            R.id.prev_runs -> {
                startActivity(Intent(this,RunningListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}