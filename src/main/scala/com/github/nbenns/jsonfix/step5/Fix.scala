package com.github.nbenns.jsonfix.step5

import scala.language.higherKinds

case class Fix[F[_]](unFix: F[Fix[F]])
