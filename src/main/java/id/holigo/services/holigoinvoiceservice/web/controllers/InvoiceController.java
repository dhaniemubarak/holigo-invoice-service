package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/web/v1/invoice/{id}/download")
    public void downloadReceipt(HttpServletResponse httpServletResponse, @PathVariable("id") UUID id)
            throws IOException {
        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        // String invoice = getInvoice(transactionDto);
        String invoice = "index";
        try {
            Path file = Paths.get(pdfService.generatePdf(transactionDto, invoice).getAbsolutePath());
            if (Files.exists(file)) {
                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
                Files.copy(file, httpServletResponse.getOutputStream());
                httpServletResponse.getOutputStream().flush();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
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
        String invoice;
        switch (transactionDto.getTransactionType()) {
            default:
                invoice = "prepaid-postpaid-index";
                break;
        }
        return invoice;
    }
}
