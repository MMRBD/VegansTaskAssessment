package com.example.veganstaskassessment.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Media(
    val status: Boolean,
    val lang: String,
    var content: List<Content>
)

@Parcelize
data class MediaDate(
    val dateString: String,
    val year: String

) : Parcelable