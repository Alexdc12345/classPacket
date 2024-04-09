package com.example.classpacket.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "phishing_table")
class Phishing() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    @ColumnInfo(name = "link")
    var link: String = ""
    @ColumnInfo(name = "username")
    var username: String = ""
    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "Classification")
    var classification: String = ""

    constructor(parcel: Parcel) : this() {
        uid = parcel.readInt()
        link = parcel.readString() ?: ""
        username = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(link)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Phishing> {
        override fun createFromParcel(parcel: Parcel): Phishing {
            return Phishing(parcel)
        }

        override fun newArray(size: Int): Array<Phishing?> {
            return arrayOfNulls(size)
        }
    }
}
