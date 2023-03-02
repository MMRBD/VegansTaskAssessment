package com.example.veganstaskassessment.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.veganstaskassessment.databinding.FragmentMediaDetailsBinding
import com.example.veganstaskassessment.viewmodel.MediaViewModel


class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding
    private lateinit var viewModel: MediaViewModel

    private val args: MediaDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity())[MediaViewModel::class.java]

        with(binding) {
            tvMediaTitleCustom.text = args.media.mediaTitleCustom
            tvDateString.text = args.media.mediaDate.dateString
        }

        binding.tvMediaTitleCustom.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(args.media.mediaUrl))
            startActivity(browserIntent)
        }

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.useWideViewPort = true;
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=${args.media.mediaUrl}")
    }
}