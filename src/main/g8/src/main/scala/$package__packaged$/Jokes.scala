package $package$

import cats.effect.IO
import cats.implicits._
import io.circe.{Decoder, Encoder}
import io.circe._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.io._
import org.http4s.implicits._

trait Jokes:
  def get: IO[Jokes.Joke]

object Jokes:
  final case class Joke(joke: String) 
  
  object Joke:
    given Decoder[Joke] = Decoder.derived[Joke]
    given EntityDecoder[IO, Joke] = jsonOf
    given Encoder[Joke] = Encoder.AsObject.derived[Joke]
    given EntityEncoder[IO, Joke] = jsonEncoderOf

  final case class JokeError(e: Throwable) extends RuntimeException

  def impl(C: Client[IO]): Jokes = new Jokes:
    def get: IO[Jokes.Joke] = {
      C.expect[Joke](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => JokeError(t)} // Prevent Client Json Decoding Failure Leaking
    }
