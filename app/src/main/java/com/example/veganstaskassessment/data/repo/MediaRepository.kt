package com.example.veganstaskassessment.data.repo

import com.example.veganstaskassessment.data.models.Content
import com.example.veganstaskassessment.data.models.Media
import com.example.veganstaskassessment.data.network.ApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {
    private var media: Media? = null

    fun getMedias(): Flow<Media> = flow {
        media = apiInterface.getMedias()
        emit(media!!)
    }

    fun removeItem(position: Int): Flow<Media> = flow {
        val tmp: MutableList<Content> = media?.content as MutableList<Content>
        tmp.removeAt(position)
        media?.content = tmp
        emit(media!!)
    }

    fun undoDeleteItem(content: Content, position: Int):  Flow<Media> = flow {
        val tmp: MutableList<Content> = media?.content as MutableList<Content>
        tmp.add(position, content)
        media?.content = tmp
        emit(media!!)
    }
}