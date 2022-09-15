package com.example.moneytracker.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListCurrencyBinding
import com.example.moneytracker.service.model.mt.Currency
import com.example.moneytracker.view.adapter.CurrencyListAdapter
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.view.ui.utils.responseErrorHandler
import com.example.moneytracker.viewmodel.ListCurrencyViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class ListCurrencyFragment : Fragment(R.layout.fragment_list_currency) {
    private val listCurrencyViewModel: ListCurrencyViewModel by viewModels()

    private var listCurrencyLiveData: MutableLiveData<Response<List<Currency>>> = MutableLiveData()
    private var deleteCurrencyLiveData: MutableLiveData<Response<Currency>> = MutableLiveData()

    private var _binding: FragmentListCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCurrencyBinding.inflate(inflater, container, false)
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
                val adapter = binding.recyclerViewCurrencyItems.adapter as CurrencyListAdapter
                adapter.deleteUserCurrency(res.id)
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listCurrencyLiveData.value = listCurrencyViewModel.getUsersCurrencies()
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
                viewLifecycleOwner.lifecycleScope.launch {
                    deleteCurrencyLiveData.value =
                        listCurrencyViewModel.deleteUserCurrency(userCurrencyId)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}