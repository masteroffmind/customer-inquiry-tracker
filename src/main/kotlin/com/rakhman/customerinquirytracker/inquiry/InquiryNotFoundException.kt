package com.rakhman.customerinquirytracker.inquiry

class InquiryNotFoundException(id: Long) : RuntimeException("Inquiry with id $id was not found.")