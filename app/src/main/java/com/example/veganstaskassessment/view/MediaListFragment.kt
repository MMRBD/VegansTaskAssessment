package com.example.veganstaskassessment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veganstaskassessment.data.models.Content
import com.example.veganstaskassessment.data.network.ApiState
import com.example.veganstaskassessment.databinding.FragmentMediaListBinding
import com.example.veganstaskassessment.view.adapter.MediaAdapter
import com.example.veganstaskassessment.viewmodel.MediaViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaListFragment : Fragment() {

    private lateinit var binding: FragmentMediaListBinding
    private lateinit var adapter: MediaAdapter

    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MediaAdapter {
            val action =
                MediaListFragmentDirections.actionMediaListFragmentToMediaDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.rcvMediaList.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvMediaList.adapter = adapter

        binding.rcvMediaList.addItemDecoration(
            DividerItemDecoration(
                binding.rcvMediaList.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMedias()

        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedPosition = viewHolder.adapterPosition
                val deletedItem = adapter.currentList[deletedPosition]

                adapter.notifyItemRemoved(deletedPosition)
                viewModel.onMediaItemSwipe(deletedItem, deletedPosition)
            }

        }).attachToRecyclerView(binding.rcvMediaList)


        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect {
                when (it) {
                    is ApiState.Empty -> {
                        binding.progressBar.isVisible = false

                    }
                    is ApiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.rcvMediaList.isVisible = false

                    }
                    is ApiState.Success<*> -> {
                        binding.progressBar.isVisible = false
                        binding.swipeRefresh.isRefreshing = false
                        binding.rcvMediaList.isVisible = true

                        adapter.submitList(it.data as List<Content>)
                    }
                    is ApiState.Failed -> {
                        binding.progressBar.isVisible = false
                        binding.swipeRefresh.isRefreshing = false
                        binding.rcvMediaList.isVisible = false

                        Toast.makeText(
                            requireContext(),
                            it.message.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.todoEvent.collect { event ->
                when (event) {
                    is MediaViewModel.MediaEvent.ShowUndoDeleteTodoMessage -> {
                        Snackbar.make(binding.rcvMediaList, "Media Deleted", Snackbar.LENGTH_LONG)
                            .setAction(
                                "Undo"
                            ) {
                                viewModel.onUndoDeleteClick(event.media, event.position)
                                adapter.notifyItemInserted(event.position)
                            }.show()
                    }
                }
            }
        }
    }
}