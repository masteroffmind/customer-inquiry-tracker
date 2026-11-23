package com.rakhman.customerinquirytracker.inquiry.dto

data class InquiryPageResponse (
    val content: List<InquiryResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)