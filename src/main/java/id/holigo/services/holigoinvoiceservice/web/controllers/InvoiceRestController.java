package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineService;
import id.holigo.services.holigoinvoiceservice.services.pdfHotel.PdfHotelService;
import id.holigo.services.holigoinvoiceservice.services.pdfPulsa.PdfPulsaService;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import id.holigo.services.holigoinvoiceservice.services.PdfService;
import id.holigo.services.holigoinvoiceservice.services.transaction.TransactionService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceRestController {

    private PdfPulsaService pdfPulsaService;

    @Autowired
    public void setPdfPulsaService(PdfPulsaService pdfPulsaService) {
        this.pdfPulsaService = pdfPulsaService;
    }

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

    ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //GENERAL
    @GetMapping("/web/v1/invoice/{id}/download")
    public void downloadReceipt(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException, DocumentException {

//        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

        String json = "{\"id\":\"0a71b4af-fae7-4d75-b9f3-f54fa9e51063\",\"invoiceNumber\":\"46/20221107/1986\",\"createdAt\":\"2022-11-07T00:43:41.007+00:00\",\"expiredAt\":\"2022-11-07T02:43:40.925+00:00\",\"discountAmount\":0,\"fareAmount\":25024,\"ntaAmount\":24855,\"nraAmount\":169,\"hpAmount\":4,\"adminAmount\":0,\"indexProduct\":\"Pulsa|XL|25.000|083816216886\",\"transactionId\":\"1986\",\"transactionType\":\"PUL\",\"paymentId\":\"30c3a6d1-737e-428f-8a5b-bf3c370f9538\",\"payment\":{\"id\":\"30c3a6d1-737e-428f-8a5b-bf3c370f9538\",\"fareAmount\":25024,\"serviceFeeAmount\":74,\"discountAmount\":0,\"totalAmount\":25024,\"paymentServiceAmount\":25098,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":25098,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"25057d36-d81a-40db-b5ac-058a149e111d\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/30c3a6d1-737e-428f-8a5b-bf3c370f9538/bankTransfer/25057d36-d81a-40db-b5ac-058a149e111d\",\"createdAt\":\"2022-11-07T00:44:23.599+00:00\",\"updatedAt\":\"2022-11-07T00:46:27.189+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":46,\"productId\":50,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"PUL\",\"serviceName\":\"Pulsa\",\"productCode\":\"XLREG\",\"productName\":\"XL\",\"nominalCode\":\"X25\",\"nominalName\":\"25.000\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/operator/Xl_86YOzimM9.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/pulsa_veiU5oNbBkg.png?tr=h-192,w-192\",\"customerNumber\":\"083816216886\",\"description\":\"25000\",\"serialNumber\":\"20181107600259\",\"billAmount\":25024,\"striketroughAdmin\":0},\"serverTime\":\"2022-11-08T07:12:21.179+00:00\"}";

        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);

        StylePdfService stylePdfService = new StylePdfService();

        switch (transactionDto.getTransactionType()) {
            case "AIR" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-airline-" + transactionDto.getInvoiceNumber() + ".pdf");
                pdfAirlineService.airlineEreceipt(transactionDto, response);
            }
            case "HTL" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-hotel-" + transactionDto.getInvoiceNumber() + ".pdf");
                pdfHotelService.eReceiptHotel(transactionDto, response);
            }
            case "PUL", "PD","PR" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-pulsa-" + transactionDto.getInvoiceNumber() + ".pdf");
                pdfPulsaService.invoicePulsa(transactionDto, response, stylePdfService);
            }
            case "DWAL" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-pulsa-" + transactionDto.getInvoiceNumber() + ".pdf");

            }
            default -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-" + transactionDto.getInvoiceNumber() + ".pdf");
                pdfService.export(response, transactionDto);
            }
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
