package com.rakhman.customerinquirytracker.inquiry.dto

import com.rakhman.customerinquirytracker.inquiry.InquiryPriority

data class UpdateInquiryRequest (
    val title: String,
    val customerName: String,
    val customerEmail: String,
    val description: String?,
    val priority: InquiryPriority
    )

