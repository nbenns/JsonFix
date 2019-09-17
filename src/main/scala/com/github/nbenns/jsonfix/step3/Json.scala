package com.github.nbenns.jsonfix.step3

import com.github.nbenns.jsonfix.Encoder

sealed trait Json[+A] extends Product with Serializable

object Json {
  /*
    Our first Naive attempt, this is seems natural, you want to make the types of "Json"
    equal the types of Scala... we will see that this isn't really going to help us.
   */
  private case object JsonNull extends Json[Nothing]
  private final case class JsonBoolean(value: Boolean) extends Json[Boolean]
  private final case class JsonNumber(value: Double) extends Json[Double]
  private final case class JsonString(value: String) extends Json[String]
  private final case class JsonArray[A](value: List[A]) extends Json[A]
  private final case class JsonObject[A](value: List[(String, A)]) extends Json[A]

  def Null: Json[Nothing] = JsonNull
  def Boolean(value: Boolean): Json[Boolean] = JsonBoolean(value)
  def Number(value: Double): Json[Double] = JsonNumber(value)
  def String(value: String): Json[String] = JsonString(value)
  def Array[A](value: Json[A]*): Json[Json[A]] = JsonArray(value.toList)
  def Object[A](value: (String, Json[A])*): Json[Json[A]] = JsonObject(value.toList)

  /*
    What do we even do here?
   */
  def foldRight[A, B](f: Json[A] => B)(j: Json[A]): B = ???

  /*
    This is weird because how do we convert an A to "something"
    Sure its ok for String because we can "toString" anything,
    but what if it was Int, or Double?
   */
  def toStringAlg[A]: Json[A] => String = {
    case JsonNull => "null"
    case JsonBoolean(b) => b.toString
    case JsonNumber(n) => n.toString
    case JsonString(s) => s""""$s""""
    case JsonArray(l: List[A]) => l.map(_.toString).mkString(", ")
    case JsonObject(l: List[(String, A)]) => l.map { case (k, v) => s""""$k": ${v}"""}.mkString(", ")
  }

  /*
    You would think we might be able to define a Functor on a type of Json[A]
    But looking at how we would have to implement it, it doesn't really work.

  implicit def jsonFunctor: Functor[Json] = new Functor[Json] {
    override def map[A, B](fa: Json[A])(f: A => B): Json[B] = fa match {
      case JsonNull => JsonNull                                           <-- this is Json[Nothing] ok if A is Covariant
      case JsonBoolean(b) => JsonBoolean(b)                               <-- this is Json[Boolean] not Json[B]
      case JsonNumber(n) => JsonNumber(n)                                 <-- this is Json[Double] not Json[B]
      case JsonString(s) => JsonString(s)                                 <-- this is Json[String] not Json[B]
      case JsonArray(l) => JsonArray(l.map(f))                            <-- this is Json[B]
      case JsonObject(l) => JsonObject(l.map { case (k, v) => (k, f(v))}) <-- this is Json[B]
    }
  }
  */

  implicit def encoderJson[A]: Encoder[Json[A]] = foldRight[A, String](toStringAlg)
}
