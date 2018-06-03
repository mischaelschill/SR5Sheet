package me.schill.sr5sheet.model

import java.util.UUID.fromString

object BuiltinDatabase {
	object Properties {
		val metaType = StringPropertyType(
				fromString("087527a3-5099-48fd-beb1-1f7adcb4a481"),
				"Metatyp", "Mensch")
		val karma = IntegerPropertyType(
				fromString("2425b4b0-5353-4316-a4be-90a70f16d71a"),
				"Karma", 0, Int.MAX_VALUE, 0)
		val all = mapOf(
				metaType.id to metaType,
				karma.id to karma)
	}

	object Attributes {
		val strength = AttributeType(fromString("6a9ac523-feb5-4410-87a9-d89b42889077"), "Stärke")
		val all = mapOf(
				strength.id to strength)
	}

	val all =
			Properties.all + Attributes.all
}