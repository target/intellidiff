@file:Suppress("SpellCheckingInspection")

package com.target.diff.writer

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class DeserializedObjectTest {
  @Test
  fun `no diff found for Kotlin objects with different object instances`() {
    val expected = Something.Hi
    val actual = serializeAndDeserialize(Something.Hi)

    Assert.assertTrue(expected != actual)

    Assert.assertTrue(
      IntelliDiff().calculateDiff(
        null,
        expected,
        actual
      ) is IntelliDiff.DiffResult.Pass
    )
  }

  sealed class Something : Serializable {
    object Hi : Something()
  }

  private fun serializeAndDeserialize(any: Any): Any {
    return deserialize(ByteArrayInputStream(serialize(any)))
  }

  private fun deserialize(inputStream: InputStream): Any {
    return ObjectInputStream(inputStream).readObject()
  }

  private fun serialize(any: Any): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val out = ObjectOutputStream(outputStream)
    out.writeObject(any)
    out.flush()
    return outputStream.toByteArray()
  }
}
