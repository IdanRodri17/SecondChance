package com.example.secondchance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.secondchance.databinding.FragmentSellerDetailBinding

class SellerDetailFragment : Fragment() {

    private var _binding: FragmentSellerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerDetailBinding.inflate(inflater, container, false)

        val seller = arguments?.getParcelable<Seller>("seller")
        val product = arguments?.getParcelable<Product>("product")

        seller?.let {
            binding.tvSellerName.text = getString(R.string.seller_name, seller.name)
            binding.tvSellerPhone.text = getString(R.string.seller_number, seller.phone)
            binding.tvSellerAddress.text = getString(R.string.tel_aviv, seller.address)


            binding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${seller.phone}")
                }
                startActivity(intent)
            }
        }

        binding.backToListButton2.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
