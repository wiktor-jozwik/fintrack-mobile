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
import com.example.moneytracker.viewmodel.ListCurrencyViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListCurrencyFragment : Fragment(R.layout.fragment_list_currency) {
    private val listCurrencyViewModel: ListCurrencyViewModel by viewModels()

    @Inject
    lateinit var saveCurrencyFragment: SaveCurrencyFragment

    private var listCurrencyLiveData: MutableLiveData<List<Currency>> = MutableLiveData()
    private var deleteCurrencyLiveData: MutableLiveData<Currency> = MutableLiveData()

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

        binding.buttonPlus.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.homeFrameLayoutFragment, saveCurrencyFragment)
                commit()
            }
        }

        val deleteLambda = CurrencyListAdapter.OnClickListener { userCurrencyId ->
            deleteUserCurrency(
                userCurrencyId,
            )
        }

        listCurrencyLiveData.observe(viewLifecycleOwner) {
            binding.recyclerViewCurrencyItems.adapter = CurrencyListAdapter(
                it,
                deleteLambda
            )
            binding.recyclerViewCurrencyItems.adapter!!.notifyDataSetChanged()
        }

        deleteCurrencyLiveData.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerViewCurrencyItems.adapter as CurrencyListAdapter
            adapter.deleteUserCurrency(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                listCurrencyLiveData.value = listCurrencyViewModel.getUsersCurrencies()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
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
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        deleteCurrencyLiveData.value =
                            listCurrencyViewModel.deleteUserCurrency(userCurrencyId)
                    } catch (e: Exception) {
                        makeErrorToast(requireContext(), e.message, 200)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}