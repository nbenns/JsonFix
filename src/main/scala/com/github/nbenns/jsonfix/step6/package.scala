package com.github.nbenns.jsonfix
import scala.language.higherKinds

package object step6 {
  type FAlgebra[F[_], A] = F[A] => A
  type Json = Fix[JsonF]
}
