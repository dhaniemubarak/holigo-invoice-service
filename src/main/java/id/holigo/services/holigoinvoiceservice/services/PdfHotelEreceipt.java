package id.holigo.services.holigoinvoiceservice.services;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import java.io.IOException;

public interface PdfHotelEreceipt {
    void eReceiptHotel(TransactionDto transactionDto)throws IOException;
}
