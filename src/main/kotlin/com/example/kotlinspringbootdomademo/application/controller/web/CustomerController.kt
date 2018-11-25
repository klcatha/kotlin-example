package com.example.kotlinspringbootdomademo.application.controller.web

import com.example.kotlinspringbootdomademo.application.input.CustomerInput
import com.example.kotlinspringbootdomademo.domain.repository.CustomerRepository
import com.example.kotlinspringbootdomademo.application.service.CustomerApplicationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/customers")
class CustomerController(
        private val customerRepository: CustomerRepository,
        private val customerApplicationService: CustomerApplicationService
) {

    @GetMapping("new")
    fun new(input: CustomerInput): String {
        return "customers/new"
    }

    @PostMapping("")
    fun create(
            @Validated customerInput: CustomerInput,
            bindingResult: BindingResult
    ): String {
        if(bindingResult.hasErrors()) {
            return "customers/new"
        }

        val id = customerApplicationService.create(customerInput)

        return "redirect:/customers/${id}"
    }

    @GetMapping("")
    fun index(model: Model): String {
        val customers = customerApplicationService.findAll()
        model.addAttribute("customers", customers)
        return "customers/index"
    }

    @GetMapping("{id}")
    fun show(
            @PathVariable id: Int,
            model: Model
    ): String {
        val customer = customerApplicationService.findById(id)
        model.addAttribute("customer", customer)
        return "customers/show"
    }

    @GetMapping("{id}/edit")
    fun edit(
            @PathVariable id: Int,
            customerInput: CustomerInput
    ): String {
        val customer = customerApplicationService.findById(id)

        customerInput.name = customer.name
        customerInput.email = customer.email
        customerInput.phone_number = customer.phone_number
        customerInput.github_name = customer.github_name
        customerInput.twitter_name = customer.twitter_name
        customerInput.main_org = customer.main_org
        customerInput.sub_org = customer.sub_org
        return "customers/edit"
    }

    @PatchMapping("{id}")
    fun update(
            @PathVariable id: Int,
            @Validated customerInput: CustomerInput,
            bindingResult: BindingResult
    ): String {
        if(bindingResult.hasErrors()) {
            return "customers/edit"
        }

        customerApplicationService.update(id, customerInput)

        return "redirect:/customers"
    }
    @DeleteMapping("{id}")
    fun delete(
            @PathVariable id: Int
    ): String {
        customerApplicationService.delete(id)
        return "redirect:/customers"
    }
}
