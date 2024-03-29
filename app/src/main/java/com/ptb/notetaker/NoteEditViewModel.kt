package com.ptb.notetaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteEditViewModel : ViewModel() {
    private val _notePosition : MutableLiveData<Int> = MutableLiveData(POSITION_NOT_SET)
    val notePosition: LiveData<Int> get() = _notePosition

    private val _saveRequest = MutableLiveData<Boolean>()
    val saveRequest: LiveData<Boolean> get() = _saveRequest

    fun updateNotePosition(position: Int) {
        _notePosition.value = position
    }

    fun triggerSave() {
        _saveRequest.value = true
    }
    fun triggerSaveComplete() {
        _saveRequest.value = false
    }
}