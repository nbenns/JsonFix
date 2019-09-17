package com.github.nbenns.jsonfix.step3

import com.github.nbenns.jsonfix.Encoder

object Main extends App {
  // This type doesn't look good
  val a: Json[Json[Any]] = Json.Object(
    "boolean" -> Json.Boolean(true),
    "number" -> Json.Number(2.0),
    "string" -> Json.String("bob"),
    "nullValue" -> Json.Null,
    "array" -> Json.Array(Json.Number(1), Json.String("2")),
    "object" -> Json.Object(
      "one" -> Json.Number(1),
      "two" -> Json.Number(2)
    )
  )

  // How do we even define this Encoder?
  val out: String = Encoder[Json[Json[Any]]].encode(a)
  println(out)
}
