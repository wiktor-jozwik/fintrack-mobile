package com.example.moneytracker.service.repository.exchangerate

import javax.inject.Inject

class ExchangerateRepository @Inject constructor(
    private val exchangerateApi: ExchangerateApi
) {}