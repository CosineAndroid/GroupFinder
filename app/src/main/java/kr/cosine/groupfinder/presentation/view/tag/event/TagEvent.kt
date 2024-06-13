package kr.cosine.groupfinder.presentation.view.tag.event

sealed interface TagEvent {

    data class SetTag(
        val tags: List<String> = emptyList()
    ) : TagEvent

    data class AddTag(
        val tag: String
    ) : TagEvent

    data class RemoveTag(
        val tag: String
    ) : TagEvent
}