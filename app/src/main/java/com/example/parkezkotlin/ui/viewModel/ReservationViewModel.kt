package com.example.parkezkotlin.ui.viewModel




import android.util.Log
import androidx.lifecycle.LiveData
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
    val reservationDetailLiveData = MutableLiveData<Pair<String, Reservation?>?>()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ReservationViewModel"

    // Cache that stores reservation details using reservation ID
    val reservationDetailsCache = mutableMapOf<String, Reservation>()

    private val parkingLotNamesMap = MutableLiveData<Map<String, String>>()

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
                    // Cache reservation details
                    reservations.forEach { reservation ->
                        reservationDetailsCache[reservation.uid ?: ""] = reservation
                    }
                }
            } catch (exception: Exception) {
                Log.w(TAG, "Error getting reservations from network: ", exception)
                reservationsLiveData.postValue(null)
            }
        }
    }

    fun fetchParkingLotNames() {
        db.collection("parkings")
            .get()
            .addOnSuccessListener { documents ->
                val map = mutableMapOf<String, String>()
                for (document in documents) {
                    val parkingLotId = document.id
                    val parkingLotName = document.getString("name") ?: ""
                    map[parkingLotId] = parkingLotName
                }
                parkingLotNamesMap.postValue(map)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting parking lot names: ", exception)
            }
    }

    fun getParkingLotNamesMap(): LiveData<Map<String, String>> {
        return parkingLotNamesMap
    }



    fun fetchReservationDetails(reservationId: String) {
        if (reservationDetailsCache.containsKey(reservationId)) {
            // If the details are cached, retrieve from the cache
            val cachedReservation = reservationDetailsCache[reservationId]
            reservationDetailLiveData.postValue(Pair(reservationId, cachedReservation))
        } else {
            // Fetch details from Firestore if not cached
            val docRef = db.collection("reservations").document(reservationId)
            docRef.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val reservationDetail = snapshot.toObject(Reservation::class.java)
                        reservationDetailLiveData.postValue(Pair(reservationId, reservationDetail))
                        reservationDetail?.let {
                            reservationDetailsCache[reservationId] = it // Cache the fetched details
                        }
                    } else {
                        Log.d(TAG, "No such document in the network")
                        reservationDetailLiveData.postValue(Pair(reservationId, null))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting reservation details from network: ", exception)
                    reservationDetailLiveData.postValue(Pair(reservationId, null))
                }
        }
    }
}

