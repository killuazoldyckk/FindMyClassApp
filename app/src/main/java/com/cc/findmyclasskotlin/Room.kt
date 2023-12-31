package com.cc.findmyclasskotlin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    val schedID: String?= null,
    val namaRuang: String?= null,
    val matkul: String?= null,
    val hari: String?= null,
    val jam: String?= null,
    val status: String?= null,
    val stambuk: String?= null,
    val kom: String?= null
) : Parcelable