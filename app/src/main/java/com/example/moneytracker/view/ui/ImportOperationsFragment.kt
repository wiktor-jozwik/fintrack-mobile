package com.example.moneytracker.view.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moneytracker.R
import com.example.moneytracker.databinding.FragmentImportOperationsBinding
import com.example.moneytracker.view.ui.utils.makeErrorToast
import com.example.moneytracker.viewmodel.ImportOperationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


@AndroidEntryPoint
class ImportOperationsFragment : Fragment(R.layout.fragment_import_operations) {
    private val importOperationsViewModel: ImportOperationsViewModel by viewModels()

    private var csvImportWaysLiveData: MutableLiveData<List<String>> = MutableLiveData()

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fulfillCsvImportWaysSpinner()

        binding.buttonSelectFile.setOnClickListener {
            selectImage()
        }

        binding.buttonImport.setOnClickListener {
//            submitForm()
        }
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

        private fun selectImage() {
//        val intent = Intent()
//            .setType("*/*")
//            .setAction(Intent.ACTION_GET_CONTENT)
            val pickIntent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickIntent, 111)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111) {
            data?.data?.let { uri ->
                uploadFile(uri)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFile(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            val stream = requireContext().contentResolver.openInputStream(uri) ?: return@launch
            val request = RequestBody.create(MediaType.parse("application/csv"), stream.readBytes()) // read all bytes using kotlin extension
            val filePart = MultipartBody.Part.createFormData(
                "file",
                uri.path,
                request
            )
            try {
                importOperationsViewModel.importOperations(filePart, binding.inputCsvImportWays.selectedItem.toString())
            }
            catch (e: Exception) {
                Log.d("MT", e.toString())
                return@launch
            }
            Log.d("MyActivity", "on finish upload file")
        }
    }


//    fun openActivityForResult() {
//        startForResult.launch(Intent(this, AnotherActivity::class.java))
//    }
//
//
//    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            result: ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
//            // Handle the Intent
//            //do stuff here
//        }
//    }

//    private fun selectImage() {
//        val intent = Intent()
//            .setType("*/*")
//            .setAction(Intent.ACTION_GET_CONTENT)
//
//        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 111 && resultCode == RESULT_OK) {
//            val selectedFileUri = data?.data //The uri with the location of the file
//            if (selectedFileUri != null && !selectedFileUri.path.isNullOrBlank()) {
//                val path = selectedFileUri.path
//                if (!path.isNullOrBlank()) {
//                    val file = File(path)
//                    viewLifecycleOwner.lifecycleScope.launch {
//                        try {
//                            val requestBody =
//                                RequestBody.create(MediaType.parse("application/csv"), file)
//                            val filePart = MultipartBody.Part.createFormData(
//                                "file",
//                                selectedFileUri.toString(),
//                                requestBody
//                            )
//
//                            Log.d("MT", filePart.toString())
//
//                            importOperationsViewModel.importOperations(filePart, "")
//
//                        } catch (e: Exception) {
//                            makeErrorToast(requireContext(), e.message, 200)
//                        }
//                    }
//                }
//            }
//        }
//    }
//


//
//    private fun submitForm() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                addCurrencyLiveData.value = saveCurrencyViewModel.addNewCurrency(
//                    binding.inputCurrency.selectedItem.toString(),
//                )
//            } catch (e: Exception) {
//                makeErrorToast(requireContext(), e.message, 200)
//            }
//        }
//    }
//
//    private fun switchToCurrencyList() {
//        parentFragmentManager.beginTransaction().apply {
//            replace(R.id.homeFrameLayoutFragment, listCurrencyFragment)
//            commit()
//        }
//    }

}