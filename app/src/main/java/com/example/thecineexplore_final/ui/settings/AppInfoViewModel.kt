package com.example.thecineexplore_final.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppInfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Projekt The Cine Explore"
    }
    val text: LiveData<String> = _text
}