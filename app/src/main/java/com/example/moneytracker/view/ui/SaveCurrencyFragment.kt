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
import com.example.moneytracker.databinding.FragmentSaveCurrencyBinding
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.viewmodel.SaveCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SaveCurrencyFragment : Fragment(R.layout.fragment_save_currency) {
    private val saveCurrencyViewModel: SaveCurrencyViewModel by viewModels()

    @Inject
    lateinit var saveFragment: SaveFragment

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
            switchToAdd()
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

    private fun switchToAdd() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.homeFrameLayoutFragment, saveFragment)
            commit()
        }
    }
}