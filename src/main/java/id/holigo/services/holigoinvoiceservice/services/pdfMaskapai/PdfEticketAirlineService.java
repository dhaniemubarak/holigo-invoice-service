package id.holigo.services.holigoinvoiceservice.services.pdfMaskapai;


import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfEticketAirlineService {

    void airlineEticket(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService) throws IOException;
}
