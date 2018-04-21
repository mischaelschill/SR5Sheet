package me.schill.sr5sheet.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import me.schill.sr5sheet.BR
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
class SR5Character() : BaseObservable() {


    @Bindable
    @field:Element
    var name = "Anonymous"
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    @field:Element
    var metatype = "Mensch"
        set(value) {
            field = value
            notifyPropertyChanged(BR.metatype)
        }

    @Bindable
    @field:Element
    var ethnicity = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ethnicity)
        }

    @Bindable
    @field:Element(required = false)
    var specialAttribute: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.specialAttribute)
        }

}