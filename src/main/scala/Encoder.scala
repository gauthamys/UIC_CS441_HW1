import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.*
object Encoder {
  private val registry: EncodingRegistry = Encodings.newDefaultEncodingRegistry
  private val encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  def encode(input: String): IntArrayList = encoding.encode(input)
  def decode(encoded: IntArrayList): String = encoding.decode(encoded)
  
}
