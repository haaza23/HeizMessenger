package com.heiztechno.heizmessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profilePicture: String):Parcelable{

    constructor(): this("", "", "")             //Parcelable es para poder poner el paquete User dentro del intent

}