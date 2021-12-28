package hu.bme.aut.runnerapp.fragments

import android.app.Dialog
import android.content.Context
import android.opengl.ETC1.isValid
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.runnerapp.R
import hu.bme.aut.runnerapp.data.RunData
import hu.bme.aut.runnerapp.databinding.FragmentActivityChangeBinding

class ActivityChangeFragment : DialogFragment() {
    interface SaveRunDataListener {
        fun saveRunData()
    }
    private lateinit var listener: SaveRunDataListener
    private lateinit var binding : FragmentActivityChangeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SaveRunDataListener
            ?: throw RuntimeException("Activity must implement the SaveRunDataListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentActivityChangeBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.rundata_added)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                    listener.saveRunData()
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    companion object {
        const val TAG = "ActivityChangeFragment"
    }
}