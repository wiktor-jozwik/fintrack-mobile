package com.example.fintrack.view.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.fintrack.databinding.FragmentImportOperationsBinding
import com.example.fintrack.service.model.ft.StringResponse
import com.example.fintrack.view.ui.utils.makeErrorToast
import com.example.fintrack.view.ui.utils.makePositiveToast
import com.example.fintrack.viewmodel.ImportOperationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class ImportOperationsFragment : Fragment(R.layout.fragment_import_operations) {
    private val IMPORT_CODE = 111
    private val importOperationsViewModel: ImportOperationsViewModel by viewModels()

    private var csvImportWaysLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private var importOperationsLiveData: MutableLiveData<StringResponse> = MutableLiveData()

    private var fileUri: Uri? = null

    private var _binding: FragmentImportOperationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportOperationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFields()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMPORT_CODE) {
            data?.data?.let { uri ->
                fileUri = uri
                binding.textFileChosen.text = "FILE UPLOADED: ${File(uri.path.toString()).name}"
                binding.textFileChosen.visibility = View.VISIBLE
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        importOperationsLiveData.observe(viewLifecycleOwner) {
            findNavController(view).navigate(R.id.action_importOperationsFragment_to_listOperationFragment)
            makePositiveToast(requireContext(), it.response, 200)
            clearFields()
        }

        fulfillCsvImportWaysSpinner()

        binding.buttonSelectFile.setOnClickListener {
            selectCsv()
        }

        binding.buttonImport.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
                .setCancelable(false)
                .setMessage("Are you sure to trigger import?")
                .setPositiveButton("Yes") { _, _ ->
                    submitForm()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun clearFields() {
        binding.textFileChosen.text = ""
        binding.textFileChosen.visibility = View.INVISIBLE
        fileUri = null
        importOperationsLiveData = MutableLiveData()
    }

    private fun fulfillCsvImportWaysSpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            csvImportWaysLiveData.observe(viewLifecycleOwner) {
                val csvImportWaysAdapter = ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    it
                )
                binding.inputCsvImportWays.adapter = csvImportWaysAdapter
            }

            try {
                csvImportWaysLiveData.value = importOperationsViewModel.getSupportedCsvImportWays()
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun selectCsv() {
        val pickIntent =
            Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickIntent, IMPORT_CODE)
    }


    private fun submitForm() {
        if (fileUri != null) {
            validForm(fileUri!!)
        } else {
            invalidForm()
        }
    }

    private fun validForm(uri: Uri) {
        triggerImport(uri)
    }

    private fun triggerImport(uri: Uri) {
        val stream = requireContext().contentResolver.openInputStream(uri) ?: return
        val request = RequestBody.create(
            MediaType.parse("application/csv"),
            stream.readBytes()
        )
        val filePart = MultipartBody.Part.createFormData(
            "file",
            uri.path,
            request
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                importOperationsLiveData.value = importOperationsViewModel.importOperations(
                    filePart,
                    binding.inputCsvImportWays.selectedItem.toString()
                )
            } catch (e: Exception) {
                makeErrorToast(requireContext(), e.message, 200)
            }
        }
    }

    private fun invalidForm() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog)
            .setTitle("Invalid form")
            .setMessage("Please provide requested fields.")
            .setPositiveButton("Okay") { _, _ -> {} }
            .show()
    }
}