package id.holigo.services.holigoinvoiceservice.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineService;
import id.holigo.services.holigoinvoiceservice.services.pdfCC.CCService;
import id.holigo.services.holigoinvoiceservice.services.pdfDwall.DigitalWalletService;
import id.holigo.services.holigoinvoiceservice.services.pdfEwall.EwallService;
import id.holigo.services.holigoinvoiceservice.services.pdfGas.GasService;
import id.holigo.services.holigoinvoiceservice.services.pdfHolicash.HolicashService;
import id.holigo.services.holigoinvoiceservice.services.pdfHotel.PdfHotelService;
import id.holigo.services.holigoinvoiceservice.services.pdfInsurance.InsuranceService;
import id.holigo.services.holigoinvoiceservice.services.pdfKereta.EreceiptKeretaService;
import id.holigo.services.holigoinvoiceservice.services.pdfKereta.EticketKeretaService;
import id.holigo.services.holigoinvoiceservice.services.pdfMultifinance.MultifinanceService;
import id.holigo.services.holigoinvoiceservice.services.pdfNeTV.NeTVService;
import id.holigo.services.holigoinvoiceservice.services.pdfPDAM.PdamServce;
import id.holigo.services.holigoinvoiceservice.services.pdfPascabayar.PascabayarService;
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

    private HolicashService holicashService;

    @Autowired
    public void setHolicashService(HolicashService holicashService) {
        this.holicashService = holicashService;
    }

    private NeTVService neTVService;

    @Autowired
    public void setNeTVService(NeTVService neTVService) {
        this.neTVService = neTVService;
    }

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

    private PdamServce pdamServce;

    @Autowired
    public void setPdamServce(PdamServce pdamServce) {
        this.pdamServce = pdamServce;
    }

    private InsuranceService insuranceService;

    @Autowired
    public void setInsuranceService(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    private CCService ccService;

    @Autowired
    public void setCcService(CCService ccService) {
        this.ccService = ccService;
    }

    private GasService gasService;

    @Autowired
    public void setGasService(GasService gasService) {
        this.gasService = gasService;
    }

    private PascabayarService pascabayarService;

    @Autowired
    public void setPascabayarService(PascabayarService pascabayarService) {
        this.pascabayarService = pascabayarService;
    }

    private MultifinanceService multifinanceService;

    @Autowired
    public void setMultifinanceService(MultifinanceService multifinanceService) {
        this.multifinanceService = multifinanceService;
    }

    private EreceiptKeretaService ereceiptKeretaService;

    @Autowired
    public void setKeretaService(EreceiptKeretaService ereceiptKeretaService) {
        this.ereceiptKeretaService = ereceiptKeretaService;
    }

    private EticketKeretaService eticketKeretaService;

    @Autowired
    public void setEvoucherKeretaService(EticketKeretaService eticketKeretaService) {
        this.eticketKeretaService = eticketKeretaService;
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
        //EMoney / EWALL
//        String json = "{\"id\":\"33fb23d6-1da2-40f4-a14f-ef2b19592f06\",\"invoiceNumber\":\"578/20221107/556\",\"createdAt\":\"2022-11-07T06:46:37.188+00:00\",\"expiredAt\":\"2022-11-07T08:46:37.156+00:00\",\"discountAmount\":500,\"fareAmount\":26155,\"ntaAmount\":25655,\"nraAmount\":500,\"hpAmount\":12,\"adminAmount\":500,\"indexProduct\":\"Uang Elektronik|Mandiri E-Money|25.000|6032982807212897\",\"transactionId\":\"556\"," + "\"transactionType\":\"EWAL\",\"paymentId\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"payment\":{\"id\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"fareAmount\":26155,\"serviceFeeAmount\":2750,\"discountAmount\":500,\"totalAmount\":25655,\"paymentServiceAmount\":28405,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":28405,\"status\":\"PAID\",\"paymentService\":{\"id\":\"VA_BCA\",\"name\":\"BCA Virtual Account\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":2750,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.VA_BCA\"},\"detailId\":\"363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"detailType\":\"virtualAccount\",\"detailRoute\":\"/api/v1/payments/d4cf5865-1bf8-40c4-ac9b-bfa75e342154/virtualAccount/363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"createdAt\":\"2022-11-07T06:49:02.153+00:00\",\"updatedAt\":\"2022-11-07T06:50:03.665+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":578,\"productId\":581,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"EWAL\",\"serviceName\":\"Uang Elektronik\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/e-money_duwsbqv3H.png?tr=h-192,w-192\",\"productCode\":\"E-TOLL MANDIRI\",\"productName\":\"Mandiri E-Money\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/kartu_elektronik/e-money_hM7o2qNzF.png?tr=h-192,w-192\",\"nominalCode\":\"TOLM25\",\"nominalName\":\"Rp 25.000\",\"customerNumber\":\"6032982807212897\",\"serialNumber\":\"166780380555519431\",\"fareAmount\":26155,\"billAmount\":25655,\"adminAmount\":500,\"striketroughAdmin\":1500},\"serverTime\":\"2022-11-08T07:19:02.638+00:00\"}";
        //DWALL
//        String json = "{\"id\":\"33fb23d6-1da2-40f4-a14f-ef2b19592f06\",\"invoiceNumber\":\"578/20221107/556\",\"createdAt\":\"2022-11-07T06:46:37.188+00:00\",\"expiredAt\":\"2022-11-07T08:46:37.156+00:00\",\"discountAmount\":500,\"fareAmount\":26155,\"ntaAmount\":25655,\"nraAmount\":500,\"hpAmount\":12,\"adminAmount\":500,\"indexProduct\":\"Uang Elektronik|Mandiri E-Money|25.000|6032982807212897\",\"transactionId\":\"556\"," + "\"transactionType\":\"DWAL\",\"paymentId\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"payment\":{\"id\":\"d4cf5865-1bf8-40c4-ac9b-bfa75e342154\",\"fareAmount\":26155,\"serviceFeeAmount\":2750,\"discountAmount\":500,\"totalAmount\":25655,\"paymentServiceAmount\":28405,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":28405,\"status\":\"PAID\",\"paymentService\":{\"id\":\"VA_BCA\",\"name\":\"BCA Virtual Account\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":2750,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.VA_BCA\"},\"detailId\":\"363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"detailType\":\"virtualAccount\",\"detailRoute\":\"/api/v1/payments/d4cf5865-1bf8-40c4-ac9b-bfa75e342154/virtualAccount/363bce49-4a1a-46fa-93f2-dfb1710e4347\",\"createdAt\":\"2022-11-07T06:49:02.153+00:00\",\"updatedAt\":\"2022-11-07T06:50:03.665+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":578,\"productId\":581,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"EWAL\",\"serviceName\":\"Uang Elektronik\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/e-money_duwsbqv3H.png?tr=h-192,w-192\",\"productCode\":\"E-TOLL MANDIRI\",\"productName\":\"Mandiri E-Money\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/kartu_elektronik/e-money_hM7o2qNzF.png?tr=h-192,w-192\",\"nominalCode\":\"TOLM25\",\"nominalName\":\"Rp 25.000\",\"customerNumber\":\"6032982807212897\",\"serialNumber\":\"166780380555519431\",\"fareAmount\":26155,\"billAmount\":25655,\"adminAmount\":500,\"striketroughAdmin\":1500},\"serverTime\":\"2022-11-08T07:19:02.638+00:00\"}";
        //NETV
//        String json = "{\"id\":\"7dca2d88-c650-4793-9354-fb871b62ba88\",\"invoiceNumber\":\"1301/20221107/84\",\"createdAt\":\"2022-11-07T08:24:14.966+00:00\",\"expiredAt\":\"2022-11-07T10:24:14.929+00:00\",\"discountAmount\":0,\"fareAmount\":268899,\"ntaAmount\":267405,\"nraAmount\":1495,\"hpAmount\":37,\"adminAmount\":2499,\"indexProduct\":\"Postpaid|Internet & TV Kabel|Indihome / Speedy|122805238348\",\"transactionId\":\"84\",\"transactionType\":\"NETV\",\"paymentId\":\"4825a319-eefc-4246-8047-bc71bfcf72b9\",\"payment\":{\"id\":\"4825a319-eefc-4246-8047-bc71bfcf72b9\",\"fareAmount\":268899,\"serviceFeeAmount\":12,\"discountAmount\":0,\"totalAmount\":268850,\"paymentServiceAmount\":268862,\"depositAmount\":0,\"isSplitBill\":true,\"pointAmount\":49,\"remainingAmount\":268862,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"c2678d6d-1b85-49d1-b17f-9fc8d52897a8\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/4825a319-eefc-4246-8047-bc71bfcf72b9/bankTransfer/c2678d6d-1b85-49d1-b17f-9fc8d52897a8\",\"createdAt\":\"2022-11-07T08:24:47.052+00:00\",\"updatedAt\":\"2022-11-07T08:26:01.637+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1301,\"productId\":1351,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"NETV\",\"serviceName\":\"Internet & TV Kabel\",\"productCode\":\"SPEEDY\",\"productName\":\"Indihome / Speedy\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/TV_Internet/indihome_bdrWbbjgR.png?tr=h-192,w-192\",\"customerNumber\":\"122805238348\",\"customerName\":\"ANISA NURSANTIKA\",\"billQty\":null,\"reference\":\"-\",\"period\":\"202211\",\"adminAmount\":2499,\"fareAmount\":268899,\"billAmount\":266400,\"discountAmount\":0,\"striketroughAdmin\":2500,\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/Internet___TV_JNYo9Nof0.png?tr=h-192,w-192\"},\"serverTime\":\"2022-11-08T07:14:30.463+00:00\"}";
        //PDAM / PAM
//        String json = "{\"id\":\"1f73e9ef-d713-4669-87a9-175ac1079a95\",\"invoiceNumber\":\"1140/20221101/43\",\"createdAt\":\"2022-11-01T00:10:34.557+00:00\",\"expiredAt\":\"2022-11-01T02:10:34.480+00:00\",\"discountAmount\":2498,\"fareAmount\":117128,\"ntaAmount\":116235,\"nraAmount\":895,\"hpAmount\":0,\"adminAmount\":2498,\"indexProduct\":\"Postpaid|PDAM|PDAM KAB BOGOR|19019262\",\"transactionId\":\"43\",\"transactionType\":\"PAM\",\"paymentId\":\"2b19ef61-0b1f-499d-8998-692e540d62b2\",\"payment\":{\"id\":\"2b19ef61-0b1f-499d-8998-692e540d62b2\",\"fareAmount\":117128,\"serviceFeeAmount\":31,\"discountAmount\":2498,\"totalAmount\":114630,\"paymentServiceAmount\":114661,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":114661,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"124a6eb3-eec0-4e7b-9aee-1a361a80ef26\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/2b19ef61-0b1f-499d-8998-692e540d62b2/bankTransfer/124a6eb3-eec0-4e7b-9aee-1a361a80ef26\",\"createdAt\":\"2022-11-01T00:11:48.969+00:00\",\"updatedAt\":\"2022-11-01T00:13:57.684+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1140,\"productId\":1159,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"PAM\",\"serviceName\":\"PDAM\",\"productCode\":\"PDAMBGR\",\"productName\":\"PDAM KAB BOGOR\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/PDAM/Tirta_Dharma_1jIjy6dWt.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/PDAM_nGWJ2kFme.png?tr=h-192,w-192\",\"customerName\":\"NUR ARIFIN.ST\",\"customerNumber\":\"19019262\",\"tagNonAir\":\"0\",\"standMeter\":\"00000826 - 00000840\",\"tempo\":\"0\",\"period\":\"202210\",\"reference\":\"-\",\"adminAmount\":2498,\"billAmount\":114630,\"penaltyAmount\":0,\"discountAmount\":0,\"fareAmount\":117128,\"striketroughAdmin\":2500},\"serverTime\":\"2022-11-08T07:24:35.749+00:00\"}";
        //GAME
//        String json = "{\"id\":\"8b7e04f6-8205-43df-bddc-a74e54386053\",\"invoiceNumber\":\"978/20221106/176\",\"createdAt\":\"2022-11-05T22:54:28.067+00:00\",\"expiredAt\":\"2022-11-06T00:54:27.868+00:00\",\"discountAmount\":0,\"fareAmount\":2087,\"ntaAmount\":1587,\"nraAmount\":500,\"hpAmount\":15,\"adminAmount\":0,\"indexProduct\":\"Voucher Game|Mobile Legends: Bang Bang|5 Diamonds|98376147(2513)\",\"transactionId\":\"176\",\"transactionType\":\"GAME\",\"paymentId\":\"66c1a473-43e4-4749-94fc-5fdd14b10010\",\"payment\":{\"id\":\"66c1a473-43e4-4749-94fc-5fdd14b10010\",\"fareAmount\":2087,\"serviceFeeAmount\":0,\"discountAmount\":0,\"totalAmount\":2087,\"paymentServiceAmount\":2087,\"depositAmount\":2087,\"isSplitBill\":true,\"pointAmount\":0,\"remainingAmount\":2087,\"status\":\"PAID\",\"paymentService\":{\"id\":\"DEPOSIT\",\"name\":\"Holi Cash\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Icon/Holicash_jKmuMF8Xc.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":null,\"closeTime\":\"23:50:00\",\"minimumAmount\":1,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.DEPOSIT\"},\"detailId\":\"13196c7f-0ef2-496e-aed4-dc4313817c73\",\"detailType\":\"deposit\",\"detailRoute\":\"/api/v1/payments/66c1a473-43e4-4749-94fc-5fdd14b10010/deposit/13196c7f-0ef2-496e-aed4-dc4313817c73\",\"createdAt\":\"2022-11-05T22:54:46.386+00:00\",\"updatedAt\":\"2022-11-05T22:54:46.459+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":978,\"productId\":1004,\"detail\":{\"isPostpaid\":false,\"serviceCode\":\"GAME\",\"serviceName\":\"Voucher Game\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/voucher-game_z820zlGiY.png?tr=h-192,w-192\",\"productCode\":\"MOBILE LEGENDS\",\"productName\":\"Mobile Legends: Bang Bang\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/Voucher-Game/Mobile-legend_6mKneWqSy.png\",\"nominalCode\":\"MLBB5\",\"nominalName\":\"5 Diamonds\",\"customerNumber\":\"98376147(2513)\",\"serialNumber\":\"S22262184930\",\"fareAmount\":2087,\"billAmount\":2087,\"adminAmount\":0,\"striketroughAdmin\":0},\"serverTime\":\"2022-11-08T07:22:01.502+00:00\"}";
        //Insurance
//        String json = "{\"id\":\"d1904168-fdeb-4b82-8ea2-db67f4280cf2\",\"invoiceNumber\":\"1261/20221104/63\",\"createdAt\":\"2022-11-04T10:01:58.849+00:00\",\"expiredAt\":\"2022-11-04T12:01:58.812+00:00\",\"discountAmount\":2498,\"fareAmount\":302498,\"ntaAmount\":300285,\"nraAmount\":2215,\"hpAmount\":69,\"adminAmount\":2498,\"indexProduct\":\"Postpaid|Asuransi|BPJS Kesehatan|0001797064356\",\"transactionId\":\"63\",\"transactionType\":\"INS\",\"paymentId\":\"51410f1b-bd90-4589-bdf7-fb703ca3af4d\",\"payment\":{\"id\":\"51410f1b-bd90-4589-bdf7-fb703ca3af4d\",\"fareAmount\":302498,\"serviceFeeAmount\":0,\"discountAmount\":2498,\"totalAmount\":300000,\"paymentServiceAmount\":300000,\"depositAmount\":300000,\"isSplitBill\":true,\"pointAmount\":0,\"remainingAmount\":300000,\"status\":\"PAID\",\"paymentService\":{\"id\":\"DEPOSIT\",\"name\":\"Holi Cash\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Icon/Holicash_jKmuMF8Xc.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":null,\"closeTime\":\"23:50:00\",\"minimumAmount\":1,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.DEPOSIT\"},\"detailId\":\"b306251f-d2c8-470f-b0b6-438d46fe5fa9\",\"detailType\":\"deposit\",\"detailRoute\":\"/api/v1/payments/51410f1b-bd90-4589-bdf7-fb703ca3af4d/deposit/b306251f-d2c8-470f-b0b6-438d46fe5fa9\",\"createdAt\":\"2022-11-04T10:02:31.019+00:00\",\"updatedAt\":\"2022-11-04T10:02:31.119+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1261,\"productId\":1262,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"INS\",\"serviceName\":\"Asuransi\",\"productCode\":\"BPJSKS\",\"productName\":\"BPJS Kesehatan\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/BPJS/BPJS_kesehatan_SIiIqHtPb.png\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/insurance_ZGrZVHpGj.png?tr=h-192,w-192\",\"billQty\":1,\"customerNumber\":\"0001797064356\",\"customerName\":\"GHOMIATI\",\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"branchCode\":null,\"branchName\":null,\"reference\":\"A00C225113DAF960\",\"billAmount\":300000,\"adminAmount\":2498,\"fareAmount\":302498,\"discountAmount\":0,\"striketroughAdmin\":2500},\"serverTime\":\"2022-11-08T07:24:02.994+00:00\"}";
//            String json = "{\"id\":\"74986bd3-025b-47b8-99c1-79b0d6d3527f\",\"invoiceNumber\":\"1261/20221205/81\",\"createdAt\":\"2022-12-05T10:40:00.889+07:00\",\"expiredAt\":\"2022-12-05T12:40:00.858+07:00\",\"discountAmount\":0,\"fareAmount\":302499,\"ntaAmount\":300355,\"nraAmount\":2145,\"hpAmount\":80,\"adminAmount\":2499,\"indexProduct\":\"Postpaid|Asuransi|BPJS Kesehatan|0001801290104\",\"transactionId\":\"81\",\"transactionType\":\"INS\",\"paymentId\":\"8b31d1b4-23da-4718-889d-c5653697136b\",\"payment\":{\"id\":\"8b31d1b4-23da-4718-889d-c5653697136b\",\"fareAmount\":302499,\"serviceFeeAmount\":0,\"discountAmount\":0,\"totalAmount\":302443,\"paymentServiceAmount\":302443,\"depositAmount\":302443,\"isSplitBill\":true,\"pointAmount\":56,\"remainingAmount\":302443,\"status\":\"PAID\",\"paymentService\":{\"id\":\"DEPOSIT\",\"name\":\"Holi Cash\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Icon/Holicash_jKmuMF8Xc.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":null,\"closeTime\":\"23:50:00\",\"minimumAmount\":1,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.DEPOSIT\"},\"detailId\":\"521c1967-c900-4949-acc0-871285eb1d89\",\"detailType\":\"deposit\",\"detailRoute\":\"/api/v1/payments/8b31d1b4-23da-4718-889d-c5653697136b/deposit/521c1967-c900-4949-acc0-871285eb1d89\",\"createdAt\":\"2022-12-05T10:40:08.392+07:00\",\"updatedAt\":\"2022-12-05T10:40:08.427+07:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1261,\"productId\":1262,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"INS\",\"serviceName\":\"Asuransi\",\"productCode\":\"BPJSKS\",\"productName\":\"BPJS Kesehatan\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/BPJS/BPJS_kesehatan_SIiIqHtPb.png\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/insurance_ZGrZVHpGj.png?tr=h-192,w-192\",\"billQty\":1,\"customerNumber\":\"0001801290104\",\"customerName\":\"MEILIANA\",\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"branchCode\":null,\"branchName\":null,\"reference\":\"FC45D26643C15229\",\"billAmount\":300000,\"adminAmount\":2499,\"fareAmount\":302499,\"discountAmount\":0,\"striketroughAdmin\":2500},\"serverTime\":\"2022-12-05T11:12:53.815+07:00\"}";
// Credit Card
//        String json = "{\"id\":\"d1ffddc6-e40c-49ae-aac6-84e99bba1384\",\"invoiceNumber\":\"1284/20220917/17\",\"createdAt\":\"2022-09-17T00:37:43.362+00:00\",\"expiredAt\":\"2022-09-17T02:37:43.346+00:00\",\"discountAmount\":0,\"fareAmount\":604765,\"ntaAmount\":604100,\"nraAmount\":1900,\"hpAmount\":59,\"adminAmount\":4765,\"indexProduct\":\"Postpaid|Tagihan Kartu Kredit|BNI|5426400912076426\",\"transactionId\":\"17\",\"transactionType\":\"CC\",\"paymentId\":\"34bfff15-3a51-4a29-98fd-1961e4c50a62\",\"payment\":{\"id\":\"34bfff15-3a51-4a29-98fd-1961e4c50a62\",\"fareAmount\":604765,\"serviceFeeAmount\":80,\"discountAmount\":0,\"totalAmount\":604765,\"paymentServiceAmount\":604845,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":604845,\"status\":\"REFUNDED\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"41447b77-a4f1-4cbe-bcfe-1c2e7af17054\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/34bfff15-3a51-4a29-98fd-1961e4c50a62/bankTransfer/41447b77-a4f1-4cbe-bcfe-1c2e7af17054\",\"createdAt\":\"2022-09-17T00:38:03.948+00:00\",\"updatedAt\":\"2022-09-20T03:24:46.602+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED_FAILED\",\"serviceId\":1284,\"productId\":1287,\"detail\":{\"isPostpaid\":true,\"parentCode\":null,\"serviceCode\":\"CC\",\"serviceName\":\"Tagihan Kartu Kredit\",\"productCode\":\"KKBNI\",\"productName\":\"BNI\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/kartu_kredit/BNI_UoPfHm6n-.png\",\"customerNumber\":\"542640XXXXXX6426\",\"customerName\":\"\",\"billQty\":1,\"reference\":null,\"adminAmount\":4765,\"billAmount\":600000,\"orderStatus\":\"ISSUED_FAILED\",\"paymentStatus\":\"PAID\",\"striketroughAdmin\":0,\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/angsuran-kredit_HwtTwB0p_.png?tr=h-192,w-192\"},\"serverTime\":\"2022-11-14T07:39:30.048+00:00\"}";
        //GAS
//        String json = "{\"id\":\"a815a6c0-9dbe-4ae2-9d34-237ca1b78c92\",\"invoiceNumber\":\"1352/20221107/5\",\"createdAt\":\"2022-11-07T06:31:29.767+00:00\",\"expiredAt\":\"2022-11-07T11:31:29.741+00:00\",\"discountAmount\":0,\"fareAmount\":50498,\"ntaAmount\":49705,\"nraAmount\":795,\"hpAmount\":19,\"adminAmount\":2498,\"indexProduct\":\"Postpaid|GAS|Perusahaan Gas Negara|015564140\",\"transactionId\":\"5\",\"transactionType\":\"GAS\",\"paymentId\":\"9e75b0d6-46a2-4183-977e-f471b3be29cc\",\"payment\":{\"id\":\"9e75b0d6-46a2-4183-977e-f471b3be29cc\",\"fareAmount\":50498,\"serviceFeeAmount\":0,\"discountAmount\":0,\"totalAmount\":50436,\"paymentServiceAmount\":50436,\"depositAmount\":50436,\"isSplitBill\":true,\"pointAmount\":62,\"remainingAmount\":50436,\"status\":\"PAID\",\"paymentService\":{\"id\":\"DEPOSIT\",\"name\":\"Holi Cash\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Icon/Holicash_jKmuMF8Xc.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":null,\"closeTime\":\"23:50:00\",\"minimumAmount\":1,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.DEPOSIT\"},\"detailId\":\"8d00596c-d5f8-40e5-8ca1-3df3ea7e6660\",\"detailType\":\"deposit\",\"detailRoute\":\"/api/v1/payments/9e75b0d6-46a2-4183-977e-f471b3be29cc/deposit/8d00596c-d5f8-40e5-8ca1-3df3ea7e6660\",\"createdAt\":\"2022-11-07T06:34:39.058+00:00\",\"updatedAt\":\"2022-11-07T06:34:39.081+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1352,\"productId\":1353,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"GAS\",\"serviceName\":\"GAS\",\"productCode\":\"GASPGN\",\"productName\":\"Perusahaan Gas Negara\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/Gas/Pgn_gas_OK5t-zg2a.png\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/Gas_i2tSuYNog.png\",\"customerName\":\"YAYU SRI RAHAYU\",\"customerNumber\":\"015564140\",\"tarif\":null,\"standMeter\":null,\"tempo\":null,\"period\":\"Okt\",\"reference\":\"-\",\"adminAmount\":2498,\"billAmount\":48000,\"penaltyAmount\":null,\"discountAmount\":0,\"fareAmount\":50498,\"striketroughAdmin\":2500},\"serverTime\":\"2022-11-08T07:20:00.826+00:00\"}";
        //Pasca bayar
//        String json = "{\"id\":\"1d7070c0-fb54-422b-84bd-cafd4f014075\",\"invoiceNumber\":\"1294/20221107/48\",\"createdAt\":\"2022-11-07T04:37:56.768+00:00\",\"expiredAt\":\"2022-11-07T06:37:56.746+00:00\",\"discountAmount\":2498,\"fareAmount\":194724,\"ntaAmount\":192231,\"nraAmount\":2495,\"hpAmount\":77,\"adminAmount\":2498,\"indexProduct\":\"Postpaid|Pascabayar|Telkomsel Halo|08121413004\",\"transactionId\":\"48\",\"transactionType\":\"TLP\",\"paymentId\":\"ee707c8b-225b-4eb5-b685-71b930a699f3\",\"payment\":{\"id\":\"ee707c8b-225b-4eb5-b685-71b930a699f3\",\"fareAmount\":194724,\"serviceFeeAmount\":50,\"discountAmount\":2498,\"totalAmount\":124669,\"paymentServiceAmount\":124719,\"depositAmount\":67488,\"isSplitBill\":true,\"pointAmount\":69,\"remainingAmount\":124719,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"03765680-d115-4cc1-ae9b-a1cbd3262160\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/ee707c8b-225b-4eb5-b685-71b930a699f3/bankTransfer/03765680-d115-4cc1-ae9b-a1cbd3262160\",\"createdAt\":\"2022-11-07T04:38:30.480+00:00\",\"updatedAt\":\"2022-11-07T04:40:01.730+00:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1294,\"productId\":1300,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"TLP\",\"serviceName\":\"Pascabayar\",\"productCode\":\"TLPTSEL\",\"productName\":\"Telkomsel Halo\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/operator/Telkomsel_g-KMOm9zbVp.png\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/pascabayar__8OiMs_IK.png?tr=h-192,w-192\",\"customerNumber\":\"08121413004\",\"customerName\":\"ROHXXXXXXYAD\",\"reference\":\"-\",\"billQty\":null,\"period\":null,\"adminAmount\":2498,\"fareAmount\":194724,\"billAmount\":192226,\"striketroughAdmin\":2500,\"discountAmount\":0,\"tempo\":\"-\"},\"serverTime\":\"2022-11-08T07:20:45.337+00:00\"}";
        //Multifnance
//        String json = "{\"id\":\"c637c9fa-ab00-4575-ac6b-bdfa610d072c\",\"invoiceNumber\":\"1266/20221110/57\",\"createdAt\":\"2022-11-10T08:51:53.891+00:00\",\"expiredAt\":\"2022-11-10T10:51:53.846+00:00\",\"discountAmount\":0,\"fareAmount\":1179499,\"ntaAmount\":1175865,\"nraAmount\":3635,\"hpAmount\":113,\"adminAmount\":0,\"indexProduct\":\"Postpaid|Multifinance|Mega Auto Finance|1512100730\",\"transactionId\":\"57\",\"transactionType\":\"MFN\",\"paymentId\":\"dc5da691-348d-42b8-837f-3a1936c51b50\",\"payment\":{\"id\":\"dc5da691-348d-42b8-837f-3a1936c51b50\",\"fareAmount\":1179499,\"serviceFeeAmount\":91,\"discountAmount\":0,\"totalAmount\":1179499,\"paymentServiceAmount\":1179590,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":1179590,\"status\":\"PAYMENT_EXPIRED\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"a5435b30-7faa-4076-bf32-ea3af1478dc9\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/dc5da691-348d-42b8-837f-3a1936c51b50/bankTransfer/a5435b30-7faa-4076-bf32-ea3af1478dc9\",\"createdAt\":\"2022-11-10T08:52:18.302+00:00\",\"updatedAt\":\"2022-11-10T10:51:54.113+00:00\"},\"paymentStatus\":\"PAYMENT_EXPIRED\",\"orderStatus\":\"ORDER_EXPIRED\",\"serviceId\":1266,\"productId\":1275,\"detail\":{\"isPostpaid\":true,\"serviceCode\":\"MFN\",\"serviceName\":\"Multifinance\",\"productCode\":\"FNMAF\",\"productName\":\"Mega Auto Finance\",\"productImageUrl\":\"https://ik.imagekit.io/holigo/multifinance/Mega_Auto_Finance_1tPOGH0SL.png?tr=h-192,w-192\",\"serviceImageUrl\":\"https://ik.imagekit.io/holigo/icon_produk_tagihan/multifinance_oyuJVzv3FP4.png?tr=h-192,w-192\",\"customerNumber\":\"1512100730\",\"customerName\":\"SUSANTY\",\"period\":\"14\",\"tenor\":null,\"tempo\":\"\",\"interest\":null,\"branch\":null,\"vehicle\":null,\"policeNumber\":\"\",\"reference\":null,\"orderStatus\":\"ORDER_EXPIRED\",\"paymentStatus\":\"PAYMENT_EXPIRED\",\"adminAmount\":0,\"billAmount\":1179500,\"fareAmount\":1179499,\"discountAmount\":1,\"striketroughAdmin\":0},\"serverTime\":\"2022-11-14T10:48:30.973+00:00\"}";
        //Holicash
//        String json = "{\"id\":\"9cdc0a73-138e-4986-b408-869b08317d0f\",\"invoiceNumber\":\"1380/20221128/376\",\"createdAt\":\"2022-11-28T14:27:16.305+07:00\",\"expiredAt\":\"2022-11-28T16:27:16.250+07:00\",\"discountAmount\":0,\"fareAmount\":100000,\"ntaAmount\":100000,\"nraAmount\":0,\"hpAmount\":0,\"adminAmount\":0,\"indexProduct\":\"Top Up|Holi Cash|100,000|628117876746\",\"transactionId\":\"376\",\"transactionType\":\"HTD\",\"paymentId\":\"43441303-5727-4722-9b5e-29cc402ac5ce\",\"payment\":{\"id\":\"43441303-5727-4722-9b5e-29cc402ac5ce\",\"fareAmount\":100000,\"serviceFeeAmount\":24,\"discountAmount\":0,\"totalAmount\":100000,\"paymentServiceAmount\":100024,\"depositAmount\":0,\"isSplitBill\":false,\"pointAmount\":0,\"remainingAmount\":100024,\"status\":\"PAID\",\"paymentService\":{\"id\":\"BT_BCA\",\"name\":\"BCA Bank Transfer\",\"imageUrl\":\"https://ik.imagekit.io/holigo/Bank/BCA_bGtQ3AAv4F-.png?ik-sdk-version=javascript-1.4.3\",\"openTime\":\"00:15:00\",\"closeTime\":\"21:00:00\",\"minimumAmount\":10000,\"maximumAmount\":null,\"serviceFee\":0,\"mdrPercent\":0,\"fdsAmount\":0,\"status\":\"AVAILABLE\",\"note\":\"payment_service.note.BT_BCA\"},\"detailId\":\"3f9180a9-7f49-4843-a758-cb6968603f4e\",\"detailType\":\"bankTransfer\",\"detailRoute\":\"/api/v1/payments/43441303-5727-4722-9b5e-29cc402ac5ce/bankTransfer/3f9180a9-7f49-4843-a758-cb6968603f4e\",\"createdAt\":\"2022-11-28T14:27:19.178+07:00\",\"updatedAt\":\"2022-11-28T14:28:04.997+07:00\"},\"paymentStatus\":\"PAID\",\"orderStatus\":\"ISSUED\",\"serviceId\":1380,\"productId\":1380,\"detail\":null,\"serverTime\":\"2022-11-29T11:54:35.938+07:00\"}";
        //Train
//        String json = "{\"id\":\"247176f8-832a-4b4d-80a5-d0561f0503f1\",\"invoiceNumber\":\"3/20221129/21\",\"createdAt\":\"2022-11-29T16:27:57.560+07:00\",\"expiredAt\":\"2022-11-29T17:07:57.407+07:00\",\"discountAmount\":0,\"fareAmount\":52500,\"ntaAmount\":47500,\"nraAmount\":5000,\"hpAmount\":125,\"adminAmount\":7500,\"indexProduct\":\"O|Jakarta - Bandung|2022-12-22 17:55:00\",\"transactionId\":\"21\",\"transactionType\":\"TRAIN\",\"paymentId\":null,\"payment\":null,\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"serviceId\":3,\"productId\":3,\"detail\":{\"id\":21,\"userId\":24403,\"iconUrl\":\"https://ik.imagekit.io/holigo/Icon_produk_homepage/pesawat_ZwrZ8JLMI.png?ik-sdk-version=javascript-1.4.3&updatedAt=1648702711249\",\"transactionId\":\"247176f8-832a-4b4d-80a5-d0561f0503f1\",\"contactPerson\":{\"name\":\"Rizki Pambudi\",\"phoneNumber\":\"628994636544\",\"email\":\"erpambudi0@gmail.com\"},\"tripType\":\"O\",\"trips\":[{\"id\":\"8e657800-b7d8-41e0-a62c-1400e2d49150\",\"passengers\":[{\"passenger\":{\"id\":null,\"type\":\"ADULT\",\"title\":\"MR\",\"name\":\"Ahmad Faisal Ibrahim\",\"phoneNumber\":null,\"identityCard\":{\"idCardNumber\":\"3671070902010001\"},\"passport\":null,\"baggageCode\":null,\"seatCode\":null},\"seatNumber\":\"EKO-EKO-1-3C\"}],\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"trainName\":\"CIKURAY\",\"trainNumber\":\"7048B\",\"originStation\":{\"id\":\"PSE\",\"name\":\"Pasar Senen\",\"city\":\"Jakarta\"},\"destinationStation\":{\"id\":\"BD\",\"name\":\"Bandung\",\"city\":\"Bandung\"},\"adultAmount\":1,\"childAmount\":0,\"infantAmount\":0,\"departureDate\":\"2022-12-22\",\"departureTime\":\"17:55:00\",\"arrivalDate\":\"2022-12-22\",\"arrivalTime\":\"22:03:00\",\"imageUrl\":\"https://ik.imagekit.io/holigo/transportasi/logo-kai-main_SyEqhgYKx.png\",\"trainClass\":\"EKO\",\"trainSubClass\":\"C\",\"fareAmount\":45000,\"adminAmount\":0,\"hpAmount\":0,\"hpcAmount\":0,\"bookCode\":\"EGJ5YV4\"}],\"isBookable\":true,\"expiredAt\":\"2022-11-29T17:07:57.407+07:00\",\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"fareAmount\":52500,\"adminAmount\":7500,\"discountAmount\":0,\"hpAmount\":125,\"hpcAmount\":125,\"seatMapUrl\":\"/api/v1/train/transactions/21/trips\"},\"serverTime\":\"2022-11-29T16:41:59.094+07:00\"}";
//        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);
        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

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
            case "PUL", "PD", "PR", "GAME" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-pulsa-" + transactionDto.getInvoiceNumber() + ".pdf");
                String type = transactionDto.getTransactionType();
                pdfPulsaService.invoicePulsa(transactionDto, response, stylePdfService, type);
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
                        "attachment;filename=" + "invoice-D-wall-" + transactionDto.getInvoiceNumber() + ".pdf");
                digitalWalletService.DigitalWallet(transactionDto, response, stylePdfService);
            }
            case "NETV" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Net-TV-" + transactionDto.getInvoiceNumber() + ".pdf");
                neTVService.invoiceNeTV(transactionDto, response, stylePdfService);
            }
            case "PAM" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-PDAM-" + transactionDto.getInvoiceNumber() + ".pdf");
                pdamServce.invoicePDAM(transactionDto, response, stylePdfService);
            }
            case "INS" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Insurance-" + transactionDto.getInvoiceNumber() + ".pdf");
                insuranceService.insuranceInvoice(transactionDto, response, stylePdfService);
            }
            case "CC" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Insurance-" + transactionDto.getInvoiceNumber() + ".pdf");
                ccService.invoiceCreditCard(transactionDto, response, stylePdfService);
            }
            case "GAS" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Gas-" + transactionDto.getInvoiceNumber() + ".pdf");
                gasService.invoiceGas(transactionDto, response, stylePdfService);
            }
            case "TLP" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Pascabayar-" + transactionDto.getInvoiceNumber() + ".pdf");
                pascabayarService.invoicePascabayar(transactionDto, response, stylePdfService);
            }
            case "MFN" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Multifinance-" + transactionDto.getInvoiceNumber() + ".pdf");

                multifinanceService.multifinanceInvoice(transactionDto, response, stylePdfService);
            }
            case "HTD" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Holicash-" + transactionDto.getInvoiceNumber() + ".pdf");

                holicashService.invoiceHolicash(transactionDto, response, stylePdfService);
            }
            case "TRAIN" -> {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + "invoice-Train-" + transactionDto.getInvoiceNumber() + ".pdf");

                ereceiptKeretaService.invoiceKereta(transactionDto, response, stylePdfService);
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
        //train
        String json = "{\"id\":\"247176f8-832a-4b4d-80a5-d0561f0503f1\",\"invoiceNumber\":\"3/20221129/21\",\"createdAt\":\"2022-11-29T16:27:57.560+07:00\",\"expiredAt\":\"2022-11-29T17:07:57.407+07:00\",\"discountAmount\":0,\"fareAmount\":52500,\"ntaAmount\":47500,\"nraAmount\":5000,\"hpAmount\":125,\"adminAmount\":7500,\"indexProduct\":\"O|Jakarta - Bandung|2022-12-22 17:55:00\",\"transactionId\":\"21\",\"transactionType\":\"TRAIN\",\"paymentId\":null,\"payment\":null,\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"serviceId\":3,\"productId\":3,\"detail\":{\"id\":21,\"userId\":24403,\"iconUrl\":\"https://ik.imagekit.io/holigo/Icon_produk_homepage/pesawat_ZwrZ8JLMI.png?ik-sdk-version=javascript-1.4.3&updatedAt=1648702711249\",\"transactionId\":\"247176f8-832a-4b4d-80a5-d0561f0503f1\",\"contactPerson\":{\"name\":\"Rizki Pambudi\",\"phoneNumber\":\"628994636544\",\"email\":\"erpambudi0@gmail.com\"},\"tripType\":\"O\",\"trips\":[{\"id\":\"8e657800-b7d8-41e0-a62c-1400e2d49150\",\"passengers\":[{\"passenger\":{\"id\":null,\"type\":\"ADULT\",\"title\":\"MR\",\"name\":\"Ahmad Faisal Ibrahim\",\"phoneNumber\":null,\"identityCard\":{\"idCardNumber\":\"3671070902010001\"},\"passport\":null,\"baggageCode\":null,\"seatCode\":null},\"seatNumber\":\"EKO-EKO-1-3C\"}],\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"trainName\":\"CIKURAY\",\"trainNumber\":\"7048B\",\"originStation\":{\"id\":\"PSE\",\"name\":\"Pasar Senen\",\"city\":\"Jakarta\"},\"destinationStation\":{\"id\":\"BD\",\"name\":\"Bandung\",\"city\":\"Bandung\"},\"adultAmount\":1,\"childAmount\":0,\"infantAmount\":0,\"departureDate\":\"2022-12-22\",\"departureTime\":\"17:55:00\",\"arrivalDate\":\"2022-12-22\",\"arrivalTime\":\"22:03:00\",\"imageUrl\":\"https://ik.imagekit.io/holigo/transportasi/logo-kai-main_SyEqhgYKx.png\",\"trainClass\":\"EKO\",\"trainSubClass\":\"C\",\"fareAmount\":45000,\"adminAmount\":0,\"hpAmount\":0,\"hpcAmount\":0,\"bookCode\":\"EGJ5YV4\"}],\"isBookable\":true,\"expiredAt\":\"2022-11-29T17:07:57.407+07:00\",\"paymentStatus\":\"SELECTING_PAYMENT\",\"orderStatus\":\"BOOKED\",\"fareAmount\":52500,\"adminAmount\":7500,\"discountAmount\":0,\"hpAmount\":125,\"hpcAmount\":125,\"seatMapUrl\":\"/api/v1/train/transactions/21/trips\"},\"serverTime\":\"2022-11-29T16:41:59.094+07:00\"}";
        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);

//        TransactionDto transactionDto = transactionService.getTransactionDetail(id);
        StylePdfService stylePdfService = new StylePdfService();
        if (transactionDto.getTransactionType().equals("AIR")) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "eticket-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfAirlineService.airlineEticket(transactionDto, response);
        } else if (transactionDto.getTransactionType().equals("TRAIN")) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "eticket-" + transactionDto.getInvoiceNumber() + ".pdf");
            eticketKeretaService.eticketKereta(transactionDto, response, stylePdfService);
        } else {
            System.err.print("transaction type is not availabel in this api");
        }
    }

    //HOTEL
    @GetMapping("/web/v1/invoice/{id}/evoucher")
    public void downloadEvoucher(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException {

        TransactionDto transactionDto = transactionService.getTransactionDetail(id);

        if (transactionDto.getTransactionType().equals("HTL")) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + "eVoucher-" + transactionDto.getInvoiceNumber() + ".pdf");
            pdfHotelService.eVoucherHotel(transactionDto, response);
        } else {
            System.err.print("transaction type is not HTL");
        }

    }


}
