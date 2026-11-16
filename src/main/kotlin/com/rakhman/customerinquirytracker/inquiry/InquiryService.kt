package com.rakhman.customerinquirytracker.inquiry

import org.springframework.stereotype.Service

//Позволит Spring обнаруживать класс, параметр
//параметр конструктора указывает, какой репозиторий передать при создании сервиса.
@Service
class InquiryService(
    private val inquiryRepository: InquiryRepository
) {
    /**
     * Spring Data находит inquiryRepository
     * он создает объект, который реализует интерфейс
     * и регистрирует его в контексте Spring
     * такой управляемый объект называется бином
     */
    fun create(title:String, customerName: String, customerEmail: String): Inquiry {
        val inquiry = Inquiry(title, customerName, customerEmail)
        return inquiryRepository.save(inquiry)
    }
}