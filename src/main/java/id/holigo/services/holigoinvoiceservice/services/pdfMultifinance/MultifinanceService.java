package id.holigo.services.holigoinvoiceservice.services.pdfMultifinance;

import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MultifinanceService {
    void multifinanceInvoice(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService)throws IOException;
}
