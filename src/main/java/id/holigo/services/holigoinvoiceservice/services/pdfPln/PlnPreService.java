package id.holigo.services.holigoinvoiceservice.services.pdfPln;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

public interface PlnPreService {
    void invoicePLNPre(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService)throws MalformedURLException;

}
