package com.example.medicomgmester.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ForgetPassword(
    val patients_tel: String?,
    val patients_name: String?,
    val new_password: String?
)

data class ResForget(
    var message: String? = null,
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()
    )

    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Appointment> = object : Parcelable.Creator<Appointment> {
            override fun createFromParcel(source: Parcel): Appointment = Appointment(source)
            override fun newArray(size: Int): Array<Appointment?> = arrayOfNulls(size)
        }
    }
}
data class ListForgetPassword(@SerializedName("data") var results: List<ResForget>? = null)

