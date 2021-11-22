package com.devgram.pokewiki.listeners

import com.devgram.pokewiki.model.Move

interface OnMoveLoadedListener {
    fun onMoveLoaded(movements: List<Move>)
}