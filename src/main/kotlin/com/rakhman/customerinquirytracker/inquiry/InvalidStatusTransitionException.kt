package com.rakhman.customerinquirytracker.inquiry

class InvalidStatusTransitionException(
    from: InquiryStatus,
    to: InquiryStatus
) : RuntimeException("Cannot change inquiry status from $from to $to")