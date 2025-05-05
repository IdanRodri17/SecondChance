package com.example.secondchance

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.secondchance.databinding.FragmentAddEditProductBinding
import java.io.File

class AddEditProductFragment : Fragment() {

    private var _binding: FragmentAddEditProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel
    private lateinit var sellerList: List<Seller>
    private lateinit var selectedSeller: Seller
    private var selectedImageUri: Uri? = null

    private val args: AddEditProductFragmentArgs by navArgs()

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.ivProductImage.setImageURI(it)
            binding.ivProductImage.visibility = View.VISIBLE
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapAndGetUri(it)
            selectedImageUri = uri
            binding.ivProductImage.setImageURI(uri)
            binding.ivProductImage.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditProductBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]

        val product = args.product
        val isEditMode = product != null

        viewModel.sellerList.observe(viewLifecycleOwner) { sellers ->
            sellerList = sellers
            val sellerNames = sellers.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sellerNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sellerSpinner.adapter = adapter

            if (isEditMode) {
                val position = sellerList.indexOfFirst { it.sellerId == product!!.sellerId }
                if (position >= 0) {
                    binding.sellerSpinner.setSelection(position)
                    selectedSeller = sellerList[position]
                }
            }

            binding.sellerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedSeller = sellerList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        if (isEditMode) {
            binding.etProductName.setText(product!!.name)
            binding.etProductDescription.setText(product.description)
            binding.Price.setText(product.price.replace(" ₪", ""))
            product.imageUri?.let {
                selectedImageUri = Uri.parse(it)
                binding.ivProductImage.setImageURI(selectedImageUri)
                binding.ivProductImage.visibility = View.VISIBLE
            }
        }

        binding.btnSelectImage.setOnClickListener {
            val options = arrayOf(
                getString(R.string.choose_from_gallery),
                getString(R.string.take_a_photo)
            )
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.select_image_source))
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> galleryLauncher.launch("image/*")
                        1 -> cameraLauncher.launch(null)
                    }
                }
                .show()
        }

        binding.btnSaveProduct.setOnClickListener {
            val name = binding.etProductName.text.toString().trim()
            val description = binding.etProductDescription.text.toString().trim()
            val price = binding.Price.text.toString().trim()

            if (name.isBlank()) {
                binding.etProductName.error = getString(R.string.please_enter_a_product_name)
                return@setOnClickListener
            }

            val priceValue = price.toDoubleOrNull()
            if (priceValue == null || priceValue < 0) {
                binding.Price.error = getString(R.string.please_enter_a_valid_price)
                return@setOnClickListener
            }

            if (!::selectedSeller.isInitialized) {
                Toast.makeText(requireContext(), getString(R.string.please_select_a_seller), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalProduct = Product(
                id = product?.id ?: 0,
                name = name,
                price = "$price ₪",
                description = description,
                imageUri = selectedImageUri?.toString(),
                sellerId = selectedSeller.sellerId
            )

            if (isEditMode) {
                viewModel.updateProduct(finalProduct)
            } else {
                viewModel.addProductToSeller(selectedSeller.sellerId, finalProduct)
            }

            Toast.makeText(requireContext(), getString(R.string.product_saved_successfully), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.backToListButton1.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun saveBitmapAndGetUri(bitmap: Bitmap): Uri? {
        val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
