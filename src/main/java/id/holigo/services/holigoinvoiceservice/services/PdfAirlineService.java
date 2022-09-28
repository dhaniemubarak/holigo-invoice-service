package id.holigo.services.holigoinvoiceservice.services;


import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfAirlineService {
    void airlineEreceipt(TransactionDto transactionDto, HttpServletResponse response)throws IOException;

    void airlineEticket(TransactionDto transactionDto, HttpServletResponse response) throws IOException;
}
