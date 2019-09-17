package com.github.nbenns.jsonfix.step1

import com.github.nbenns.jsonfix.Encoder

sealed trait Json extends Product with Serializable

object Json {
  private case object JsonNull extends Json
  private final case class JsonBoolean(value: Boolean) extends Json
  private final case class JsonNumber(value: Double) extends Json
  private final case class JsonString(value: String) extends Json
  private final case class JsonArray(value: List[Json]) extends Json
  private final case class JsonObject(value: List[(String, Json)]) extends Json

  def jsonNull: Json = JsonNull
  def jsonBoolean(value: Boolean): Json = JsonBoolean(value)
  def jsonNumber(value: Double): Json = JsonNumber(value)
  def jsonString(value: String): Json = JsonString(value)
  def jsonArray(value: Json*): Json = JsonArray(value.toList)
  def jsonObject(value: (String, Json)*): Json = JsonObject(value.toList)

  /*
    Here he are doing recursion on Array and on Object.
    In every implementation of a typeclass over our type we would need to implement
    the same recursion...
    It would be nice to factor it out... note that folds are the way to remove recursion
    and use the datatype to abstract it away.
    FoldLeft restricts us, since it provides only works on linear data structures.
    You cannot foldLeft on a Tree, since it has multiple branches and how do you create a value for a branch?
    You would need to know the values of both the leaves...
    FoldRight on the other hand, starts at the leaves, provides the empty value for Nil and then joins the structure
    along the way.  So in step 2 lets implement foldRight.
   */
  implicit val encoderJson: Encoder[Json] = {
    case JsonNull => "null"
    case JsonBoolean(v) => v.toString
    case JsonNumber(v) => v.toString
    case JsonString(v) => s""""$v""""
    case JsonArray(v) =>
      val elements = v.map(encoderJson.encode).mkString(", ")
      s"[ $elements ]"
    case JsonObject(v) =>
      val pairs = v.map { case (k, v) => s""""$k": ${encoderJson.encode(v)}""" }.mkString(", ")
      s"{ $pairs }"
  }
}
