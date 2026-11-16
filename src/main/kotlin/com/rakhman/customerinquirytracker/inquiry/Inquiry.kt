package com.rakhman.customerinquirytracker.inquiry

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "inquiries")
class Inquiry(
    @Column(name = "title", nullable = false, length = 200)
    var title: String,

    @Column(name = "customer_name", length = 100, nullable = false)
    var customerName: String,

    @Column(name = "customer_email", length = 254, nullable = false)
    var customerEmail: String

) {
    @Id //говорит JPA, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;

    @Column(name = "description", nullable = true, unique = false, length = 5000)
    var description: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100, nullable = false)
    var status: InquiryStatus = InquiryStatus.NEW

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 100, nullable = false)
    var priority : InquiryPriority = InquiryPriority.MEDIUM

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null
}