pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":auth:app",
        ":appcheck:app",
        ":config:app",
        ":database:app",
        ":dynamic-links:app",
        ":dl-invites:app",
        ":firebaseoptions:app",
        ":functions:app",
        ":firestore:app",
        ":storage:app",
        ":tasks:app",
        ":inappmessaging:app",
        ":admob:app",
        ":messaging:app",
        ":crashlytics:app",
        ":perf:app",
        ":test-lab:app",
        ":analytics:app",
        ":installations:app",
        ":vertexai:app"
)

