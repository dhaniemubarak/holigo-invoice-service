package id.holigo.services.holigoinvoiceservice.services.pdfMaskapai;


import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfEreceiptAirlineService {
    void airlineEreceipt(TransactionDto transactionDto, HttpServletResponse response)throws IOException;
}
