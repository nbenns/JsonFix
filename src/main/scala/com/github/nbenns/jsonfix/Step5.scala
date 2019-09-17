package com.github.nbenns.jsonfix

import step5._
import step5.Json._

object Step5 extends App {
  // That's a lot better
  val a: Fix[Json] = jsonObject(
    "boolean" -> jsonBoolean(true),
    "number" -> jsonNumber(2.0),
    "string" -> jsonString("bob"),
    "nullValue" -> jsonNull,
    "array" -> jsonArray(jsonNumber(1), jsonString("2")),
    "object" -> jsonObject(
      "one" -> jsonNumber(1),
      "two" -> jsonNumber(2)
    )
  )

  // We can have an Encoder again!
  val out: String = Encoder[Fix[Json]].encode(a)
  println(out)
}
