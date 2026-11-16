package com.rakhman.customerinquirytracker.inquiry.dto

import com.rakhman.customerinquirytracker.inquiry.InquiryPriority
import com.rakhman.customerinquirytracker.inquiry.InquiryStatus
import java.time.Instant

data class InquiryResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: InquiryStatus,
    val priority: InquiryPriority,
    val customerName: String,
    val customerEmail: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
