package com.github.nbenns.jsonfix
import com.github.nbenns.jsonfix.step6.JsonF.{JsonArray, JsonBoolean, JsonNull, JsonNumber, JsonObject, JsonString}

import scala.language.higherKinds

package object step6 {
  type FAlgebra[F[_], A] = F[A] => A

  type Json = Fix[JsonF]
  object Json {
    def Null: Json = Fix(JsonNull())
    def Boolean(value: Boolean): Json = Fix(JsonBoolean(value))
    def Number(value: Double): Json = Fix(JsonNumber(value))
    def String(value: String): Json = Fix(JsonString(value))
    def Array(value: Json*): Json = Fix(JsonArray(value.toList))
    def Object(value: (String, Json)*) = Fix(JsonObject(value.toList))
  }
}
