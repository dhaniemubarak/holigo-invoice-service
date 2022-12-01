package id.holigo.services.holigoinvoiceservice.services.pdfHolicash;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

public interface HolicashService {
    void invoiceHolicash(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService)throws MalformedURLException;
}
