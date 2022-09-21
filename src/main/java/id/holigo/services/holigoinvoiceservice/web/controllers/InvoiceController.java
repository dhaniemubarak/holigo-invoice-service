package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineService;
import id.holigo.services.holigoinvoiceservice.services.PdfHotelEreceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import id.holigo.services.holigoinvoiceservice.services.PdfService;
import id.holigo.services.holigoinvoiceservice.services.transaction.TransactionService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InvoiceController {

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private PdfAirlineService pdfAirlineService;

    @Autowired
    private PdfHotelEreceipt pdfHotelEreceipt;

    @GetMapping("/web/v1/invoice/{id}/download")
    public void downloadReceipt(HttpServletResponse response, @PathVariable("id") UUID id)
            throws IOException, DocumentException {
        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        if (transactionDto.getTransactionType().equals("AIR")){
            pdfAirlineService.airlineEreceipt(transactionDto);
            pdfAirlineService.airlineEticket(transactionDto);
        }if (transactionDto.getTransactionType().equals("HTL")){
            pdfHotelEreceipt.eReceiptHotel(transactionDto);
        }else {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "invoice-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfService.export(response, transactionDto);
        }

    }

    @GetMapping("/web/v1/invoice/{id}")
    public String getInvoice(@PathVariable("id") UUID id, Model model)
            throws DocumentException, IOException {

        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        model.addAttribute("transactionDto", transactionDto);
        model.addAttribute("media", "screen");
        return getInvoice(transactionDto);
    }

    private String getInvoice(TransactionDto transactionDto) {
        String invoice = null;
        switch (transactionDto.getTransactionType()) {
            default -> {
                invoice = "prepaid-postpaid-index";
            }
        }
        return invoice;
    }

}
