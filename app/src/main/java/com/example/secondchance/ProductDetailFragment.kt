package com.example.secondchance

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.secondchance.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = arguments?.getParcelable<Product>("product") ?: return

        // הצגת פרטי המוצר
        binding.tvProductName.text = product.name
        binding.tvProductPrice.text = product.price
        binding.tvProductDescription.text = product.description ?: "אין תיאור"

        val imageSource = product.imageUri?.let { Uri.parse(it) } ?: product.imageRes
        Glide.with(requireContext())
            .load(imageSource)
            .centerCrop()
            .into(binding.ivProductImage)

        // תצוגת תמונה במסך מלא
        binding.ivProductImage.setOnClickListener {
            showImageFullScreen(imageSource)
        }

        // כפתור "צור קשר עם המוכר"
        binding.btnContactSeller.setOnClickListener {
            val seller = ViewModelProvider(requireActivity())[ProductViewModel::class.java]
                .getSellerById(product.sellerId)

            if (seller == null) {
                Toast.makeText(requireContext(), "לא נמצאו פרטי מוכר", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val action = ProductDetailFragmentDirections
                .actionProductDetailFragmentToSellerDetailFragment(seller, product)
            findNavController().navigate(action)
        }

        // כפתור חזרה
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showImageFullScreen(imageSource: Any) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_fullscreen_image)

        val imageView = dialog.findViewById<ImageView>(R.id.fullscreenImageView)

        Glide.with(requireContext())
            .load(imageSource)
            .into(imageView)

        imageView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



