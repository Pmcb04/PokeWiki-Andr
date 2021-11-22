package com.devgram.pokewiki.listeners

import com.devgram.pokewiki.model.Achievement

interface SelectionListenerAchievement {
    fun onListItemSelected(item: Achievement?)
}