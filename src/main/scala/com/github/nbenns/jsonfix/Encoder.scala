package com.github.nbenns.jsonfix

trait Encoder[A] {
  def encode(a: A): String
}

object Encoder {
  def apply[A](implicit encoder: Encoder[A]): Encoder[A] = encoder
}
