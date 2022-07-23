package com.example.astrosampleorder

import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController {

    @GetMapping("/order200")
    fun getOrder200(): Order {
        return Order()
    }

    @GetMapping("/order500")
    fun getOrder500() {
        throw UnexpectedRollbackException("")
    }

    @PostMapping("/order500")
    fun postOrder500() {
        throw UnexpectedRollbackException("")
    }

}