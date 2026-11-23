package com.rakhman.customerinquirytracker.inquiry

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface InquiryRepository : JpaRepository<Inquiry, Long> {
    /**
     * Передали null первая часть истинна, поэтому подойдут обращения с любым статусом
     * Передали NEW — подойдут только обращения со статусом NEW.
     */
    @Query(
        """
            SELECT i FROM Inquiry i
            WHERE (:status IS NULL OR i.status = :status)
            AND (:priority IS NULL OR i.priority = :priority)
        """
        /**
         * задает запрос, который Spring Data должен выполнить для этого метода
         */
    )
    fun findByFilters(
        @Param("status") status: InquiryStatus?,
        @Param("priority") priority: InquiryPriority?,
        pageable: Pageable
        /**
         * Pageable - какие данные запросить, номер страницы, размер и сортировку
         */
    ):Page<Inquiry>
    /**
     * Результат - обращения выбранной страницы и сведения об общем количестве
     */
}