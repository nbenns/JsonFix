package com.github.nbenns.jsonfix.step5

import cats.Functor
import cats.implicits._
import com.github.nbenns.jsonfix.Encoder

// We have to go invariant again... Fix doesn't like the covariance
sealed trait Json[A] extends Product with Serializable

object Json {
  private case class JsonNull[A]() extends Json[A]
  private final case class JsonBoolean[A](value: Boolean) extends Json[A]
  private final case class JsonNumber[A](value: Double) extends Json[A]
  private final case class JsonString[A](value: String) extends Json[A]
  private final case class JsonArray[A](value: List[A]) extends Json[A]
  private final case class JsonObject[A](value: List[(String, A)]) extends Json[A]

  /*
    The smart constructors now return Fix[Json], this will now become our Json type.
   */
  def Null: Fix[Json] = Fix(JsonNull())
  def Boolean(value: Boolean): Fix[Json] = Fix(JsonBoolean(value))
  def Number(value: Double): Fix[Json] = Fix(JsonNumber(value))
  def String(value: String): Fix[Json] = Fix(JsonString(value))
  def Array[A](value: Fix[Json]*): Fix[Json] = Fix(JsonArray(value.toList))
  def Object[A](value: (String, Fix[Json])*): Fix[Json] = Fix(JsonObject(value.toList))

  /*
    The implementation seems a little weird, let's explain:
    At first glance, the simplest thing would be f(j.unFix).
    However, the Json inside Fix is existential (Json[?]), we need to have Json[A].
    How do we get that?  We can map over it, but with what,
    How do we get a A from Fix[Json], foldRight obviously!
    The reason this works is because Map doesn't really do anything with A.
    That gives us the Json[A] we are looking for, now we can apply f() to our Json[A] to get A.

    Notice that all of our recursion is in the .map(foldRight(f)) bit, nowhere else does recursion occur.

    This seems like it could be more Generic...
    Maybe next we should move this to Fix?
   */
  def foldRight[A](f: Json[A] => A)(j: Fix[Json]): A = f(j.unFix.map(foldRight(f)))

  /*
    This is weird because how do we convert an A to "something"
    Sure its ok for String because we can "toString" anything,
    but what if it was Int, or Double?
   */
  val toStringAlg: Json[String] => String = {
    case JsonNull() => "null"
    case JsonBoolean(b) => b.toString
    case JsonNumber(n) => n.toString
    case JsonString(s) => s""""$s""""
    case JsonArray(l) => s"[ ${l.map(_.toString).mkString(", ")} ]"
    case JsonObject(l) => s"{ ${l.map { case (k, v) => s""""$k": ${v}"""}.mkString(", ")} }"
  }

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

  implicit def encoderJson[A]: Encoder[Fix[Json]] = foldRight(toStringAlg)
}
