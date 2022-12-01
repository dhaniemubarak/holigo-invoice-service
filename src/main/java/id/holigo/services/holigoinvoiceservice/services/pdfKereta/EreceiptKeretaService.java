package id.holigo.services.holigoinvoiceservice.services.pdfKereta;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;

public interface EreceiptKeretaService {
    void invoiceKereta(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService);
}
