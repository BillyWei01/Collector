package com.horizon.collector.config

import com.horizon.base.config.GlobalConfig


object PathManager {
    val filesDir: String = GlobalConfig.getAppContext().filesDir.absolutePath
    val fastKVDir: String = "$filesDir/fastkv"
}