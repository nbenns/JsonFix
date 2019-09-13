package com.github.nbenns

import scala.language.higherKinds

package object jsonfix {
  type FAlgebra[F[_], A] = F[A] => A
  type Json = Fix[JsonF]

  implicit val fixJsonEnc: Encoder[Json] = _.cata(JsonF.toStringFAlg)
}
