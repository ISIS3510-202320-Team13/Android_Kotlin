import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore

class ParkingViewModel : ViewModel() {
    val parkingsLiveData = MutableLiveData<List<parkingModel>?>()
    val parkingDetailLiveData = MutableLiveData<parkingModel?>()

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ParkingViewModel"  // Ensure TAG is initialized

    fun fetchParkings() {
        db.collection("parkings")
            .get()  // This will fetch from the network by default
            .addOnSuccessListener { documents ->
                val parkings = mutableListOf<parkingModel>()
                for (document in documents) {
                    val parking = document.toObject(parkingModel::class.java).apply {
                        uid = document.id  // Add the document ID to the model
                    }
                    parkings.add(parking)
                }
                parkingsLiveData.postValue(parkings)
                Log.d(TAG, "Parkings fetched from network")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents from network: ", exception)
                parkingsLiveData.postValue(null)
            }
    }

    fun fetchParkingDetails(parkingId: String) {
        val docRef = db.collection("parkings").document(parkingId)

        docRef.get()  // This will fetch from the network by default
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val parkingDetail = snapshot.toObject(parkingModel::class.java)
                    parkingDetailLiveData.postValue(parkingDetail)
                } else {
                    Log.d(TAG, "No such document in the network")
                    parkingDetailLiveData.postValue(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting parking details from network: ", exception)
                parkingDetailLiveData.postValue(null)
            }
    }

}
