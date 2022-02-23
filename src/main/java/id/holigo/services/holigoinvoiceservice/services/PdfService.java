package id.holigo.services.holigoinvoiceservice.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

public interface PdfService {
    void export(HttpServletResponse response, TransactionDto transactionDto) throws DocumentException, IOException;
}
