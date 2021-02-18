package chat.quill.util

actual fun getKMPStack(): List<KMPStackElement> {
  return Thread.currentThread().stackTrace.map { element ->
    KMPStackElement(
      fileName = element.fileName,
      className = element.className,
      methodName = element.methodName
    )
  }
}
