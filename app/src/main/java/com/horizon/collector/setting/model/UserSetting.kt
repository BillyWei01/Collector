package com.horizon.collector.setting.model

import com.horizon.collector.config.KVData

object UserSetting : KVData("user_setting"){

    var showHidden by boolean("showHidden")
    var huabanChannels by string("huabanChannels")
    var collectPath by string("collectPath")
    var lastShowingFragment by string("lastShowingFragment")
}