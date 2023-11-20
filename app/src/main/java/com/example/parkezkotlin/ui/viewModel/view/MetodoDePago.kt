package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentMetodoDePagoBinding
import com.google.firebase.functions.FirebaseFunctions
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.Token

class MetodoDePago : Fragment() {

    private var _binding: FragmentMetodoDePagoBinding? = null
    private val binding get() = _binding!!
    private lateinit var stripe: Stripe
    private lateinit var functions: FirebaseFunctions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMetodoDePagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parkingName = arguments?.getString("parkingName") ?: "Nombre no disponible"
        val reservationId = arguments?.getString("reservationId") ?: "ID no disponible"
        val totalCost = arguments?.getInt("totalCost") ?: 0
        val timeToReserve= arguments?.getInt("timeToReserve") ?: 0
        PaymentConfiguration.init(requireContext(), "pk_test_51O9bWRKDmBizySjnvjKaABN6AV1jq9Sa36eANNomhucKK0qa3Plnklra0Ys1TCIokcDtfReoZtyeDiCByhQdER4400wsFD7eYD")
        stripe = Stripe(requireContext(), PaymentConfiguration.getInstance(requireContext()).publishableKey)

        // Configura Firebase Functions para usar el emulador
        functions = FirebaseFunctions.getInstance()
        functions.useEmulator("127.0.2.2", 5001)

        binding.payButton.setOnClickListener {
            val cardInputWidget = binding.cardInputWidget
            val cardParams = cardInputWidget.cardParams
            if (cardParams != null) {
                stripe.createCardToken(cardParams, callback = object : ApiResultCallback<Token> {
                    override fun onSuccess(result: Token) {
                        callProcessPaymentFunction(result.id)
                        //redirect to recibos
                        val bundle = Bundle().apply {

                            putString("parkingName", parkingName)
                            putString("reservationId", reservationId)
                            putInt("totalCost", totalCost)
                            putInt("timeToReserve", timeToReserve)
                        }

                        findNavController().navigate(R.id.action_metodoDePago2_to_recibos, bundle)
                        Toast.makeText(context, "Pago procesado exitosamente", Toast.LENGTH_LONG).show()

                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(context, "Error en el pago: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(context, "Información de tarjeta inválida", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callProcessPaymentFunction(tokenId: String) {
        val data = hashMapOf(
            "token" to tokenId,
            "amount" to 1000
        )

        functions
            .getHttpsCallable("processPayment")
            .call(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Pago procesado exitosamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                //Toast.makeText(context, "Error al procesar el pago: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
