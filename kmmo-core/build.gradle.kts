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

import org.jetbrains.dokka.gradle.DokkaTaskPartial

val kmmo_core: String by project
val nbt_api: String by project
val h2: String by project
val kreds: String by project
val kryo: String by project
val groovy: String by project
val guava: String by project

version = kmmo_core

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    compileOnly("org.openjdk.jmh:jmh-core:1.21")
    compileOnly("org.openjdk.jmh:jmh-generator-annprocess:1.21")

    implementation("de.tr7zw:item-nbt-api-plugin:$nbt_api")
    shadow("de.tr7zw:item-nbt-api-plugin:$nbt_api")

    // https://mvnrepository.com/artifact/com.h2database/h2
    compileOnly("com.h2database:h2:${h2}")

    // https://central.sonatype.com/artifact/io.github.crackthecodeabhi/kreds
    compileOnly("io.github.crackthecodeabhi:kreds:${kreds}")

    // https://mvnrepository.com/artifact/com.esotericsoftware/kryo
    compileOnly("com.esotericsoftware:kryo:${kryo}")

    // https://mvnrepository.com/artifact/com.google.guava/guava
    compileOnly("com.google.guava:guava:${guava}")

    // https://mvnrepository.com/artifact/org.apache.groovy/groovy
    compileOnly("org.apache.groovy:groovy:${groovy}")
    testImplementation("org.apache.groovy:groovy:${groovy}")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            mapOf(
                "version" to kmmo_core,
                "h2" to h2,
                "kreds" to kreds,
                "kryo" to kryo,
                "groovy" to groovy,
                "guava" to guava,
            )
        )
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        configureEach {
            moduleName = "KMMO Core"
            includes.from("Module.md")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}