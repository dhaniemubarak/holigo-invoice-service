package id.holigo.services.holigoinvoiceservice.services.pdfPulsa;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

public interface PdfPulsaService {
    void invoicePulsa(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService, String type) throws MalformedURLException;

}
