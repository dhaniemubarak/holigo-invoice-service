package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineService;
import id.holigo.services.holigoinvoiceservice.services.pdfHotel.PdfHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import id.holigo.services.holigoinvoiceservice.services.PdfService;
import id.holigo.services.holigoinvoiceservice.services.transaction.TransactionService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceRestController {

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    private PdfService pdfService;

    @Autowired
    public void setPdfService(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private PdfAirlineService pdfAirlineService;

    @Autowired
    public void setPdfAirlineService(PdfAirlineService pdfAirlineService) {
        this.pdfAirlineService = pdfAirlineService;
    }

    private PdfHotelService pdfHotelService;

    @Autowired
    public void setPdfHotelService(PdfHotelService pdfHotelService) {
        this.pdfHotelService = pdfHotelService;
    }

    //GENERAL
    @GetMapping("/web/v1/invoice/{id}/download")
    public void downloadReceipt(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException, DocumentException {

        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

        if (transactionDto.getTransactionType().equals("AIR")){
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "invoice-airline-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfAirlineService.airlineEreceipt(transactionDto,response);
        }else if (transactionDto.getTransactionType().equals("HTL")){
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "invoice-hotel-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfHotelService.eReceiptHotel(transactionDto,response);
        }else {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "invoice-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfService.export(response, transactionDto);
        }
    }
    @GetMapping("/web/v1/invoice/{id}/eticket")
    public void downloadEvoucherEticket(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException {

        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        if (transactionDto.getTransactionType().equals("AIR")){
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "eticket-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfAirlineService.airlineEticket(transactionDto,response);
        }else {
            System.err.print("transaction type is not AIR");
        }
    }
    //HOTEL
    @GetMapping("/web/v1/invoice/{id}/evoucher")
    public void downloadEvoucher(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException {

        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

        if (transactionDto.getTransactionType().equals("HTL")){
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "eVoucher-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfHotelService.eVoucherHotel(transactionDto,response);
        }else {
            System.err.print("transaction type is not HTL");
        }

    }




}
