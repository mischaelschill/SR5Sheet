package me.schill.sr5sheet.model

import android.databinding.Bindable
import me.schill.sr5sheet.BR
import me.schill.sr5sheet.persistence.Entity

class Attribute : Entity() {
    @Bindable
    var name = "?"
        set (value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var code = "?"
        set(value) {
            field = value
            notifyPropertyChanged(BR.code)
        }

    @Bindable
    var value = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.value)
        }

    override fun toString() = name + " (" + code + "): " + value
}