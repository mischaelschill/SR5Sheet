package me.schill.sr5sheet.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
class Settings {
    @Element
    var currentCharacter: String? = null
}