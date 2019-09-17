package com.github.nbenns.jsonfix

import step4._
import step4.Json._

object Step4 extends App {
  // This type looks worse
  val a: Json[Json[Json[Any]]] = jsonObject(
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

  // Still got a problem here
  val out: String = Encoder[Json[Json[Json[Any]]]].encode(a)
  println(out)

  /*
    We really have a problem... our type runs away from us, expanding recursively
    We need to stop this somehow.
   */
}
