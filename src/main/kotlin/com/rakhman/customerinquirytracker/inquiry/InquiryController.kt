package com.rakhman.customerinquirytracker.inquiry

import com.rakhman.customerinquirytracker.inquiry.dto.CreateInquiryRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * InquiryController.create(request) принимает данные из JSON и передаёт их сервису.
 * InquiryService.create(title, customerName, customerEmail) создаёт и сохраняет обращение.
 */
@RestController
@RequestMapping("/api/v1/inquiries")
class InquiryController (
    private val inquiryService: InquiryService
){
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateInquiryRequest): Inquiry {
        return inquiryService.create(title = request.title, customerName = request.customerName, customerEmail = request.customerEmail)
    }
}
