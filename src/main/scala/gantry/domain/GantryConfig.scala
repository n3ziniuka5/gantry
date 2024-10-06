package gantry.domain

import org.virtuslab.yaml.*

case class GantryConfig(name: String, image: GantryConfig.Image, ports: List[GantryConfig.Port]) derives YamlDecoder

object GantryConfig:
    enum Protocol:
        case TCP, UDP, SCTP

        def toModel: gantry.domain.Protocol = this match
            case TCP  => gantry.domain.Protocol.TCP
            case UDP  => gantry.domain.Protocol.UDP
            case SCTP => gantry.domain.Protocol.SCTP

    object Protocol:
        given YamlDecoder[Protocol] = YamlDecoder.forString.map(s => Protocol.valueOf(s.toUpperCase))

    case class Image(repository: String, tag: String) derives YamlDecoder
    case class Port(
        name: String,
        containerPort: Int,
        servicePort: Option[Int],
        protocol: Protocol = Protocol.TCP
    ) derives YamlDecoder
