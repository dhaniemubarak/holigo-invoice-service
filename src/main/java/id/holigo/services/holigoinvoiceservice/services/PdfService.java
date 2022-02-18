package id.holigo.services.holigoinvoiceservice.services;

import java.io.File;
import java.io.IOException;

import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

public interface PdfService {

    File generatePdf(TransactionDto transactionDto, String template) throws IOException, DocumentException;
}
