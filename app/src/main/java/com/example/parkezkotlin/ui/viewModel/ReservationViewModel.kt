package com.example.parkezkotlin.ui.viewModel




import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.Reservation
import com.google.firebase.firestore.FirebaseFirestore

class ReservationViewModel : ViewModel() {
    val reservationsLiveData = MutableLiveData<List<Reservation>?>()
    val reservationDetailLiveData = MutableLiveData<Reservation?>()

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ReservationViewModel"  // Ensure TAG is initialized

    fun fetchReservations() {
        db.collection("reservations")
            .get()  // This will fetch from the network by default
            .addOnSuccessListener { documents ->
                val reservations = mutableListOf<Reservation>()
                for (document in documents) {
                    val reservation = document.toObject(Reservation::class.java).apply {
                        uid = document.id  // Add the document ID to the model
                    }
                    reservations.add(reservation)
                }
                reservationsLiveData.postValue(reservations)
                Log.d(TAG, "Reservations fetched from network")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents from network: ", exception)
                reservationsLiveData.postValue(null)
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
