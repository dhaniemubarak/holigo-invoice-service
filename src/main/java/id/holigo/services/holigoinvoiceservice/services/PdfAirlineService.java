package id.holigo.services.holigoinvoiceservice.services;


import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import java.io.IOException;

public interface PdfAirlineService {
    void airline(TransactionDto transactionDto)throws IOException;
}
