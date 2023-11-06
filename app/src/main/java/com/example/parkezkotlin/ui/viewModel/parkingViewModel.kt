import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class ParkingViewModel : ViewModel() {
    val parkingsLiveData = MutableLiveData<List<parkingModel>?>()
    val parkingDetailLiveData = MutableLiveData<parkingModel?>()  // Nuevo LiveData para el detalle

    private val db = FirebaseFirestore.getInstance()

    fun fetchParkings() {
        db.collection("parkings")
            .get(Source.CACHE)  // Force the query to use the local cache
            .addOnSuccessListener { documents ->
                val parkings = mutableListOf<parkingModel>()
                for (document in documents) {
                    val parking = document.toObject(parkingModel::class.java).apply {
                        uid = document.id  // Add the document ID to the model
                    }
                    parkings.add(parking)
                }
                parkingsLiveData.postValue(parkings)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents from cache: ", exception)
                // Attempt to fetch from the network if cache fails
                fetchParkingsFromNetwork()
            }
    }

    private fun fetchParkingsFromNetwork() {
        db.collection("parkings")
            .get()
            .addOnSuccessListener { documents ->
                val parkings = mutableListOf<parkingModel>()
                for (document in documents) {
                    val parking = document.toObject(parkingModel::class.java).apply {
                        uid = document.id
                    }
                    parkings.add(parking)
                }
                parkingsLiveData.postValue(parkings)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents from network: ", exception)
                parkingsLiveData.postValue(null)
            }
    }

    fun fetchParkingDetails(parkingId: String) {
        println("Parking ID: $parkingId")
        val docRef = db.collection("parkings").document(parkingId)

        // Attempt to get the parking details from the cache first
        docRef.get(Source.CACHE)
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Parse the snapshot if it exists and update LiveData
                    val parkingDetail = snapshot.toObject(parkingModel::class.java)
                    parkingDetailLiveData.postValue(parkingDetail)
                } else {
                    // If the snapshot doesn't exist in the cache, fetch from the network
                    fetchParkingDetailsFromNetwork(parkingId)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting parking details from cache: ", exception)
                // If there is an error fetching from the cache, fetch from the network
                fetchParkingDetailsFromNetwork(parkingId)
            }
    }

    private fun fetchParkingDetailsFromNetwork(parkingId: String) {
        val docRef = db.collection("parkings").document(parkingId)

        // Listen for the parking details from the network
        docRef.get()
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
