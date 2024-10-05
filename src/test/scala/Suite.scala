import CosineSimilarity.CosSimMapper
import EmbeddingTask.EmbeddingReducer
import com.knuddels.jtokkit.api.IntArrayList
import org.apache.hadoop.io.Text
import util.StrUtil

class Suite extends munit.FunSuite {
  private val csMapper = new CosSimMapper
  private val strUtil = new StrUtil
  private val embeddingReducer = new EmbeddingReducer

  test("converting word:Vec pair to vec test") {
    val actual = csMapper.convertWordVecToPair("word:[1,2,3,4,5,6]")
    val goal = Array[Double](1,2,3,4,5,6)
    assertEquals(actual._1, "word")
    for(i <- 0 until 6) {
      assertEquals(goal(i), actual._2(i))
    }
  }
  test("0 cosine similarity test") {
    val actual = csMapper.similarity("w1:[0,0,1]", "w2:[1,0,0]")
    assertEquals(("w1", "w2"), actual._1)
    assertEquals(0D, actual._2)
  }
  test("non zero cosine similarity test") {
    val actual = csMapper.similarity("w1:[1,1,1]", "w2:[1,0,0]")
    assertEquals(("w1", "w2"), actual._1)
    assertEquals(1 / math.sqrt(3), actual._2)
  }
  test("test util string cleaning") {
    val actual = strUtil.cleanTextToWords(new Text("Hello123! tHis Is a Very ~.,unclean: Sentence<>[])(*&^%$#@!/?"))
    val goal = "hello this is a very unclean sentence".split(" ")
    for(i <- 0 until 7) {
      assertEquals(actual(i), goal(i))
    }
  }
  test("test token encoding conversion") {
    val actual = strUtil.decodeEncoding2String("1:209:234:87")
    val expected = Array(1,209,234,87)
    assert(actual.toArray.sameElements(expected))
  }
  test("test encoding to string conversion") {
    val l = new IntArrayList()
    l.add(1)
    l.add(2)
    l.add(3)
    val actual = strUtil.encoding2String(l)
    val expected = "1:2:3"
    assertEquals(actual, expected)
  }
  test("test converting string to vector") {
    val actual = embeddingReducer.convertVec2Arr("1,2,3,4,5,6")
    val expected = Array(1D,2D,3D,4D,5D,6D)
    assert(actual.sameElements(expected))
  }
}
