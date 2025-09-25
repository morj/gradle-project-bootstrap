package io.github.morj

import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension
import org.gradle.api.initialization.Settings
import java.io.File
import java.util.*

// TODO: convert to plugin?
fun Settings.enableGE(domain: String, defaultKey: String = "") {
    extensions.configure<GradleEnterpriseExtension>(GradleEnterpriseExtension.NAME) {
        // TODO: support GitHub
        val isCIRun = System.getenv("TEAMCITY_VERSION") != null
        val startParameter = gradle.startParameter
        val scanJournal = File(settingsDir, "scan-journal.log")

        server = "https://$domain"
        if (System.getenv("GRADLE_ENTERPRISE_ACCESS_KEY") == null) {
            accessKey = getLocallyProvisionedAccessKey(domain) ?: defaultKey
        }

        buildScan {
            isUploadInBackground = !isCIRun

            // obfuscate NIC since we don't want to expose user real IP (will be relevant without VPN)
            obfuscation {
                ipAddresses { addresses -> addresses.map { _ -> "0.0.0.0" } }
            }

            capture {
                isTaskInputFiles = true
            }

            buildScanPublished {
                scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
            }

            publishAlways()
        }
    }
}

fun getLocallyProvisionedAccessKey(domain: String): String? {
  val properties = Properties()
  val file = File(System.getProperty("user.home"), ".gradle/enterprise/keys.properties")
  return if (file.exists()) {
    file.inputStream().use { input ->
      properties.load(input)
    }
    properties[domain].toString()
  } else null
}
