package kr.cosine.groupfinder.presentation.view.search

val micTags = listOf("마이크X", "마이크O", "상관없음")
val styleTags = listOf("빡겜", "즐겜", "욕X", "듣톡", "채팅X")

data class Tag(val tag: String)

object Tags {
//    var List : MutableList<Tag> = mutableListOf()
    var selectedTagList = mutableListOf("마이크X", "빡겜", "듣톡")

    fun addTag(tag: CharSequence){
        selectedTagList.add(tag.toString())
    }

    fun deleteTag(tag: Tag){
        selectedTagList.remove(tag.toString())
    }

    fun getTag(tag: Tag): MutableList<String>{
        return selectedTagList
    }
}