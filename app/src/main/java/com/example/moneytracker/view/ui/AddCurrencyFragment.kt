
package com.example.moneytracker.view.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentAddCurrencyBinding
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.AddCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AddCurrencyFragment : Fragment(R.layout.fragment_add_currency) {
    private val addCurrencyViewModel: AddCurrencyViewModel by viewModels()

    @Inject
    lateinit var addFragment: AddFragment

    private var addCurrencyLiveData: MutableLiveData<Response<Currency>> = MutableLiveData()
    private var currencyLiveData: MutableLiveData<Response<List<Currency>>> = MutableLiveData()

    private var _binding: FragmentAddCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        addCurrencyLiveData = MutableLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCurrencyLiveData.observe(viewLifecycleOwner) {
            try {
                responseErrorHandler(it)
                switchToAdd()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        fulfillCurrencySpinner()


        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fulfillCurrencySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            currencyLiveData.observe(viewLifecycleOwner) {
                try {
                    val res = responseErrorHandler(it)
                    val currenciesNames = res.map { currency -> currency.name }
                    val currenciesAdapter = ArrayAdapter(
                        activity as Context,
                        android.R.layout.simple_spinner_item,
                        currenciesNames
                    )
                    binding.inputCurrency.adapter = currenciesAdapter
                } catch (e: Exception) {
                    makeErrorToast(requireContext(), e.message, 200)
                }
            }

            currencyLiveData.value = addCurrencyViewModel.getSupportedCurrencies()
        }
    }

    private fun submitForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            addCurrencyLiveData.value = addCurrencyViewModel.addNewCurrency(
                binding.inputCurrency.selectedItem.toString(),
            )
        }
    }

    private fun switchToAdd() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, addFragment)
            commit()
        }
    }
}