/*
 * Copyright (c) 2023 Krysztal112233
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val targetJavaVersion = 17
val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("org.jetbrains.dokka") version "1.9.0"
}

val folia: String by project
val placeholder_api: String by project
val fastutil: String by project
val guava: String by project
val nbt_api: String by project

dependencies {
    paperweight.foliaDevBundle("${folia}-R0.1-SNAPSHOT")
    implementation(kotlin("script-runtime"))

}

configurations.configureEach {
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
        maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    }

    subprojects {
        apply(plugin = "com.github.johnrengelman.shadow")
        apply(plugin = "io.papermc.paperweight.userdev")
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.dokka")

        dependencies {

            compileOnly("org.projectlombok:lombok:1.18.28")
            annotationProcessor("org.projectlombok:lombok:1.18.28")

            testCompileOnly("org.projectlombok:lombok:1.18.28")
            testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

            compileOnly("com.google.guava:guava:${guava}")
            compileOnly("me.clip:placeholderapi:${placeholder_api}")
            compileOnly("it.unimi.dsi:fastutil:${fastutil}")

            paperweight.foliaDevBundle("${folia}-R0.1-SNAPSHOT")

        }

        tasks.shadowJar {
            minimize()
        }
    }
}

