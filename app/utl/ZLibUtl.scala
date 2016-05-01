package utl

import java.util.zip.{Deflater, Inflater}

object ZLibUtl {

  def compress(input: Array[Byte]): Array[Byte] = {
    val deflater = new Deflater()
    deflater.setInput(input)
    deflater.finish

    val compressedData = new Array[Byte](input.size * 2)

    compressedData.take(
      deflater.deflate(compressedData))
  }

  def decompress(input: Array[Byte]): String = {
    val inflater = new Inflater()
    inflater.setInput(input)

    val decompressedData = new Array[Byte](input.size * 4)

    new String(
      decompressedData.take(
        inflater.inflate(decompressedData))
    )
  }

}
