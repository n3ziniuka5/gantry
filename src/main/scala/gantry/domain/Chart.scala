package gantry.domain

import org.virtuslab.yaml.YamlCodec

case class Chart(apiVersion: String, name: String, `type`: String, version: String, appVersion: String)
    derives YamlCodec

object Chart:
    val fileName = "Chart.yaml"

    def generate(config: GantryConfig): (fileName: String, value: Chart) =
        val chartVersion = if config.image.tag == "latest" then "0.1.0" else config.image.tag

        (
          fileName,
          Chart(
            apiVersion = "v2",
            name = config.name,
            `type` = "application",
            version = chartVersion,
            appVersion = config.image.tag
          )
        )
