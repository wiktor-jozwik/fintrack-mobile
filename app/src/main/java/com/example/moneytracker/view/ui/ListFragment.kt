package com.example.moneytracker.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    @Inject
    lateinit var operationListFragment: OperationListFragment

    @Inject
    lateinit var categoryListFragment: CategoryListFragment

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MT", "555")

        childFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragmentList, operationListFragment)
            commit()
        }

        binding.buttonShowOperations.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentList, operationListFragment)
                commit()
            }
        }

        binding.buttonShowCategories.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragmentList, categoryListFragment)
                commit()
            }
        }
    }
}