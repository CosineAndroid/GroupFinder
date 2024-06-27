package kr.cosine.groupfinder.presentation.view.record.state.item

import kr.cosine.groupfinder.enums.Rank

class RankMap(
    rankMap: Map<Rank, RankItem>
) : Map<Rank, RankItem> by rankMap