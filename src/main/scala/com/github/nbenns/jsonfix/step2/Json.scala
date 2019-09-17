package com.github.nbenns.jsonfix.step2

import com.github.nbenns.jsonfix.Encoder

sealed trait Json extends Product with Serializable

object Json {
  private case object JsonNull extends Json
  private final case class JsonBoolean(value: Boolean) extends Json
  private final case class JsonNumber(value: Double) extends Json
  private final case class JsonString(value: String) extends Json
  private final case class JsonArray(value: List[Json]) extends Json
  private final case class JsonObject(value: List[(String, Json)]) extends Json

  val Null: Json = JsonNull
  def Boolean(value: Boolean): Json = JsonBoolean(value)
  def Number(value: Double): Json = JsonNumber(value)
  def String(value: String): Json = JsonString(value)
  def Array(value: Json*): Json = JsonArray(value.toList)
  def Object(value: (String, Json)*): Json = JsonObject(value.toList)

  /*
    We've separated the recursion into foldRight, however the function looks a little weird.
    Because we don't specifically have one type of value, we can't just give it one function from A => B
    Instead we need to know specifically how many types of leaves are in the AST Tree.
    Something different will need to happen in order to continue refactoring.
    In Step3 we will try the opposite, we pattern match the object types instead of foldRight.
    However we can't remove the recursion if JsonArray and JsonObject still contain Json.
    If we make any recursive types contain already processed values,
    we can remove the recursion.
    In order to do this we will have to parameterize our AST with the type it will contain.
   */
  def foldRight[A, B](empty: B)
    (b: Boolean => B, n: Double => B, s: String => B, a: List[B] => B, o: List[(String, B)] => B)
    (j: Json): B = j match {
      case JsonNull => empty
      case JsonBoolean(v) => b(v)
      case JsonNumber(v) => n(v)
      case JsonString(v) => s(v)
      case JsonArray(l) => a(l.map(foldRight(empty)(b, n, s, a, o)))
      case JsonObject(l) => o(l.map { case (k, v) => (k, foldRight(empty)(b, n, s, a, o)(v))})
    }

  implicit val encoderJson: Encoder[Json] = foldRight("null")(
    b => b.toString,
    n => n.toString,
    s => s""""$s"""",
    a => s"[ ${a.mkString(", ")} ]",
    o => s"{ ${o.map { case (k, v) => s""""$k": $v""" }.mkString(", ")} }"
  )
}
