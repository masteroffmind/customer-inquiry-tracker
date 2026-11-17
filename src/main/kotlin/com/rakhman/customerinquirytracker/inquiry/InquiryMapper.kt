package com.rakhman.customerinquirytracker.inquiry

import com.rakhman.customerinquirytracker.inquiry.dto.InquiryResponse

/**
 * Маппер берёт данные из сохранённого Inquiry и собирает из них InquiryResponse,
 * который контроллер потом отдаст клиенту как JSON.
 */
fun Inquiry.toResponse(): InquiryResponse {
    return InquiryResponse(
        id = checkNotNull(this.id),
        title = this.title,
        description = this.description,
        status = this.status,
        priority = this.priority,
        customerName = this.customerName,
        customerEmail = this.customerEmail,
        createdAt = checkNotNull(this.createdAt),
        updatedAt = checkNotNull(this.updatedAt)
    )
}