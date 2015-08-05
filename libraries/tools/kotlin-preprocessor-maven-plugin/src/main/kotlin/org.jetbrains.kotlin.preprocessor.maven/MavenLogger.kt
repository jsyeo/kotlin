package org.jetbrains.kotlin.preprocessor.maven

import org.apache.maven.plugin.logging.Log
import org.jetbrains.kotlin.preprocessor.Logger

public class MavenLogger(val log: Log): Logger {
    override fun debug(msg: CharSequence) = log.debug(msg)
    override fun info(msg: CharSequence) = log.info(msg)
    override fun warn(msg: CharSequence) = log.warn(msg)
    override fun error(msg: CharSequence) = log.error(msg)
}