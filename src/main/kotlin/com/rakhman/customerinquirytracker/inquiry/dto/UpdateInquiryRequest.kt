package com.rakhman.customerinquirytracker.inquiry.dto

import com.rakhman.customerinquirytracker.inquiry.InquiryPriority
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateInquiryRequest (
    @field:NotBlank
    @field:Size(min = 3, max = 200)
    val title: String,

    @field:NotBlank
    @field:Size(max = 100)
    val customerName: String,

    @field:NotBlank
    @field:Email
    @field:Size(min = 3, max = 254)
    val customerEmail: String,

    @field:Size(max = 5000)
    val description: String?,

    val priority: InquiryPriority
    )

