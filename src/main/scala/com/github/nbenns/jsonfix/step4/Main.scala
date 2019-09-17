package com.github.nbenns.jsonfix.step4

import com.github.nbenns.jsonfix.Encoder

object Main extends App {
  // This type looks worse
  val a: Json[Json[Json[Any]]] = Json.Object(
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

  // Still got a problem here
  val out: String = Encoder[Json[Json[Json[Any]]]].encode(a)
  println(out)

  /*
    We really have a problem... our type runs away from us, expanding recursively
    We need to stop this somehow.
   */
}
