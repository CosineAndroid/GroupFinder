package kr.cosine.groupfinder.presentation.view.search

val micTags = listOf("마이크X", "마이크O", "상관없음")
val styleTags = listOf("빡겜", "즐겜", "욕X", "듣톡", "채팅X", "빡겜", "즐겜", "욕X", "듣톡", "채팅X", "빡겜", "즐겜", "욕X", "듣톡", "채팅X")

data class Tag(val tag: String)

object Tags {
    var tagList : MutableList<Tag> = mutableListOf()

    fun addTag(tag: Tag){
        tagList.add(tag)
    }

    fun deleteTag(tag: Tag){
        tagList.remove(tag)
    }

    fun getTag(tag: Tag): MutableList<Tag>{
        return tagList
    }
}