package kr.cosine.groupfinder.presentation.view.record.state.item.extension

import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem

val RankItem.rating get() = ((win / (win + lose).toDouble()) * 100).toInt()