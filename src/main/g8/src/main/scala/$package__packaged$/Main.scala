package $package$

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    $name;format="Camel"$Server.run
