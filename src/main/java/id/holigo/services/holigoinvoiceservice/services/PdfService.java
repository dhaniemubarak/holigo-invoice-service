package id.holigo.services.holigoinvoiceservice.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

public interface PdfService {
    void export(HttpServletResponse response) throws DocumentException, IOException;
}
