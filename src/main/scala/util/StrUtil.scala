package util

import com.knuddels.jtokkit.api.IntArrayList
import org.apache.hadoop.io.Text

class StrUtil {
  private def clean(v: Text): String = {
    var clean = v.toString.trim.toLowerCase.replaceAll("[,./?\"{}()~+@!#$%^&*:;0-9']", "")
    clean = clean.replace("[", "")
    clean = clean.replace("]", "")
    clean = clean.replaceAll("-+", "")
    clean
  }
  def cleanTextToWords(v: Text): Array[String] = {
    val clean = this.clean(v)
    clean.split("\\s+")
  }
  def encoding2String(encoded: IntArrayList): String = {
    val res = encoded.toArray.mkString(":")
    res
  }
  def decodeEncoding2String(strEncoded: String): IntArrayList = {
    val res = new IntArrayList()
    val x = strEncoded.split(":")
    x.foreach(i => {
      res.add(i.toInt)
    })
    res
  }
}
