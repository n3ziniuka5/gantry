package gantry.domain

object Helpers:
    val fileName    = "templates/_helpers.tpl"
    val includeName = "{{ include \"gantry.fullname\" . }}"

    val selectorLabels = Map(
      "app.kubernetes.io/name"     -> includeName,
      "app.kubernetes.io/instance" -> "{{ .Release.Name }}"
    )

    val commonLabels = Map(
      "app.kubernetes.io/version"    -> "{{ .Chart.AppVersion | quote }}",
      "app.kubernetes.io/managed-by" -> "HelmViaGantry"
    ) ++ selectorLabels

    def generate(config: GantryConfig): (fileName: String, value: String) =
        (
          fileName,
          s"""
      |{{- define "gantry.fullname" -}}
      |{{- if eq .Chart.Name .Release.Name }}
      |{{- .Release.Name | trunc 63 | trimSuffix "-" }}
      |{{- else }}
      |{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" }}
      |{{- end }}
      |{{- end }}""".stripMargin
        )
