package com.rakhman.customerinquirytracker.inquiry.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateInquiryRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 200)
    val title: String,

    @field:NotBlank
    @field:Size(min = 3, max = 100)
    val customerName: String,

    @field:NotBlank
    @field:Email
    @field:Size(min = 3, max = 254)
    val customerEmail: String,
)