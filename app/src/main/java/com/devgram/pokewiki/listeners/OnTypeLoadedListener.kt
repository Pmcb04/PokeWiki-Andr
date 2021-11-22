package com.devgram.pokewiki.listeners

import com.devgram.pokewiki.model.TypeInfo

interface OnTypeLoadedListener {
    fun onTypeLoaded(typeInfo: TypeInfo?)
}