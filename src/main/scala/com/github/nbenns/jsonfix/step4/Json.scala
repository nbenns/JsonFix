package com.github.nbenns.jsonfix.step4

import cats.Functor
import com.github.nbenns.jsonfix.Encoder

sealed trait Json[+A] extends Product with Serializable

object Json {
  /*
    Instead of A equaling the inner type, lets leave it existential (unset)
    This might give us issues, but lets see what happens
   */
  private case class JsonNull[A]() extends Json[A]
  private final case class JsonBoolean[A](value: Boolean) extends Json[A]
  private final case class JsonNumber[A](value: Double) extends Json[A]
  private final case class JsonString[A](value: String) extends Json[A]
  private final case class JsonArray[A](value: List[A]) extends Json[A]
  private final case class JsonObject[A](value: List[(String, A)]) extends Json[A]

  def Null[A]: Json[A] = JsonNull()
  def Boolean[A](value: Boolean): Json[A] = JsonBoolean(value)
  def Number[A](value: Double): Json[A] = JsonNumber(value)
  def String[A](value: String): Json[A] = JsonString(value)
  def Array[A](value: Json[A]*): Json[Json[A]] = JsonArray(value.toList)
  def Object[A](value: (String, Json[A])*): Json[Json[A]] = JsonObject(value.toList)

  /*
    Still having trouble... A keeps changing on us!
   */
  def foldRight[A, B](f: Json[A] => B)(j: Json[A]): B = ???

  /*
    This is weird because how do we convert an A to "something"
    Sure its ok for String because we can "toString" anything,
    but what if it was Int, or Double?
   */
  def toStringAlg[A]: Json[String] => String = {
    case JsonNull() => "null"
    case JsonBoolean(b) => b.toString
    case JsonNumber(n) => n.toString
    case JsonString(s) => s""""$s""""
    case JsonArray(l) => l.map(_.toString).mkString(", ")
    case JsonObject(l) => l.map { case (k, v) => s""""$k": ${v}"""}.mkString(", ")
  }

  /*
    We can now define a Functor instance!
  */
  implicit def jsonFunctor: Functor[Json] = new Functor[Json] {
    override def map[A, B](fa: Json[A])(f: A => B): Json[B] = fa match {
      case JsonNull() => JsonNull()
      case JsonBoolean(b) => JsonBoolean(b)
      case JsonNumber(n) => JsonNumber(n)
      case JsonString(s) => JsonString(s)
      case JsonArray(l) => JsonArray(l.map(f))
      case JsonObject(l) => JsonObject(l.map { case (k, v) => (k, f(v))})
    }
  }

  implicit def encoderJson[A]: Encoder[Json[A]] = ???
}
