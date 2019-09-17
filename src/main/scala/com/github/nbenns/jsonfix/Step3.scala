package com.github.nbenns.jsonfix

import step3._
import step3.Json._

object Step3 extends App {
  // This type doesn't look good
  val a: Json[Json[Any]] = jsonObject(
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

  // How do we even define this Encoder?
  val out: String = Encoder[Json[Json[Any]]].encode(a)
  println(out)
}
