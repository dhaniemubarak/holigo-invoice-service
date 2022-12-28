package id.holigo.services.holigoinvoiceservice.services.pdfKereta;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EreceiptKeretaServiceImpl implements EreceiptKeretaService {


    @Autowired
    private final MessageSource messageSource;

    @Override
    public void invoiceKereta(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService) {

        //starter pack
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        PdfFont plusJakarta;
        try {
            plusJakarta = PdfFontFactory.createFont();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PdfFont plusJakartaDisplayBold;
        try {
            plusJakartaDisplayBold = PdfFontFactory.createFont();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfDocument.addNewPage();

        //image Data
        ImageData imageDataLogo = null;
        try {
            imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataPaid = null;
        try {
            imageDataPaid = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/holigo-paid_ahXX6qW67gl.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143805976");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image imagePaid = new Image(imageDataPaid).scaleAbsolute(128, 128);
        ImageData imageDataEmail = null;
        try {
            imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image imageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = null;
        try {
            phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);

        float col = 200f;
        float[] twoCol = {350f, 200f};

        // - - - - - HEADER  - - - - -
        String title = messageSource.getMessage("invoice.generic-title-bukti-pembayaran", null, LocaleContextHolder.getLocale());
        String subTitle = messageSource.getMessage("invoice.subtitle-kereta", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, title, subTitle));
        //--> ID TRANSAKSI
        String transactionId = messageSource.getMessage("invoice.id-transaksi", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.transaksiId(transactionId, plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));

        //        Detail Pemesanan buyer
        Table detailPemesananHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        String bookingDetails = messageSource.getMessage("invoice.detail-pemesanan", null, LocaleContextHolder.getLocale());
        detailPemesananHead.addCell(stylePdfService.getHeaderTextCell(bookingDetails, plusJakarta));
        Table tblDetailPemesanan = new Table(new float[]{150, 30, 150, 30, 150});
        tblDetailPemesanan.setMarginLeft(8);
        // PART 1 nama detail pemesanan
        String namaCustomer = messageSource.getMessage("invoice.generic-name", null, LocaleContextHolder.getLocale());
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail(namaCustomer, plusJakarta)); //col 1
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));

        String meterNumber = messageSource.getMessage("invoice.generic-reference", null, LocaleContextHolder.getLocale());
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail(meterNumber, plusJakarta)); //col 2
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));

        String idPelanggan = messageSource.getMessage("invoice.generic-nomor-pelanggan", null, LocaleContextHolder.getLocale());
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail(idPelanggan, plusJakarta)); //col 3

        // pemesanan output
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("name").asText(), plusJakartaDisplayBold)); //col 1
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("email").asText(), plusJakartaDisplayBold)); //col 2
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("phoneNumber").asText(), plusJakartaDisplayBold)); //col 3


        document.add(stylePdfService.smallSpace(pdfDocument));
        document.add(detailPemesananHead);
        document.add(tblDetailPemesanan);


        //        Detail Pembayaran output
        boolean statusPayment = false;
        try {
            if (transactionDto.getPayment() != null && transactionDto.getPayment().getStatus().toString().equalsIgnoreCase("PAID")) {
                Table detailPembayaranHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
                //detail pembayaran
                String paymentDetail = messageSource.getMessage("invoice.generic-detail-payment", null, LocaleContextHolder.getLocale());
                detailPembayaranHead.addCell(stylePdfService.getHeaderTextCell(paymentDetail, plusJakarta));
                Table detailPembayaran = new Table(new float[]{150, 10, 60, 20, col});
                detailPembayaran.setMarginLeft(8);
                // waktu pembayaran
                String paymentTimeDetail = messageSource.getMessage("invoice.generic-date-payment", null, LocaleContextHolder.getLocale());
                detailPembayaran.addCell(stylePdfService.getTextDetail(paymentTimeDetail, plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                String paymentMethod = messageSource.getMessage("invoice.generic-method-payment", null, LocaleContextHolder.getLocale());
                detailPembayaran.addCell(stylePdfService.getTextDetail(paymentMethod, plusJakarta));

                document.add(stylePdfService.space(pdfDocument));

                DateFormat inputDatePayment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                DateFormat outputDatePayment = new SimpleDateFormat("EE, dd MMM yyyy");
                DateFormat outputTimePayment = new SimpleDateFormat("HH:mm");
                //Payment timestamp to date
                Date paymentDate;
                paymentDate = inputDatePayment.parse(transactionDto.getPayment().getUpdatedAt().toString());
                detailPembayaran.addCell(stylePdfService.getDetailUserBold(outputDatePayment.format(paymentDate), plusJakartaDisplayBold));
                //Payment timestamp to Time
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakartaDisplayBold));
                Date paymentTime;
                paymentTime = inputDatePayment.parse(transactionDto.getPayment().getUpdatedAt().toString());
                detailPembayaran.addCell(stylePdfService.getDetailUserBold(
                                outputTimePayment.format(paymentTime) + " WIB", plusJakartaDisplayBold)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPaddings(0, 0, 0, 0));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakartaDisplayBold));

                detailPembayaran.addCell(stylePdfService.getDetailUserBold(transactionDto.getPayment().getPaymentService().getName(), plusJakartaDisplayBold));

                document.add(detailPembayaranHead);
                document.add(detailPembayaran);
                statusPayment = true;
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Detail pembayaran got some error");
        }
        document.add(stylePdfService.smallSpace(pdfDocument));
        document.add(stylePdfService.brokeLine(pdfDocument));


//        Detail Produk title
        Table detailProdukTbl = new Table(new float[]{60, 220, 230, 200, col});
        detailProdukTbl.setMarginLeft(20);
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("No.", plusJakarta)); //col1
        String product = messageSource.getMessage("invoice.generic-produk", null, LocaleContextHolder.getLocale());

        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell(product, plusJakarta)); //col2
        String desctiptionProduct = messageSource.getMessage("invoice.generic-deskripsi", null, LocaleContextHolder.getLocale());

        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell(desctiptionProduct, plusJakarta)); //col3
        String quantity = messageSource.getMessage("invoice.generic-quantity", null, LocaleContextHolder.getLocale());

        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell(quantity, plusJakarta)); //col4
        String price = messageSource.getMessage("invoice.generic-price", null, LocaleContextHolder.getLocale());

        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell(price, plusJakarta)); //col5

//        Detail Produk output
        int count = 1; //dummy
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput("" + count, plusJakarta)); //col 1

        String produk = messageSource.getMessage("invoice.kereta-produk",null,LocaleContextHolder.getLocale()); //col 2
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput(produk, plusJakarta));

        Table deskripsiTable = new Table(new float[] {230});
        String trainName = transactionDto.getDetail().get("trips").get(0).get("trainName").asText(); //col 3
        String[] destination = transactionDto.getIndexProduct().split("\\|");
        String[] trainClass = {transactionDto.getDetail().get("trips").get(0).get("trainClass").asText(),transactionDto.getDetail().get("trips").get(0).get("trainSubClass").asText()};
        String trainNumber = transactionDto.getDetail().get("trips").get(0).get("trainNumber").asText();

        deskripsiTable.addCell(stylePdfService.getDetailProdukOutput(trainName,plusJakarta));
        deskripsiTable.addCell(stylePdfService.getDetailProdukOutputDesc(destination[1],plusJakarta));
        deskripsiTable.addCell(stylePdfService.getDetailProdukOutputDesc(trainClass[0]+" - " +trainClass[1],plusJakarta));
        deskripsiTable.addCell(stylePdfService.getDetailProdukOutputDesc(trainNumber,plusJakarta));

        detailProdukTbl.addCell(new Cell().add(deskripsiTable.setBorder(Border.NO_BORDER)).setBorder(Border.NO_BORDER));

        // col 4
        int amount = transactionDto.getDetail().get("trips").get(0).get("adultAmount").intValue() + transactionDto.getDetail().get("trips").get(0).get("childAmount").intValue() + transactionDto.getDetail().get("trips").get(0).get("infantAmount").intValue();
        String amountStr = Integer.toString(amount);
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput(amountStr, plusJakarta));

        double billAmount = transactionDto.getDetail().get("billAmount").doubleValue();// col5
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput("Rp " + stylePdfService.getPrice(billAmount) + ",- ", plusJakarta));

        document.add(detailProdukTbl);
        document.add(stylePdfService.brokeLine(pdfDocument));

//        Bill / Price / harga

        // checking 0 or not
        double adminAmount;
        if (transactionDto.getAdminAmount() != null) {
            adminAmount = transactionDto.getAdminAmount().doubleValue();
        } else {
            adminAmount = 0.0;
        }
        double discount = transactionDto.getDiscountAmount().doubleValue();
        if (transactionDto.getDiscountAmount() != null && transactionDto.getDiscountAmount().doubleValue() > 0) {
            String discountStr = transactionDto.getDiscountAmount().toString();
            discount = Float.parseFloat(discountStr);
        }

        Table priceTable = new Table(twoCol);
        Table nestedPrice = new Table(new float[]{100, 150});
        Table bungkusNestedPrice = new Table(new float[]{250});
        nestedPrice.addCell(stylePdfService.getHeaderTextCell("Sub Total", plusJakarta));

        nestedPrice.addCell(stylePdfService.getDetailProdukOutput("Rp " + stylePdfService.getPrice(billAmount) + ",-", plusJakarta));

        //kalau tidak ada discount, teks discount hilang
        if (transactionDto.getDiscountAmount() != null && transactionDto.getDiscountAmount().doubleValue() > 0) {
            String discountStr = messageSource.getMessage("invoice.generic-discount", null, LocaleContextHolder.getLocale());
            nestedPrice.addCell(stylePdfService.getHeaderTextCell(discountStr, plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput("Rp - " + stylePdfService.getPrice(discount) + ",-", plusJakarta));
        } else {
            nestedPrice.addCell(stylePdfService.getHeaderTextCell(" ", plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput(" ", plusJakarta));
        }
        //kalau tidak ada admin amount, teks biaya jasa hilang
        if (transactionDto.getDiscountAmount() != null && transactionDto.getAdminAmount().doubleValue() > 0) {
            String biayaJasa = messageSource.getMessage("invoice.generic-admin-amount", null, LocaleContextHolder.getLocale());
            nestedPrice.addCell(stylePdfService.getHeaderTextCell(biayaJasa, plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput("Rp " + stylePdfService.getPrice(adminAmount) + ",-", plusJakarta));
        } else {
            nestedPrice.addCell(stylePdfService.getHeaderTextCell(" ", plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput(" ", plusJakarta));
        }
//        nestedPrice.addCell()
        bungkusNestedPrice.addCell(new Cell().add(nestedPrice).setBorder(Border.NO_BORDER));
        double serviceFeeAmoung;
        try {
            serviceFeeAmoung = transactionDto.getPayment().getServiceFeeAmount().doubleValue();
        }catch (NullPointerException e){
            serviceFeeAmoung = 0.0;
        }

        double finalPrice = billAmount + adminAmount + serviceFeeAmoung - discount;
        bungkusNestedPrice.addCell(new Cell().add(" - - - - - - - - - - - - - - - - - - - - - - - -").setBold().setBorder(Border.NO_BORDER));
        Table totalTable = new Table(new float[]{100, 150});
        totalTable.addCell(stylePdfService.getHeaderTextCell("Total", plusJakarta));
        totalTable.addCell(stylePdfService.totalOutput(finalPrice,plusJakarta));

        bungkusNestedPrice.addCell(new Cell().add(totalTable).setBorder(Border.NO_BORDER));

//        Paid Logo, logo position
        if (statusPayment) {
            priceTable.addCell(new Cell().add(imagePaid).setPaddings(10, 0, 0, 100).setBorder(Border.NO_BORDER));
        } else {
            priceTable.addCell(new Cell().add("").setPaddings(10, 0, 0, 100).setBorder(Border.NO_BORDER));
        }

        priceTable.addCell(new Cell().add(bungkusNestedPrice).setBorder(Border.NO_BORDER));
        priceTable.setTextAlignment(TextAlignment.CENTER);

        document.add(priceTable);

        //footer
        document.add(stylePdfService.footer(plusJakarta, pdfDocument, imageMail, phoneImg));


        //- - - - - -closer- - - - - -
        pdfDocument.close();
        document.close();

        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        try {
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.info("ERROR Download invoice,Invoice Number  -> {}", transactionDto.getInvoiceNumber());
        }

    }
}
