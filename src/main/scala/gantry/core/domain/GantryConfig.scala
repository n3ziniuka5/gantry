package gantry.core.domain

import pureconfig.ConfigReader
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto.deriveReader

case class GantryConfig(name: String, image: GantryConfig.Image, ports: List[GantryConfig.Port])

object GantryConfig:
    given ConfigReader[GantryConfig] = deriveReader // auto derives doesn't support default values

    enum Protocol:
        case TCP, UDP, SCTP

        def toModel: gantry.core.domain.Protocol = this match
            case TCP  => gantry.core.domain.Protocol.TCP
            case UDP  => gantry.core.domain.Protocol.UDP
            case SCTP => gantry.core.domain.Protocol.SCTP

    object Protocol:
        given ConfigReader[Protocol] = ConfigReader.fromString { s =>
            Try(Protocol.valueOf(s.toUpperCase)) match
                case Success(value) => Right(value)
                case Failure(exception) =>
                    Left(
                      CannotConvert(
                        s,
                        "Protocol",
                        s"Invalid protocol. Supported protocols: ${Protocol.values.mkString(", ")}"
                      )
                    )
        }

    case class Image(repository: String, tag: String)
    object Image:
        given ConfigReader[Image] = deriveReader[Image] // auto derives doesn't support default values

    case class Port(
        name: String,
        port: Int,
        protocol: Protocol = Protocol.TCP
    )
    object Port:
        given ConfigReader[Port] = deriveReader[Port] // auto derives doesn't support default values
