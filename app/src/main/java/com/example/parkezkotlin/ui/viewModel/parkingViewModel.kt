import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore

class ParkingViewModel : ViewModel() {
    val parkingsLiveData = MutableLiveData<List<parkingModel>?>()

    private val db = FirebaseFirestore.getInstance()

    fun fetchParkings() {
        db.collection("parkings")
            .get()
            .addOnSuccessListener { documents ->
                val parkings = mutableListOf<parkingModel>()
                for (document in documents) {
                    val parking = document.toObject(parkingModel::class.java)
                    parkings.add(parking)
                }
                parkingsLiveData.postValue(parkings)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                parkingsLiveData.postValue(null)
            }
    }
}
