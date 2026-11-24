package com.rakhman.customerinquirytracker.inquiry

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.Optional
import kotlin.test.assertFailsWith

class InquiryServiceTest {

    @Test
    fun `NEW inquiry can move to IN_PROGRESS`(){
        /**
         * Mockito.mock(InquiryRepository::class.java) создает поддельную mock реализацию интерфейса
         * или класса InquiryRepository для юнит-теста
         */
        val repository = Mockito.mock(InquiryRepository::class.java)
        /**
         * Создает настоящий сервис и передает ему подставной репо через конструктор
         */
        val service = InquiryService(repository)
        val inquiry = Inquiry(
            title = "Не приходит письмо",
            customerName = "Анна",
            customerEmail = "anna@example.com"
        )

        Mockito.`when`(repository.findById(1L))
            .thenReturn(Optional.of(inquiry))

        val result = service.changeStatus(1L, InquiryStatus.IN_PROGRESS)
        assertEquals(InquiryStatus.IN_PROGRESS, result.status)
    }

    @Test
    fun `NEW inquiry cannot move to CLOSED`(){
        val repository = Mockito.mock(InquiryRepository::class.java)
        val service = InquiryService(repository)
        val inquiry = Inquiry(
            title = "Не приходит письмо",
            customerName = "Анна",
            customerEmail = "anna@example.com"
        )
        Mockito.`when`(repository.findById(1L))
            .thenReturn(Optional.of(inquiry))

        assertFailsWith<InvalidStatusTransitionException>{
            service.changeStatus(1L, InquiryStatus.CLOSED)
        }
        assertEquals(InquiryStatus.NEW, inquiry.status)
    }

    @Test
    fun `RESOLVED inquiry can move to IN_PROGRESS`(){
        val repository = Mockito.mock(InquiryRepository::class.java)
        val service = InquiryService(repository)
        val inquiry = Inquiry(
            title = "Не приходит письмо",
            customerName = "Анна",
            customerEmail = "anna@example.com"
        )
        inquiry.status = InquiryStatus.RESOLVED
        Mockito.`when`(repository.findById(1L))
            .thenReturn(Optional.of(inquiry))

        val result = service.changeStatus(1L, InquiryStatus.IN_PROGRESS)
        assertEquals(InquiryStatus.IN_PROGRESS, result.status)
    }
}