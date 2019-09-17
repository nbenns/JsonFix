package com.github.nbenns.jsonfix.step6

import cats.Functor
import cats.implicits._

import scala.language.higherKinds

case class Fix[F[_]](unFix: F[Fix[F]])

object Fix {
  /*
    foldRight has now become cataMorphism and has been made more Generic.
   */
  def cataMorphism[F[_]: Functor, A](fAlgebra: FAlgebra[F, A])(fixF: Fix[F]): A =
    fAlgebra(fixF.unFix.map(cataMorphism(fAlgebra)))

  implicit class FixOps[F[_]: Functor](ff: Fix[F]) {
    def cata[A](f: FAlgebra[F, A]): A = cataMorphism[F, A](f)(ff)
  }
}
