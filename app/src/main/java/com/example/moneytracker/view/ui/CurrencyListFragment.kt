package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentCurrencyListBinding
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.view.adapter.CurrencyListAdapter
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.CurrencyListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class CurrencyListFragment : Fragment(R.layout.fragment_currency_list) {
    private val currencyListViewModel: CurrencyListViewModel by viewModels()

    private var listCurrencyLiveData: MutableLiveData<Response<List<Currency>>> = MutableLiveData()
    private var deleteCurrencyLiveData: MutableLiveData<Response<Currency>> = MutableLiveData()

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
        listCurrencyLiveData = MutableLiveData()
        deleteCurrencyLiveData = MutableLiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deleteLambda = CurrencyListAdapter.OnClickListener { userCurrencyId ->
            deleteUserCurrency(
                userCurrencyId,
            )
        }

        listCurrencyLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                binding.recyclerViewCurrencyItems.adapter = CurrencyListAdapter(
                    res,
                    deleteLambda
                )
                binding.recyclerViewCurrencyItems.adapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        deleteCurrencyLiveData.observe(viewLifecycleOwner) {
            try {
                val res = responseErrorHandler(it)
                Log.d("MT", res.toString())
                val adapter = binding.recyclerViewCurrencyItems.adapter as CurrencyListAdapter
                adapter.deleteUserCurrency(res.id)
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listCurrencyLiveData.value = currencyListViewModel.getUsersCurrencies()
        }

        binding.recyclerViewCurrencyItems.adapter = CurrencyListAdapter(
            listOf(),
            deleteLambda
        )
        binding.recyclerViewCurrencyItems.layoutManager = LinearLayoutManager(activity)
    }

    private fun deleteUserCurrency(userCurrencyId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setCancelable(false)
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                Log.d("MT", "deleting user currency")
                viewLifecycleOwner.lifecycleScope.launch {
                    deleteCurrencyLiveData.value = currencyListViewModel.deleteUserCurrency(userCurrencyId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}