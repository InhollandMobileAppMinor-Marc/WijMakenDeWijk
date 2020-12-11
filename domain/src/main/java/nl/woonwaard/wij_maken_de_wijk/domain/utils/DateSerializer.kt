package nl.woonwaard.wij_maken_de_wijk.domain.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

object DateSerializer : KSerializer<Date> {
    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = parser.parse(decoder.decodeString()) ?: Date()

    override fun serialize(encoder: Encoder, value: Date) {
        return encoder.encodeString(parser.format(value))
    }
}
