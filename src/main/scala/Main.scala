import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.*

object Main {
  def main(args: Array[String]): Unit = {
    val registry = Encodings.newDefaultEncodingRegistry
    val encoding = registry.getEncoding(EncodingType.CL100K_BASE)
    val text = "this is a sentence!"
    val encoded = encoding.encode(text)
    
  }
}