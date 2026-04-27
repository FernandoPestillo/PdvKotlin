package com.example.pdvkotlin.features.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdvkotlin.domain.model.ReportSummary
import com.example.pdvkotlin.domain.usecase.GetReportSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportSummaryUseCase: GetReportSummaryUseCase,
) : ViewModel() {
    private val _summary = MutableStateFlow(ReportSummary(0.0, 0, 0.0, emptyList()))
    val summary: StateFlow<ReportSummary> = _summary

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { _summary.value = getReportSummaryUseCase() }
    }
}

