import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ParkingViewModel : ViewModel() {
    val parkingsLiveData = MutableLiveData<List<parkingModel>?>()
    val parkingDetailLiveData = MutableLiveData<parkingModel?>()

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ParkingViewModel"

    // Cache that stores the parking ID and its name
    val parkingNameIdCache = mutableMapOf<String, String>()

    fun fetchParkings() {
        viewModelScope.launch {
            try {
                val documents = db.collection("parkings").get().await()
                val parkings = documents.mapNotNull { document ->
                    document.toObject(parkingModel::class.java).also { parking ->
                        parking.uid = document.id  // Cache the parking ID
                        parking.name?.let { name ->
                            parkingNameIdCache[document.id] = name  // Cache the parking name using the ID as key
                        }
                    }
                }
                parkingsLiveData.postValue(parkings)
                Log.d(TAG, "Parking names cached: $parkingNameIdCache")
                Log.d(TAG, "Parkings fetched from network")
            } catch (exception: Exception) {
                Log.w(TAG, "Error getting documents from network: ", exception)
                parkingsLiveData.postValue(null)
            }
        }
    }

    fun fetchParkingDetails(parkingId: String) {
        db.collection("parkings").document(parkingId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                snapshot.toObject(parkingModel::class.java)?.let { parkingDetail ->
                    parkingDetailLiveData.postValue(parkingDetail)
                }
            } else {
                Log.d(TAG, "No such document in the network")
                parkingDetailLiveData.postValue(null)
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting parking details from network: ", exception)
            parkingDetailLiveData.postValue(null)
        }
    }
}
