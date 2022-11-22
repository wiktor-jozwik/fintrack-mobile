package com.example.fintrack.view.ui

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
import androidx.navigation.Navigation.findNavController
import com.example.fintrack.R
import com.example.fintrack.databinding.FragmentSaveCurrencyBinding
import com.example.fintrack.service.model.ft.Currency
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.viewmodel.SaveCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveCurrencyFragment : Fragment(R.layout.fragment_save_currency) {
    private val saveCurrencyViewModel: SaveCurrencyViewModel by viewModels()

    private var addCurrencyLiveData: MutableLiveData<Currency> = MutableLiveData()
    private var currencyLiveData: MutableLiveData<List<Currency>> = MutableLiveData()

    private var _binding: FragmentSaveCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveCurrencyBinding.inflate(inflater, container, false)
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
            findNavController(view).navigate(R.id.action_saveCurrencyFragment_to_listCurrencyFragment)
        }

        fulfillCurrencySpinner()

        binding.buttonSave.setOnClickListener {
            submitForm()
        }
    }

    private fun fulfillCurrencySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            currencyLiveData.observe(viewLifecycleOwner) {
                val currenciesNames = it.map { currency -> currency.name }
                val currenciesAdapter = ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    currenciesNames
                )
                binding.inputCurrency.adapter = currenciesAdapter
            }

            try {
                currencyLiveData.value = saveCurrencyViewModel.getSupportedCurrencies()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun submitForm() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                addCurrencyLiveData.value = saveCurrencyViewModel.addNewCurrency(
                    binding.inputCurrency.selectedItem.toString(),
                )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }
}