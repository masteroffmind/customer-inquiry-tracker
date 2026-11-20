package com.rakhman.customerinquirytracker.inquiry.dto

import com.rakhman.customerinquirytracker.inquiry.InquiryStatus

data class ChangeStatusRequest (
    val status: InquiryStatus
)