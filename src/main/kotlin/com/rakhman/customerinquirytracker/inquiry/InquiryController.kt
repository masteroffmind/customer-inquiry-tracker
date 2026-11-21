package com.rakhman.customerinquirytracker.inquiry

import com.rakhman.customerinquirytracker.inquiry.dto.ChangeStatusRequest
import com.rakhman.customerinquirytracker.inquiry.dto.CreateInquiryRequest
import com.rakhman.customerinquirytracker.inquiry.dto.InquiryResponse
import com.rakhman.customerinquirytracker.inquiry.dto.UpdateInquiryRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

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
    fun create(@Valid @RequestBody request: CreateInquiryRequest): ResponseEntity<InquiryResponse> {
        /**
         * аннотация Valid включает проверку ограничений DTO, обращаясь к полям, которые покрыты аннотациями
         */
        val savedInquiry = inquiryService.create(title = request.title, customerName = request.customerName, customerEmail = request.customerEmail)
        val response = savedInquiry.toResponse()//Собирает DTO
        val location = URI.create("/api/v1/inquiries/${response.id}")//Построение адреса конкретного обращения
        return ResponseEntity.created(location).body(response)//Собирает полноценный HTTP-ответ со статусом Created, заголовком и DTO в теле
    }
    /**
     * Сервис вовзращает найденный inquiry, а контроллер преобразует его в dto и отпрвляет с 200
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<InquiryResponse> {
       val response = inquiryService.getById(id)
        return ResponseEntity.ok(response.toResponse())
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UpdateInquiryRequest): ResponseEntity<InquiryResponse> {
        val response = inquiryService.update(id, request)
        return ResponseEntity.ok(response.toResponse())
    }
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void>{
        /**
         * Void даст нам ответ без DTO,coContent даст 204 статус, который
         * означает, что сервер обработал успешно обработал запрос, но не вернул ответ
         */
        inquiryService.delete(id)
        return ResponseEntity.noContent().build()
    }
    @PatchMapping("/{id}/status")
            /**
             * Метод HTTP используется для частичного изменения ресурса
             */
    fun changeStatus(@PathVariable id: Long, @RequestBody request: ChangeStatusRequest): ResponseEntity<InquiryResponse> {
        val response = inquiryService.changeStatus(id, request.status)
        return ResponseEntity.ok(response.toResponse())
    }

}
