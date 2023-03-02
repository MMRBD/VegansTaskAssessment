package com.example.veganstaskassessment.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(
    val mediaId: Int,
    val mediaUrl: String,
    val mediaUrlBig: String,
    val mediaType: String,
    val mediaTitleCustom: String,
    val mediaDate: MediaDate

) : Parcelable