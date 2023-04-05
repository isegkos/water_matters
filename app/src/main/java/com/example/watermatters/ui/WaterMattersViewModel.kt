package com.example.watermatters.ui

import androidx.lifecycle.ViewModel
import com.example.watermatters.model.WMUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WaterMattersViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(WMUiState())
    val uiState: StateFlow<WMUiState> = _uiState.asStateFlow()
}
