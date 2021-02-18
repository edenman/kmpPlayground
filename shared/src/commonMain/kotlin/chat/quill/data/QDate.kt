package chat.quill.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

expect class QDate(apiDate: Long) {
  // millis
  val apiDate: Long
  fun iso8601(): String
  // 0-23 in device timezone
  fun getHourOfDay(): Int
  fun truncateTime(): QDate
}

expect fun now(): QDate

// NOTE: if you want to use this in a new file, make sure you put this at the top of the file:
// @file:UseSerializers(QDateNullableSerializer::class)
// OR: for each field, annotate with @Serializable(with=MyExternalSerializer::class) (gross).
@Serializer(forClass = QDate::class)
object QDateNullableSerializer : KSerializer<QDate?> {
  override fun deserialize(decoder: Decoder): QDate? {
    val apiDate = decoder.decodeLong()
    if (apiDate == 0L) {
      return null
    }
    return QDate(apiDate)
  }

  override fun serialize(encoder: Encoder, value: QDate?) {
    encoder.encodeLong(value?.apiDate ?: 0)
  }

  override val descriptor = PrimitiveSerialDescriptor("QDate", PrimitiveKind.LONG)
}

@Serializer(forClass = QDate::class)
object QDateSerializer : KSerializer<QDate> {
  override fun deserialize(decoder: Decoder): QDate {
    val apiDate = decoder.decodeLong()
    if (apiDate == 0L) {
      throw IllegalStateException("Got 0 Date for non-nullable QDate field")
    }
    return QDate(apiDate)
  }

  override fun serialize(encoder: Encoder, value: QDate) {
    encoder.encodeLong(value.apiDate)
  }

  override val descriptor = PrimitiveSerialDescriptor("QDate", PrimitiveKind.LONG)
}

// The slack API uses seconds, our API uses milliseconds.
@Serializer(forClass = QDate::class)
object QDateNullableSerializerFromSeconds : KSerializer<QDate?> {
  override fun deserialize(decoder: Decoder): QDate? {
    val seconds = decoder.decodeLong()
    if (seconds == 0L) {
      return null
    }
    return QDate(seconds * 1000)
  }

  override fun serialize(encoder: Encoder, value: QDate?) {
    val seconds = value?.apiDate ?: 0
    encoder.encodeLong(seconds / 1000)
  }

  override val descriptor = PrimitiveSerialDescriptor("QDate", PrimitiveKind.LONG)
}
