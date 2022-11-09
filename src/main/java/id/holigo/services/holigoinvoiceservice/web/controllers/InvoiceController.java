package id.holigo.services.holigoinvoiceservice.web.controllers;

import id.holigo.services.holigoinvoiceservice.services.transaction.TransactionService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
public class InvoiceController {

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(name = "/web/v1/invoice/{id}", method = RequestMethod.GET)
    public String getInvoice(@PathVariable("id") UUID id, Model model) {
        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        model.addAttribute("transactionDto", transactionDto);
        model.addAttribute("media", "screen");
        return getInvoice(transactionDto);
    }

    private String getInvoice(TransactionDto transactionDto) {
        String invoice;
        invoice = "prepaid-postpaid-index";
        return invoice;
    }
}
