package com.github.nbenns.jsonfix.step1

import com.github.nbenns.jsonfix.Encoder

object Main extends App {
  val a: Json = Json.Object(
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

  val out: String = Encoder[Json].encode(a)
  println(out)
}
