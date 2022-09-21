package id.holigo.services.holigoinvoiceservice.services;


import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import java.io.IOException;

public interface PdfAirlineService {
    void airlineEreceipt(TransactionDto transactionDto)throws IOException;

    void airlineEticket(TransactionDto transactionDto) throws IOException;
}
