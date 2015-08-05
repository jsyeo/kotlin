/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.preprocessor.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.*
import java.io.File
import org.jetbrains.kotlin.preprocessor.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Mojo(name = "preprocess")
public class PreprocessorMojo : AbstractMojo() {

    @Parameter
    public var source: File? = null

    @Parameter
    public var output: String? = null

    @Parameter
    public var profiles: Map<String, File?> = emptyMap()


    override fun execute() {
        val source = requireNotNull(source, "source")
        val profiles = profiles.entrySet().map { createProfile(it.key, getProfileTargetPath(it.key, it.value)) }
        log.info("Preprocessing sources from $source")
        profiles.forEach { with(it) { log.info("Profile $name to $targetRoot") } }

        val logger = MavenLogger(log)
        val pool = Executors.newCachedThreadPool()
        profiles.forEach { pool.submit { Preprocessor(logger.withPrefix(it.name)).processSources(source, it) } }

        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

    private fun getProfileTargetPath(key: String, value: File?): File {
        val output = output ?: return value ?: throw IllegalArgumentException("Output path for profile '$key' is not specified")
        val profilePlaceholder = "\$profile"
        val outputDir = if (output.contains(profilePlaceholder))
            File(output.replace(profilePlaceholder, key.toLowerCase()))
        else
            File(output, key.toLowerCase())
        return value ?: outputDir
    }

}
