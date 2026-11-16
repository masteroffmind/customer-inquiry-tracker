package com.rakhman.customerinquirytracker.inquiry

import org.springframework.data.jpa.repository.JpaRepository

interface InquiryRepository : JpaRepository<Inquiry, Long> {
}