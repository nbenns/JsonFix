package com.github.nbenns.jsonfix

import step1._
import step1.Json._

object Step1 extends App {
  val a: Json = jsonObject(
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

  val out: String = Encoder[Json].encode(a)
  println(out)
}
