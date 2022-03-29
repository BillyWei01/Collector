package com.horizon.collector.setting.model

import com.horizon.collector.config.KVData

object UserSetting : KVData("user_setting"){

    var showHidden by boolean("1")
    var huabanChannels by string("2")
    var collectPath by string("4")
    var lastShowingFragment by string("5")
}