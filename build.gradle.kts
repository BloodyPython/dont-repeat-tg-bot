import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "org.violetsky"
version = "1.1"

repositories {
    mavenCentral()
}


val telegramBotApiVersion by extra("2.0.2")
val ktormVersion by extra("3.5.0")
val sqliteVersion by extra("3.36.0.3")
val koinVersion by extra("3.2.0")
val loggerVersion by extra("1.7.36")


dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.20")
    // Telegram
    implementation("dev.inmo:tgbotapi:$telegramBotApiVersion")
    // ORM
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-sqlserver:$ktormVersion")
    // DB
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    implementation("com.microsoft.sqlserver:mssql-jdbc:11.2.1.jre8")
    // DI
    implementation("io.insert-koin:koin-core:$koinVersion")
    // Logging
    implementation("org.slf4j:slf4j-log4j12:$loggerVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<org.gradle.jvm.tasks.Jar>() {
    exclude("META-INF/MSFTSIG.RSA", "META-INF/MSFTSIG.SF", "META-INF/MSFTSIG.DSA")
}