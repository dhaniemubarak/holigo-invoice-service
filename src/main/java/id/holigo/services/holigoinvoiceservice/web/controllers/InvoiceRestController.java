package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineService;
import id.holigo.services.holigoinvoiceservice.services.pdfDwall.DigitalWalletService;
import id.holigo.services.holigoinvoiceservice.services.pdfEwall.EwallService;
import id.holigo.services.holigoinvoiceservice.services.pdfHotel.PdfHotelService;
import id.holigo.services.holigoinvoiceservice.services.pdfPln.PlnPostService;
import id.holigo.services.holigoinvoiceservice.services.pdfPln.PlnPreService;
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

    private DigitalWalletService digitalWalletService;

    @Autowired
    public void setDigitalWalletService(DigitalWalletService digitalWalletService) {
        this.digitalWalletService = digitalWalletService;
    }

    private EwallService ewallService;

    @Autowired
    public void setEwallService(EwallService ewallService) {
        this.ewallService = ewallService;
    }

    private PlnPreService plnPreService;

    @Autowired
    public void setPlnPreService(PlnPreService plnPreService) {
        this.plnPreService = plnPreService;
    }

    private PlnPostService plnPostService;

    @Autowired
    public void setPlnPostService(PlnPostService plnPostService) {
        this.plnPostService = plnPostService;
    }

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


        //pulsa
//        String json = "{\"id\":\"0a71b4af-fae7-4d75-b9f3-f54fa9e51063\",\"invoiceNumber\":\"46/20221107/1986\",\"createdAt\":\"2022-11-07T00:43:41.007+00:00\",\"expiredAt\":\"2022-11-07T02:43:40.925+00:00\",\"discountAmount\":0,\"fareAmount\":25024,\"ntaAmount\":24855,\"nraAmount\":169,\"hpAmount\":4,\"adminAmount\":0,\"indexProduct\":\"Pulsa|XL|25.000|083816216886\",\"transactionId\":\"1986\",\"transactionType\":\"PUL\",\"paymentId\":\"30c3a6d1-737e-428f-8a5b-bf3c370f9538\",\"payment\":{\"id\":\"30c3a6d1-737e-428f-8a5b-bf3c370f9538\",\"fareAmount\":25024,\"serviceFeeAmount\":74,\"discountAmount\":0,\"totalAmount\":25024,\"paymentServiceAmount\":25098,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":25098,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"25057d36-d81a-40db-b5ac-058a149e111d\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/30c3a6d1-737e-428f-8a5b-bf3c370f9538/bankTransfer/25057d36-d81a-40db-b5ac-058a149e111d\",\"createdAt\":\"2022-11-07T00:44:23.599+00:00\",\"updatedAt\":\"2022-11-07T00:46:27.189+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":46,\"productId\":50,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"PUL\",\"serviceName\":\"Pulsa\",\"productCode\":\"XLREG\",\"productName\":\"XL\",\"nominalCode\":\"X25\",\"nominalName\":\"25.000\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/operator/Xl_86YOzimM9.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/pulsa_veiU5oNbBkg.png?tr=h-192,w-192\",\"customerNumber\":\"083816216886\",\"description\":\"25000\",\"serialNumber\":\"20181107600259\",\"billAmount\":25024,\"striketroughAdmin\":0},\"serverTime\":\"2022-11-08T07:12:21.179+00:00\"}";
        //PLN post
//        String json = "{\"id\":\"1ed9ffdb-4aa3-4d5f-b20f-14fd4c9006ae\",\"invoiceNumber\":\"1099/20221107/56\",\"createdAt\":\"2022-11-07T08:20:09.868+00:00\",\"expiredAt\":\"2022-11-07T10:20:09.826+00:00\",\"discountAmount\":0,\"fareAmount\":423945,\"ntaAmount\":421971,\"nraAmount\":1975,\"hpAmount\":49,\"adminAmount\":1999,\"indexProduct\":\"Postpaid|Tagihan Listrik|Tagihan Listrik|534440487884\",\"transactionId\":\"56\",\"transactionType\":\"PLNPOST\",\"paymentId\":\"5f2c0508-9586-4111-b09e-5bd9eb483939\",\"payment\":{\"id\":\"5f2c0508-9586-4111-b09e-5bd9eb483939\",\"fareAmount\":423945,\"serviceFeeAmount\":2,\"discountAmount\":0,\"totalAmount\":423944,\"paymentServiceAmount\":423946,\"depositAmount\":0,\"isSplitBill\":true,\"pointAmount\":1,\"remainingAmount\":423946,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"77ddf863-826f-4870-84f8-131c33d50273\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/5f2c0508-9586-4111-b09e-5bd9eb483939/bankTransfer/77ddf863-826f-4870-84f8-131c33d50273\",\"createdAt\":\"2022-11-07T08:21:09.890+00:00\",\"updatedAt\":\"2022-11-07T08:24:10.236+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1099,\"productId\":1110,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"PLNPOST\",\"serviceName\":\"Tagihan Listrik\",\"productCode\":\"PLNPOSTB\",\"productName\":\"Tagihan Listrik\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/token-PLN_H5p6H0xT-87.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/token-PLN_H5p6H0xT-87.png?tr=h-192,w-192\",\"customerName\":\"BTN GRIYA ASRI\",\"customerNumber\":\"534440487884\",\"billQty\":1,\"period\":\"NOV22\",\"standMeter\":\"00026666-00026969\",\"tariffAdjustment\":\"R1M /000000900VA\",\"reference\":\"0UAK21110003DC46DB0D\",\"adminAmount\":1999,\"fareAmount\":423945,\"billAmount\":421946,\"discountAmount\":0,\"striketroughAdmin\":2000},\"serverTime\":\"2022-11-08T07:16:23.902+00:00\"}{\"id\":\"1ed9ffdb-4aa3-4d5f-b20f-14fd4c9006ae\",\"invoiceNumber\":\"1099/20221107/56\",\"createdAt\":\"2022-11-07T08:20:09.868+00:00\",\"expiredAt\":\"2022-11-07T10:20:09.826+00:00\",\"discountAmount\":0,\"fareAmount\":423945,\"ntaAmount\":421971,\"nraAmount\":1975,\"hpAmount\":49,\"adminAmount\":1999,\"indexProduct\":\"Postpaid|Tagihan Listrik|Tagihan Listrik|534440487884\",\"transactionId\":\"56\",\"transactionType\":\"PLNPOST\",\"paymentId\":\"5f2c0508-9586-4111-b09e-5bd9eb483939\",\"payment\":{\"id\":\"5f2c0508-9586-4111-b09e-5bd9eb483939\",\"fareAmount\":423945,\"serviceFeeAmount\":2,\"discountAmount\":0,\"totalAmount\":423944,\"paymentServiceAmount\":423946,\"depositAmount\":0,\"isSplitBill\":true,\"pointAmount\":1,\"remainingAmount\":423946,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"77ddf863-826f-4870-84f8-131c33d50273\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/5f2c0508-9586-4111-b09e-5bd9eb483939/bankTransfer/77ddf863-826f-4870-84f8-131c33d50273\",\"createdAt\":\"2022-11-07T08:21:09.890+00:00\",\"updatedAt\":\"2022-11-07T08:24:10.236+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1099,\"productId\":1110,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"PLNPOST\",\"serviceName\":\"Tagihan Listrik\",\"productCode\":\"PLNPOSTB\",\"productName\":\"Tagihan Listrik\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/token-PLN_H5p6H0xT-87.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/token-PLN_H5p6H0xT-87.png?tr=h-192,w-192\",\"customerName\":\"BTN GRIYA ASRI\",\"customerNumber\":\"534440487884\",\"billQty\":1,\"period\":\"NOV22\",\"standMeter\":\"00026666-00026969\",\"tariffAdjustment\":\"R1M /000000900VA\",\"reference\":\"0UAK21110003DC46DB0D\",\"adminAmount\":1999,\"fareAmount\":423945,\"billAmount\":421946,\"discountAmount\":0,\"striketroughAdmin\":2000},\"serverTime\":\"2022-11-08T07:16:23.902+00:00\"}";
        //PLN pre
//        String json = "{\"id\":\"9aba23b6-7e62-45aa-a1c3-9b7bace900ca\",\"invoiceNumber\":\"1100/20221107/331\",\"createdAt\":\"2022-11-07T07:09:01.970+00:00\",\"expiredAt\":\"2022-11-07T09:09:01.940+00:00\",\"discountAmount\":2500,\"fareAmount\":1002500,\"ntaAmount\":1000000,\"nraAmount\":2500,\"hpAmount\":78,\"adminAmount\":2500,\"indexProduct\":\"Token Listrik|Token Listrik|Rp 1.000.000|546200214751\",\"transactionId\":\"331\",\"transactionType\":\"PLNPRE\",\"paymentId\":\"4732ce2b-3b99-4417-ba4d-24ed3cdd89df\",\"payment\":{\"id\":\"4732ce2b-3b99-4417-ba4d-24ed3cdd89df\",\"fareAmount\":1002500,\"serviceFeeAmount\":0,\"discountAmount\":2500,\"totalAmount\":1000000,\"paymentServiceAmount\":1000000,\"depositAmount\":1000000,\"isSplitBill\":true,\"pointAmount\":0,\"remainingAmount\":1000000,\"status\":\"PAID\",\"paymentService\":{\"id\":\"DEPOSIT\",\"name\":\"Holi Cash\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Icon/Holicash_jKmuMF8Xc.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":null,\"closeTime\":\"23:50:00\",\"minimumAmount\":1,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.DEPOSIT\"},\"detailId\":\"4ddc0eb4-aa24-445f-acc6-be650ae760b4\",\"detailType\":\"deposit\",\"detailRoute\":\"/api/v1/payments/4732ce2b-3b99-4417-ba4d-24ed3cdd89df/deposit/4ddc0eb4-aa24-445f-acc6-be650ae760b4\",\"createdAt\":\"2022-11-07T07:09:21.093+00:00\",\"updatedAt\":\"2022-11-07T07:09:21.119+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1100,\"productId\":1106,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"PLNPRE\",\"serviceName\":\"Token Listrik\",\"productCode\":\"PLN PREPAID\",\"productName\":\"Token Listrik\",\"nominalCode\":\"PLNPRE1000\",\"nominalName\":\"Token Rp 1.000.000\",\"customerName\":\"NURBANI ERMIN\",\"customerNumber\":\"546200214751\",\"tariffAdjustment\":\"R3/33000\",\"kwh\":\"503,0\",\"token\":\"3504-6092-3343-3601-4717\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/Token-listrik_jOG15kiP2.png?tr=h-192,w-192\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/Token-listrik_jOG15kiP2.png?tr=h-192,w-192\",\"fareAmount\":1002500,\"billAmount\":1000000,\"adminAmount\":2500,\"striketroughAdmin\":2500},\"serverTime\":\"2022-11-08T07:17:48.363+00:00\"}";
        //EMoney
        String json = "{\"id\":\"33fb23d6-1da2-40f4-a14f-ef2b19592f06\",\"invoiceNumber\":\"578/20221107/556\",\"createdAt\":\"2022-11-07T06:46:37.188+00:00\",\"expiredAt\":\"2022-11-07T08:46:37.156+00:00\",\"discountAmount\":500,\"fareAmount\":26155,\"ntaAmount\":25655,\"nraAmount\":500,\"hpAmount\":12,\"adminAmount\":500,\"indexProduct\":\"Uang Elektronik|Mandiri E-Money|25.000|6032982807212897\",\"transactionId\":\"556\"," + "\"transactionType\":\"EWAL\",\"paymentId\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"payment\":{\"id\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"fareAmount\":26155,\"serviceFeeAmount\":2750,\"discountAmount\":500,\"totalAmount\":25655,\"paymentServiceAmount\":28405,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":28405,\"status\":\"PAID\",\"paymentService\":{\"id\":\"VA_BCA\",\"name\":\"BCA Virtual Account\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":2750,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.VA_BCA\"},\"detailId\":\"363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"detailType\":\"virtualAccount\",\"detailRoute\":\"/api/v1/payments/d4cf5865-1bf8-40c4-ac9b-bfa75e342154/virtualAccount/363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"createdAt\":\"2022-11-07T06:49:02.153+00:00\",\"updatedAt\":\"2022-11-07T06:50:03.665+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":578,\"productId\":581,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"EWAL\",\"serviceName\":\"Uang Elektronik\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/e-money_duwsbqv3H.png?tr=h-192,w-192\",\"productCode\":\"E-TOLL MANDIRI\",\"productName\":\"Mandiri E-Money\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/kartu_elektronik/e-money_hM7o2qNzF.png?tr=h-192,w-192\",\"nominalCode\":\"TOLM25\",\"nominalName\":\"Rp 25.000\",\"customerNumber\":\"6032982807212897\",\"serialNumber\":\"166780380555519431\",\"fareAmount\":26155,\"billAmount\":25655,\"adminAmount\":500,\"striketroughAdmin\":1500},\"serverTime\":\"2022-11-08T07:19:02.638+00:00\"}";
        //DWALL
//        String json = "{\"id\":\"33fb23d6-1da2-40f4-a14f-ef2b19592f06\",\"invoiceNumber\":\"578/20221107/556\",\"createdAt\":\"2022-11-07T06:46:37.188+00:00\",\"expiredAt\":\"2022-11-07T08:46:37.156+00:00\",\"discountAmount\":500,\"fareAmount\":26155,\"ntaAmount\":25655,\"nraAmount\":500,\"hpAmount\":12,\"adminAmount\":500,\"indexProduct\":\"Uang Elektronik|Mandiri E-Money|25.000|6032982807212897\",\"transactionId\":\"556\"," +
//                "\"transactionType\":\"DWAL\",\"paymentId\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"payment\":{\"id\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"fareAmount\":26155,\"serviceFeeAmount\":2750,\"discountAmount\":500,\"totalAmount\":25655,\"paymentServiceAmount\":28405,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":28405,\"status\":\"PAID\",\"paymentService\":{\"id\":\"VA_BCA\",\"name\":\"BCA Virtual Account\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":2750,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.VA_BCA\"},\"detailId\":\"363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"detailType\":\"virtualAccount\",\"detailRoute\":\"/api/v1/payments/d4cf5865-1bf8-40c4-ac9b-bfa75e342154/virtualAccount/363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"createdAt\":\"2022-11-07T06:49:02.153+00:00\",\"updatedAt\":\"2022-11-07T06:50:03.665+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":578,\"productId\":581,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"EWAL\",\"serviceName\":\"Uang Elektronik\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/e-money_duwsbqv3H.png?tr=h-192,w-192\",\"productCode\":\"E-TOLL MANDIRI\",\"productName\":\"Mandiri E-Money\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/kartu_elektronik/e-money_hM7o2qNzF.png?tr=h-192,w-192\",\"nominalCode\":\"TOLM25\",\"nominalName\":\"Rp 25.000\",\"customerNumber\":\"6032982807212897\",\"serialNumber\":\"166780380555519431\",\"fareAmount\":26155,\"billAmount\":25655,\"adminAmount\":500,\"striketroughAdmin\":1500},\"serverTime\":\"2022-11-08T07:19:02.638+00:00\"}";

        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);
//        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

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
                String type = transactionDto.getTransactionType().toString();
                pdfPulsaService.invoicePulsa(transactionDto, response, stylePdfService,type);
            }
            case "PLNPOST" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-PLN-post-" + transactionDto.getInvoiceNumber() + ".pdf");
                plnPostService.invoicePLNPost(transactionDto, response, stylePdfService);
            }
            case "PLNPRE" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-PLN-pre-" + transactionDto.getInvoiceNumber() + ".pdf");
                plnPreService.invoicePLNPre(transactionDto, response, stylePdfService);
            }
            case "EWAL" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-E-wall-" + transactionDto.getInvoiceNumber() + ".pdf");
                ewallService.invoiceEwall(transactionDto, response, stylePdfService);
            }
            case "DWAL" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-E-wall-" + transactionDto.getInvoiceNumber() + ".pdf");
                digitalWalletService.DigitalWallet(transactionDto, response, stylePdfService);
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
