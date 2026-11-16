package com.rakhman.customerinquirytracker.inquiry.dto

data class CreateInquiryRequest(
    val title: String,
    val customerName: String,
    val customerEmail: String,
)