package kr.cosine.groupfinder.presentation.view.tag.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.presentation.view.tag.event.TagEvent

class TagViewModel : ViewModel() {

    private val _event = MutableSharedFlow<TagEvent>()
    val event: SharedFlow<TagEvent> get() = _event.asSharedFlow()

    private val _tags = mutableSetOf<String>()
    val tags: Set<String> get() = _tags

    fun isTagged(tag: String): Boolean {
        return _tags.contains(tag)
    }

    fun setTags(tags: List<String>) = viewModelScope.launch {
        _event.emit(TagEvent.SetTag(tags))
        this@TagViewModel._tags.apply {
            clear()
            addAll(tags)
        }
    }

    fun addTag(tag: String) = viewModelScope.launch {
        _event.emit(TagEvent.AddTag(tag))
        _tags.add(tag)
    }

    fun removeTag(tag: String) = viewModelScope.launch {
        _event.emit(TagEvent.RemoveTag(tag))
        _tags.remove(tag)
    }

    fun clearTags() {
        setTags(emptyList())
    }
}