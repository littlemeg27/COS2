@file:Suppress("UnstableApiUsage", "UnstableApiUsage", "UnstableApiUsage", "UnstableApiUsage",
    "RedundantSuppression", "RedundantSuppression", "RedundantSuppression"
)

import org.gradle.internal.impldep.org.jsoup.safety.Safelist.basic


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        }
    }


develocity {
    buildScan {
        develocity.buildScan.termsOfUseUrl
        develocity.buildScan.termsOfUseAgree
    }
}

plugins {
    id("com.gradle.develocity") version "3.17.5"
}
develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"

        publishing.onlyIf { false }
    }
}



rootProject.name = "CrimeShield"
include(":app")
