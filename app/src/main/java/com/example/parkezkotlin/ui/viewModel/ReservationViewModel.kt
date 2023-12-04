package com.example.parkezkotlin.ui.viewModel




import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkezkotlin.data.model.Reservation
import com.example.parkezkotlin.data.model.parkingModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReservationViewModel : ViewModel() {
    val reservationsLiveData = MutableLiveData<List<Reservation>?>()
    val reservationDetailLiveData = MutableLiveData<Reservation?>()
    val parkingDetailLiveData = MutableLiveData<parkingModel?>()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ReservationViewModel"  // Ensure TAG is initialized

    fun fetchReservationsFromUser(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("reservations")
                    .whereEqualTo("user", userId)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    Log.d(TAG, "No reservations found for user $userId")
                    reservationsLiveData.postValue(emptyList())
                } else {
                    val reservations = snapshot.toObjects(Reservation::class.java)
                    Log.d(TAG, "Fetched reservations: $reservations") // Log the fetched data

                    reservationsLiveData.postValue(reservations)
                }
            } catch (exception: Exception) {
                Log.w(TAG, "Error getting reservations from network: ", exception)
                reservationsLiveData.postValue(null)
            }
        }
    }

    fun fetchReservationDetails(reservationId: String) {
        val docRef = db.collection("reservations").document(reservationId)

        docRef.get()  // This will fetch from the network by default
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val reservationDetail = snapshot.toObject(Reservation::class.java)
                    reservationDetailLiveData.postValue(reservationDetail)
                } else {
                    Log.d(TAG, "No such document in the network")
                    reservationDetailLiveData.postValue(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting parking details from network: ", exception)
                reservationDetailLiveData.postValue(null)
            }
    }

}
