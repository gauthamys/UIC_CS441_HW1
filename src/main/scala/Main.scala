import com.knuddels.jtokkit.*
import com.knuddels.jtokkit.api.*

object Main {
  def main(args: Array[String]): Unit = {
    val registry = Encodings.newDefaultEncodingRegistry
    val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
    val encoded = encoding.encode("This is a sample sentence.")
    println(encoded)
    val decoded = encoding.decode(encoded)
    println(decoded)
  }
}