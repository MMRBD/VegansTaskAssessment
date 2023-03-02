package com.example.veganstaskassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veganstaskassessment.data.models.Content
import com.example.veganstaskassessment.data.network.ApiState
import com.example.veganstaskassessment.data.repo.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MediaViewModel @Inject constructor(
    private val repository: MediaRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ApiState>(ApiState.Empty)
    val stateFlow: StateFlow<ApiState> = _stateFlow


    private val todoEventChannel = Channel<MediaEvent.ShowUndoDeleteTodoMessage>()
    val todoEvent = todoEventChannel.receiveAsFlow()

    init {
        getMedias()
    }

    fun getMedias() {
        viewModelScope.launch {
            repository.getMedias()
                .onStart {
                    _stateFlow.value = ApiState.Loading
                }
                .catch { error ->
                    _stateFlow.value = ApiState.Failed(message = error)
                }
                .collect {
                    _stateFlow.value = ApiState.Success(it.content)
                }
        }
    }

    fun onMediaItemSwipe(media: Content, position: Int) = viewModelScope.launch {
        viewModelScope.launch {
            repository.removeItem(position)
                .catch { error ->
                    _stateFlow.value = ApiState.Failed(message = error)
                }
                .collect {
                    _stateFlow.value = ApiState.Success(it.content)
                }
        }
        todoEventChannel.send(MediaEvent.ShowUndoDeleteTodoMessage(media, position))
    }

    fun onUndoDeleteClick(media: Content, position: Int) {
        viewModelScope.launch {
            repository.undoDeleteItem(media, position)
                .catch { error ->
                    _stateFlow.value = ApiState.Failed(message = error)
                }
                .collect {
                    _stateFlow.value = ApiState.Success(it.content)
                }
        }
    }

    sealed class MediaEvent {
        data class ShowUndoDeleteTodoMessage(val media: Content, val position: Int) : MediaEvent()
    }

}