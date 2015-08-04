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

@Mojo(name = "preprocess")
public class PreprocessorMojo : AbstractMojo() {

    @Parameter
    public var source: File? = null

    @Parameter
    public var output: File? = null


    override fun execute() {
        getLog().info("Preprocessing $source to $output")
    }

}
