package com.example.nychighschools

import com.example.nychighschools.data.Highschool

data class SchoolsState(val schools: List<Highschool>, val isFromForceUpdate: Boolean = false)
