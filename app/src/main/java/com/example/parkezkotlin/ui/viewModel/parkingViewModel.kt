import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore

class ParkingViewModel : ViewModel() {
    val parkingsLiveData = MutableLiveData<List<parkingModel>?>()
    val parkingDetailLiveData = MutableLiveData<parkingModel?>()  // Nuevo LiveData para el detalle

    private val db = FirebaseFirestore.getInstance()

    fun fetchParkings() {
        db.collection("parkings")
            .get()
            .addOnSuccessListener { documents ->
                val parkings = mutableListOf<parkingModel>()
                for (document in documents) {
                    val parking = document.toObject(parkingModel::class.java).apply {
                        uid = document.id  // Agrega el ID del documento al modelo
                    }
                    parkings.add(parking)
                }
                parkingsLiveData.postValue(parkings)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                parkingsLiveData.postValue(null)
            }
    }

    fun fetchParkingDetails(parkingId: String) {
        println("Parking ID: $parkingId")
        db.collection("parkings").document(parkingId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    parkingDetailLiveData.postValue(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val parkingDetail = snapshot.toObject(parkingModel::class.java)
                    parkingDetailLiveData.postValue(parkingDetail)
                } else {
                    Log.d(TAG, "Current data: null")
                    parkingDetailLiveData.postValue(null)
                }
            }
    }
}
