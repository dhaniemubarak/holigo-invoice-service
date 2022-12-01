package id.holigo.services.holigoinvoiceservice.services.pdfKereta;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

public interface EticketKeretaService {
    void eticketKereta(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService) throws MalformedURLException;
}
