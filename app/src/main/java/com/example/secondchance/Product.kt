package com.example.secondchance

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ProductsTable")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    @ColumnInfo("Name")
    val name: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("Price")
    val price: String,
    @ColumnInfo(name = "ImageResId")
    val imageRes: Int = R.drawable.logo,
    @ColumnInfo(name = "ImageUri")
    val imageUri: String? = null,
    val sellerId: String

) :Parcelable{


}

