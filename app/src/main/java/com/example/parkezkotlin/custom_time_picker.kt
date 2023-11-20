
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.parkezkotlin.R
import java.util.Calendar

class CustomTimePickerFragment : DialogFragment() {
    private var listener: TimePickerListener? = null

    interface TimePickerListener {
        fun onTimeSelected(hourOfDay: Int, minute: Int, tag: String?)
    }

    fun setOnTimeSelectedListener(listener: TimePickerListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_custom_time_picker, null)
        val timePicker: TimePicker = view.findViewById(R.id.timePicker)

        val currentCalendar = Calendar.getInstance()
        timePicker.hour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        timePicker.minute = currentCalendar.get(Calendar.MINUTE)

        return AlertDialog.Builder(requireContext()).apply {
            setView(view)
            setPositiveButton("OK") { _, _ ->
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute

                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }

                if (selectedCalendar.before(currentCalendar)) {
                    // La hora seleccionada es anterior a la hora actual
                    Toast.makeText(activity, "La hora seleccionada no puede ser anterior a la hora actual.", Toast.LENGTH_LONG).show()
                } else {
                    // La hora seleccionada es válida
                    listener?.onTimeSelected(selectedHour, selectedMinute, tag)
                }
            }
            setNegativeButton("Cancel", null)
        }.create()
    }
}
