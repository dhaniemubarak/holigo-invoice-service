package id.holigo.services.holigoinvoiceservice.services.pdfHotel;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfHotelService {
    void eReceiptHotel(TransactionDto transactionDto, HttpServletResponse response) throws IOException;
    void eVoucherHotel(TransactionDto transactionDto, HttpServletResponse response) throws IOException;
}