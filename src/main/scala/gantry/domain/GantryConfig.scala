package gantry.domain

import org.virtuslab.yaml.YamlCodec

case class GantryConfig(name: String, image: GantryConfig.Image) derives YamlCodec

object GantryConfig:
    case class Image(repository: String, tag: String) derives YamlCodec
