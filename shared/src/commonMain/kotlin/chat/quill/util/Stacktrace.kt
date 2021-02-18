package chat.quill.util

data class KMPStackElement(
  val fileName: String?,
  val className: String?,
  val methodName: String?,
)

expect fun getKMPStack(): List<KMPStackElement>
