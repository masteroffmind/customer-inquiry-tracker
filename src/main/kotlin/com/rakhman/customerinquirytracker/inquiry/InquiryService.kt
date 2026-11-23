package com.rakhman.customerinquirytracker.inquiry


import com.rakhman.customerinquirytracker.inquiry.dto.UpdateInquiryRequest
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

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
    fun getById(id:Long) : Inquiry{
        return inquiryRepository.findById(id)
            .orElseThrow{InquiryNotFoundException(id)}
    }

    /**
     * Transactional гарантирует, что
     * набор операций с базой данных выполнится атомарно
     */
    @Transactional
    fun update(id:Long, dto: UpdateInquiryRequest): Inquiry {
        val existingInquiry= inquiryRepository.findById(id)
            .orElseThrow{InquiryNotFoundException(id)}
        existingInquiry.apply {
            title = dto.title
            customerName = dto.customerName
            customerEmail = dto.customerEmail
            description = dto.description
            priority = dto.priority
            /**
             *  выполняет блок кода в контексте объекта и возвращает сам этот объект
             *  apply чаще всего используется для инициализации новых сущностей или массового
             *  обновления полей существующих объектов. Это избавляет от необходимости многократно
             *  повторять имя переменной.
             */
        }
        return existingInquiry
    }
    @Transactional
    fun delete(id:Long) {
        val inquiry = getById(id)
        inquiryRepository.delete(inquiry)
    }

    @Transactional
    fun changeStatus(id:Long, newStatus: InquiryStatus): Inquiry {
        val inquiry = getById(id)

        val allowed = when(inquiry.status) {
            InquiryStatus.NEW -> newStatus == InquiryStatus.IN_PROGRESS
            InquiryStatus.IN_PROGRESS -> newStatus == InquiryStatus.RESOLVED
            InquiryStatus.RESOLVED -> newStatus == InquiryStatus.IN_PROGRESS || newStatus == InquiryStatus.CLOSED
            InquiryStatus.CLOSED ->  false
        }
        if(!allowed) {
            throw InvalidStatusTransitionException(inquiry.status, newStatus)
            /**
             * RuntimeException используют, когда операция не может продолжаться из-за ошибки
             * или неподходящего состояния.
             */
        }
        inquiry.status = newStatus
        return inquiry
    }

    @Transactional(readOnly = true)
    fun findByFilters(status: InquiryStatus?, priority: InquiryPriority?, pageable: Pageable): Page<Inquiry> {
        return inquiryRepository.findByFilters(status, priority, pageable)
    }

}