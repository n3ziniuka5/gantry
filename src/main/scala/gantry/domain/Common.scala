package gantry.domain

import org.virtuslab.yaml.YamlEncoder

enum Protocol:
    case TCP, UDP, SCTP

object Protocol:
    given YamlEncoder[Protocol] = YamlEncoder.forString.mapContra(_.toString)
