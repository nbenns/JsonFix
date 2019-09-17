package com.github.nbenns.jsonfix.step6

import cats.Functor
import com.github.nbenns.jsonfix.Encoder

/*
 We rename our Functor to JsonF, and put a type alias in a package object
 type Json = Fix[Json]
 */
sealed trait JsonF[A] extends Product with Serializable

object JsonF {
  private final case class JsonNull[A]() extends JsonF[A]
  private final case class JsonBoolean[A](value: Boolean) extends JsonF[A]
  private final case class JsonNumber[A](value: Double) extends JsonF[A]
  private final case class JsonString[A](value: String) extends JsonF[A]
  private final case class JsonArray[A](value: List[A]) extends JsonF[A]
  private final case class JsonObject[A](value: List[(String, A)]) extends JsonF[A]

  def jsonNull: Json = Fix(JsonNull())
  def jsonBoolean(value: Boolean): Json = Fix(JsonBoolean(value))
  def jsonNumber(value: Double): Json = Fix(JsonNumber(value))
  def jsonString(value: String): Json = Fix(JsonString(value))
  def jsonArray(value: Json*): Json = Fix(JsonArray(value.toList))
  def jsonObject(value: (String, Json)*) = Fix(JsonObject(value.toList))

  val toStringFAlg: FAlgebra[JsonF, String] = {
    case JsonNull() => "null"
    case JsonBoolean(v) => v.toString
    case JsonNumber(v) => v.toString
    case JsonString(v) => s""""$v""""
    case JsonArray(v) => s"[ ${v.map(_.toString).mkString(", ")} ]"
    case JsonObject(v) =>
      val pairs = v.map { case (k, v) => s""""$k": $v""" }.mkString(", ")
      s"{ $pairs }"
  }

  implicit val jsonFunctor: Functor[JsonF] = new Functor[JsonF] {
    override def map[A, B](fa: JsonF[A])(f: A => B): JsonF[B] = fa match {
      case JsonNull() => JsonNull()
      case JsonBoolean(v) => JsonBoolean(v)
      case JsonNumber(v) => JsonNumber(v)
      case JsonString(v) => JsonString(v)
      case JsonArray(v) => JsonArray(v.map(f))
      case JsonObject(v) => JsonObject(v.map { case (k, e) => (k, f(e)) })
    }
  }

  implicit val fixJsonEnc: Encoder[Json] = _.cata(JsonF.toStringFAlg)
}
