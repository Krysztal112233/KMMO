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

package dev.krysztal.kmmo.core

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.repository.RemoteRepository

@SuppressWarnings("all")
class KMMOCorePluginLoader() : PluginLoader {
    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        val mvnRepository = with(MavenLibraryResolver()) {
            listOf(
                Triple("JitPack", "default", "https://jitpack.io"),
                Triple("Ali Maven Repository", "default", "https://maven.aliyun.com/nexus/content/groups/public/"),
                Triple(
                    "Tencent Maven Repository",
                    "default",
                    "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/",
                ),
            )
                .map { RemoteRepository.Builder(it.first, it.second, it.third).build() }
                .forEach(this::addRepository)

            this
        }
        classpathBuilder.addLibrary(mvnRepository)
    }
}