package id.holigo.services.holigoinvoiceservice.services.pdfHotel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.HotelFasilitasDto;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfHotelServiceImpl implements PdfHotelService {

    private final ObjectMapper objectMapper;

    @Override
    public void eReceiptHotel(TransactionDto transactionDto, HttpServletResponse response) throws MalformedURLException {
        StylePdfService stylePdfService = new StylePdfService();

        //starter pack
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        PdfFont plusJakarta;
        try {
//            plusJakarta = PdfFontFactory.createFont("src/main/resources/static/fonts/PlusJakartaSans-Regular.ttf");
            plusJakarta = PdfFontFactory.createFont();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PdfFont plusJakartaDisplayBold = null;
        try {
//            plusJakartaDisplayBold = PdfFontFactory.createFont("src/main/resources/static/fonts/PlusJakartaDisplay-Bold.otf");
            plusJakartaDisplayBold = PdfFontFactory.createFont();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfDocument.addNewPage();

//        Image
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataPaid = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/holigo-paid_ahXX6qW67gl.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143805976");
        Image imagePaid = new Image(imageDataPaid).scaleAbsolute(128, 128);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image imageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);

//        Size Table Declarartion

        float col = 200f;
        float colHalf = 100f;
        float[] twoCol = {350f, 200f};

        // - - - - - HEADER  - - - - -
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, "Bukti Pembayaran", "Hotel"));

        //--> ID TRANSAKSI
        document.add(stylePdfService.transaksiId(plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));

        // - - - - - Body Part  - - - - -

        //        Detail Pemesanan buyer
        Table detailPemesananHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        detailPemesananHead.addCell(stylePdfService.getHeaderTextCell("Detail Pemesanan", plusJakarta));
        Table tblDetailPemesanan = new Table(new float[]{col, 30, col, 30, col});
        tblDetailPemesanan.setMarginLeft(8);
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("Nama Lengkap", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("Email", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdfService.getTextDetail("Nomor Ponsel", plusJakarta));


        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("name").asText(), plusJakartaDisplayBold));

        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("email").asText(), plusJakartaDisplayBold));

        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("phoneNumber").asText(), plusJakartaDisplayBold));

        document.add(stylePdfService.smallSpace(pdfDocument));
        document.add(detailPemesananHead);
        document.add(tblDetailPemesanan);
        //      Detail Pembayaran Title


        //        Detail Pembayaran output
        Boolean statusPayment = false;
        try {
            if (transactionDto.getPayment() != null && transactionDto.getPayment().getStatus().toString().equalsIgnoreCase("PAID")) {
                Table detailPembayaranHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
                detailPembayaranHead.addCell(stylePdfService.getHeaderTextCell("Detail Pembayaran", plusJakarta));

                Table detailPembayaran = new Table(new float[]{150, 10, 60, 20, col});
                detailPembayaran.setMarginLeft(8);
                detailPembayaran.addCell(stylePdfService.getTextDetail("Waktu pembayaran", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakarta));
                detailPembayaran.addCell(stylePdfService.getTextDetail("Metode pembayaran", plusJakarta));


                document.add(stylePdfService.space(pdfDocument));

                DateFormat inputDatePayment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                DateFormat outputDatePayment = new SimpleDateFormat("E, dd MMM yyyy");
                DateFormat outputTimePayment = new SimpleDateFormat("HH:mm");
                //Payment timestamp to date
                Date paymentDate = null;
                try {
                    paymentDate = inputDatePayment.parse(transactionDto.getPayment().getUpdatedAt().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                detailPembayaran.addCell(stylePdfService.getDetailUserBold(outputDatePayment.format(paymentDate), plusJakartaDisplayBold));
                //Payment timestamp to Time
                detailPembayaran.addCell(stylePdfService.getTextDetail(" ", plusJakartaDisplayBold));
                Date paymentTime = null;
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
        } catch (Exception ignored) {
            statusPayment = false;
        }

        document.add(stylePdfService.smallSpace(pdfDocument));
        document.add(stylePdfService.brokeLine(pdfDocument));


//        Detail Produk title
        Table detailProdukTbl = new Table(new float[]{colHalf, colHalf, col + colHalf, col, col});
        detailProdukTbl.setMarginLeft(20);
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("No.", plusJakarta));
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("Produk", plusJakarta));
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("Deskripsi", plusJakarta));
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("Jumlah", plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(stylePdfService.getHeaderTextCell("Harga", plusJakarta));

//        Detail Produk output
        Table deskripsiProduk = new Table(new float[]{150f});
        //nama hotel
        String[] namaHotel = transactionDto.getDetail().get("hotel").get("name").asText().split(" ");
        int longNameHtl = namaHotel.length;
        if (longNameHtl < 3) {
            deskripsiProduk.addCell(stylePdfService.getDetailProdukOutput(namaHotel[0] + " " + namaHotel[1], plusJakarta));
        } else {
            deskripsiProduk.addCell(stylePdfService.getDetailProdukOutput(namaHotel[0] + " " + namaHotel[1] + " " + namaHotel[2], plusJakarta));
        }
        //nama room
        deskripsiProduk.addCell(stylePdfService.getDetailProdukOutput(transactionDto.getDetail().get("room").get("name").asText(), plusJakarta).setFontSize(8).setPaddingTop(-6));

        double adminAmount;
        double discount = transactionDto.getDiscountAmount().doubleValue();
        double fareAmount;

        int count = 1; //dummy
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput("" + count, plusJakarta));
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput(transactionDto.getDetail().get("hotel").get("type").asText(), plusJakarta));
        detailProdukTbl.addCell(new Cell().add(deskripsiProduk).setBorder(Border.NO_BORDER));
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput("1", plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(stylePdfService.getDetailProdukOutput("Rp " + stylePdfService.getPrice(transactionDto.getFareAmount().floatValue()) + ",- ", plusJakarta));


        String fareAmountStr = transactionDto.getFareAmount().toString();
        fareAmount = Float.parseFloat(fareAmountStr);

        // pricing on produk
        if (transactionDto.getDetail().get("adminAmount") != null) {
            String adminAmountStr = transactionDto.getDetail().get("adminAmount").asText();
            adminAmount = Float.parseFloat(adminAmountStr);

        } else {
            String adminAmountStr = "0";
            adminAmount = Float.parseFloat(adminAmountStr);
        }

        if (transactionDto.getDiscountAmount() != null && transactionDto.getDiscountAmount().doubleValue() > 0) {
            String discountStr = transactionDto.getDiscountAmount().toString();
            discount = Float.parseFloat(discountStr);
        }


        document.add(detailProdukTbl);
        document.add(stylePdfService.brokeLine(pdfDocument));


//        Bill / Price / harga
        Table priceTable = new Table(twoCol);
        Table nestedPrice = new Table(new float[]{col / 2, col - 50});
        nestedPrice.addCell(stylePdfService.getHeaderTextCell("Sub Total", plusJakarta));

        nestedPrice.addCell(stylePdfService.getDetailProdukOutput("Rp " + stylePdfService.getPrice(fareAmount) + ",-", plusJakarta));
//        nestedPrice.addCell(stylePdf.getHeaderTextCell("Biaya Jasa", plusJakarta));
//        nestedPrice.addCell(stylePdf.getDetailProdukOutput("Rp " + stylePdf.getPrice(adminAmount) + ",-", plusJakarta));
        System.out.println("discout amount : " + transactionDto.getDiscountAmount().toString());
        if (transactionDto.getDiscountAmount() != null && transactionDto.getDiscountAmount().doubleValue() > 0) {

            nestedPrice.addCell(stylePdfService.getHeaderTextCell("Discount", plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput("Rp - " + stylePdfService.getPrice(discount) + ",-", plusJakarta));
        } else {
            nestedPrice.addCell(stylePdfService.getHeaderTextCell(" ", plusJakarta));
            nestedPrice.addCell(stylePdfService.getDetailProdukOutput(" ", plusJakarta));
        }
        double finalPrice = fareAmount + adminAmount - discount;
        nestedPrice.addCell(stylePdfService.getDetailProdukOutput("- - - - - - - - - ", plusJakarta).setBold());
        nestedPrice.addCell(stylePdfService.getDetailProdukOutput(" - - - - - - - - - - - - - - - - -", plusJakarta).setPaddingLeft(-25f).setBold());
        nestedPrice.addCell(stylePdfService.getHeaderTextCell("Total", plusJakarta));
        nestedPrice.addCell(new Cell().add("Rp " + stylePdfService.getPrice(finalPrice) + ",-")
                .setPaddings(0, 5, 0, 5)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setFontSize(12)
                .setBorder(Border.NO_BORDER)
                .setFontColor(Color.BLACK));

//        Paid Logo, logo position
        if (statusPayment) {
            priceTable.addCell(new Cell().add(imagePaid).setPaddings(10, 0, 0, 100).setBorder(Border.NO_BORDER));
        } else {
            priceTable.addCell(new Cell().add("").setPaddings(10, 0, 0, 100).setBorder(Border.NO_BORDER));
        }
        priceTable.addCell(new Cell().add(nestedPrice).setBorder(Border.NO_BORDER));
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
        OutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            baos.writeTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void eVoucherHotel(TransactionDto transactionDto, HttpServletResponse response) throws MalformedURLException {

        //starter pack
        StylePdfService stylePdfService = new StylePdfService();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        PdfFont plusJakarta = null;
        try {
//            plusJakarta = PdfFontFactory.createFont("src/main/resources/static/fonts/PlusJakartaSans-Regular.ttf");
            plusJakarta = PdfFontFactory.createFont();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfDocument.addNewPage();

        //        Image
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image imageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);
        ImageData websiteHtlData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/globe_-fzTbtsF6.png?ik-sdk-version=javascript-1.4.3&updatedAt=1666176833029");
        Image websiteHtlImg = new Image(websiteHtlData).scaleAbsolute(9, 8);
        ImageData emailHtlData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/email_5U8FSm4O-_hpSHynbTw.png?ik-sdk-version=javascript-1.4.3&updatedAt=1666177621077");
        Image emailHtlImg = new Image(emailHtlData).scaleAbsolute(9, 8);

        ImageData timeImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/time_GTSbqaC0g_82z2L8w2D.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783948");
        Image timeImg = new Image(timeImgData).scaleAbsolute(44, 44);
        ImageData ticketHelpData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/voucher_Rmwi88eJd_h5rAd0OAg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225784204");
        Image ticketHelpImg = new Image(ticketHelpData).scaleAbsolute(44, 44);
        ImageData informationImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/information_9VP2VqxAc_wLTMswxFx.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783892");
        Image informationImg = new Image(informationImgData).scaleAbsolute(44, 44);
        ImageData hotelImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/hotel_yvXZta7bi_r6HibDZTm.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663906035018");
        Image hotelImg = new Image(hotelImgData).scaleAbsolute(104, 104);

        // Image point
        ImageData lineImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/lne_MCAF9RJZLU.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100438");
        Image lineImg = new Image(lineImgData).scaleAbsolute(1, 24);
        ImageData startImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/start_point_AVgXLqfRZY.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100803");
        Image startImg = new Image(startImgData).scaleAbsolute(10, 10);
        ImageData endImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/end_point_LsUTxBDyIR.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100442");
        Image endImg = new Image(endImgData).scaleAbsolute(10, 10);

        //Image Hotel Logo body 1.2
        ImageData addressImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/map-marker_doURF3f0I.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100453");
        Image addressImg = new Image(addressImgData).scaleAbsolute(13, 13);

        ImageData apartementImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/apartment_o7EqI6q4U.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100442");
        Image apartementImg = new Image(apartementImgData).scaleAbsolute(13, 13);

        ImageData bedImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/bed_66QdpYGyEI.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100480");
        Image bedImg = new Image(bedImgData).scaleAbsolute(13, 13);

        ImageData roomImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/room_bNmWKMvWKH.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100487");
        Image roomImg = new Image(roomImgData).scaleAbsolute(13, 13);

        ImageData phoneHtlImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/Phone_call_ywJ7a8IZ1.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821101061");
        Image phoneHtlImg = new Image(phoneHtlImgData).scaleAbsolute(13, 13);

        ImageData userImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/user_m0PJTXiXv.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663821100446");
        Image userImg = new Image(userImgData).scaleAbsolute(13, 13);

        ImageData star5ImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/star_5_n-GLqHYTr_KY50lE9X5.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663840539107");
        Image star5Img = new Image(star5ImgData).scaleAbsolute(58, 10);
        ImageData star4ImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/star_4_gAU-lIkTl_WIAYMipzw.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663840539095");
        Image star4Img = new Image(star4ImgData).scaleAbsolute(58, 10);
        ImageData star3ImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/star_3_4uz5yMix8_hkI0Jaire.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663840539071");
        Image star3Img = new Image(star3ImgData).scaleAbsolute(58, 10);
        ImageData star2ImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/star_2_b9uzoVgGeL_wBigl38lM.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663840539080");
        Image star2Img = new Image(star2ImgData).scaleAbsolute(58, 10);
        ImageData star1ImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/hotel/star_1_hTMpwPBSgU_lN7WSWV9F.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663840539057");
        Image star1Img = new Image(star1ImgData).scaleAbsolute(58, 10);

        //TIme FORMATING
        SimpleDateFormat oldTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat oldTimeFormatWithoutSS = new SimpleDateFormat("HH:mm");
        SimpleDateFormat newTimeFormat = new SimpleDateFormat("HH:mm");

        // - - - - - HEADER  - - - - -
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, "Voucher", "Pemesanan Hotel"));


        //--> ID TRANSAKSI
        document.add(stylePdfService.transaksiId(plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));
        document.add(stylePdfService.spaceInColumn());

        // - - - - - BODY  - - - - -
        Table body1 = new Table(new float[]{170, 200, 210});

        //        1.1 --HELP/Intruction--
        Table helpTbl = new Table(new float[]{45, 134});
        helpTbl.addCell(new Cell().add(ticketHelpImg).setBorder(Border.NO_BORDER));
        Paragraph ticketPrg = new Paragraph();
        ticketPrg.setFixedLeading(10);
        ticketPrg.add("Tunjukan identitas diri dan bukti pemesanan saat check-in");
        helpTbl.addCell(stylePdfService.getInfo(ticketPrg, plusJakarta));
        helpTbl.addCell(new Cell().add(timeImg).setBorder(Border.NO_BORDER));
        Paragraph timePrg = new Paragraph();
        timePrg.add("Check-in sesuai dengan waktu yang tertera pada bukti pemesanan").setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(timePrg, plusJakarta));
        helpTbl.addCell(new Cell().add(informationImg).setBorder(Border.NO_BORDER));
        Paragraph infoPrg = new Paragraph();
        infoPrg.add("Waktu yang tertera sesuai dengan waktu hotel setempat").setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(infoPrg, plusJakarta));

        //      1.1 Closer
        body1.addCell(new Cell().add(helpTbl).setBorder(Border.NO_BORDER).setPaddings(0, 10, 0, 10));

        //      1.2 -- Hotel Profile
        //      1.2.1 apartement nama hotel
        Table hotelBody = new Table(new float[]{220});
        Table hotelProfile = new Table(new float[]{30, 190});
        hotelProfile.addCell(stylePdfService.logoPositionHtl(apartementImg));
        Paragraph namaHotel = new Paragraph();
        namaHotel.add(transactionDto.getDetail().get("hotel").get("name").asText());
        namaHotel.setFixedLeading(15);
        hotelProfile.addCell(new Cell().add(namaHotel)
                .setBold()
                .setFont(plusJakarta)
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setPaddings(-2, 0, -2, 0)
                .setRelativePosition(0, 2, 0, 0));

        //      1.2.1,5 rating
        hotelProfile.addCell(new Cell().add(" ").setBorder(Border.NO_BORDER));
        int rating = transactionDto.getDetail().get("hotel").get("rating").asInt();
        switch (rating) {
            case 1 -> hotelProfile.addCell(new Cell().add(star1Img).setBorder(Border.NO_BORDER));
            case 2 -> hotelProfile.addCell(new Cell().add(star2Img).setBorder(Border.NO_BORDER));
            case 3 -> hotelProfile.addCell(new Cell().add(star3Img).setBorder(Border.NO_BORDER));
            case 4 -> hotelProfile.addCell(new Cell().add(star4Img).setBorder(Border.NO_BORDER));
            case 5 -> hotelProfile.addCell(new Cell().add(star5Img).setBorder(Border.NO_BORDER));
        }

        //      1.2.2 address
        hotelProfile.addCell(stylePdfService.logoPositionHtl(addressImg));
        Paragraph addressHotel = new Paragraph();
        addressHotel.add(transactionDto.getDetail().get("hotel").get("location").get("address").asText());
        addressHotel.setFixedLeading(10);
        hotelProfile.addCell(new Cell().add(addressHotel)
                .setFontSize(8)
                .setFont(plusJakarta)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setBorder(Border.NO_BORDER));

        //      1.2.3 Contact Hotel
        if (transactionDto.getDetail().get("hotel").get("contacts").get(0).get("type").asText().equalsIgnoreCase("website")) {
            hotelProfile.addCell(stylePdfService.logoPositionHtl(websiteHtlImg));
        } else if (transactionDto.getDetail().get("hotel").get("contacts").get(0).get("type").asText().equalsIgnoreCase("email")) {
            hotelProfile.addCell(stylePdfService.logoPositionHtl(emailHtlImg));
        } else if (transactionDto.getDetail().get("hotel").get("contacts").get(0).get("type").asText().equalsIgnoreCase("phone")) {
            hotelProfile.addCell(stylePdfService.logoPositionHtl(phoneHtlImg));
        }


        hotelProfile.addCell(stylePdfService.outputHotelBody(transactionDto.getDetail().get("hotel").get("contacts").get(0).get("detail").asText(), plusJakarta).setFontSize(8));


        //      1.2.3,5 space
        hotelProfile.addCell(stylePdfService.outputHotelBody("\n", plusJakarta));
        hotelProfile.addCell(stylePdfService.outputHotelBody("\n", plusJakarta));

        //      1.2.4 Room Class
        hotelProfile.addCell(stylePdfService.logoPositionHtl(bedImg));
        hotelProfile.addCell(new Cell().add(WordUtils.capitalizeFully(transactionDto.getDetail().get("room").get("name").asText()))
                .setFontSize(12)
                .setBold()
                .setFont(plusJakarta)
                .setBorder(Border.NO_BORDER)
//                .setPaddings(-1, 0, -1, 0)
                .setRelativePosition(0, 0, 0, 2));

        //      1.2.5 Room
        hotelProfile.addCell(stylePdfService.logoPositionHtl(roomImg));
        hotelProfile.addCell(stylePdfService.outputHotelBody(transactionDto.getDetail().get("roomAmount").asText() + " kamar", plusJakarta));
        hotelProfile.addCell(stylePdfService.smallSpaceInColumn());
        hotelProfile.addCell(stylePdfService.smallSpaceInColumn());

        //      1.2.6 User
        hotelProfile.addCell(stylePdfService.logoPositionHtl(userImg));
        hotelProfile.addCell(stylePdfService.outputHotelBody(transactionDto.getDetail().get("guestAmount").asText() + " Tamu", plusJakarta));
        hotelProfile.addCell(stylePdfService.smallSpaceInColumn());
        hotelProfile.addCell(stylePdfService.smallSpaceInColumn());
        hotelBody.addCell(new Cell().add(hotelProfile).setBorder(Border.NO_BORDER));

        //      1.2.7 Kode Booking
        hotelBody.addCell(new Cell().add(" ").setHeight(15).setBorder(Border.NO_BORDER));
        hotelBody.addCell(new Cell().add("Kode Booking").setFontSize(9).setFontColor(new DeviceRgb(97, 97, 97)).setBorder(Border.NO_BORDER).setRelativePosition(10, 0, 0, 0));
        if (transactionDto.getDetail().get("voucherCode") != null) {
            hotelBody.addCell(new Cell().add(transactionDto.getDetail().get("voucherCode").asText()).setFontSize(20).setFontColor(new DeviceRgb(0, 189, 23)).setBorder(Border.NO_BORDER).setRelativePosition(12, 0, 0, 0).setPaddings(-5, 0, -5, 0));
        }
        //      1.2 Closer
        body1.addCell(new Cell().add(hotelBody).setBorderTop(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderBottom(Border.NO_BORDER));

        //      1.3 -- Checkin n Checkout
        // 1.3 Date N Time
        Table dateCheck = new Table(new float[]{190});
        // 1.3.1 Two Column In Table Date Check
        Table dateCheckSplit = new Table(new float[]{130, 80});

        // 1.3.1.1 table in Col 1
        Table dateCheckSplitDesc = new Table(new float[]{130});
        dateCheckSplitDesc.addCell(stylePdfService.getCheck("Check-in", plusJakarta));
        // date Check IN, date formater
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        //formating date
        Date checkingDate;

        try {
            checkingDate = inputDate.parse(transactionDto.getDetail().get("checkIn").asText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String checkin = outputFormat.format(checkingDate);
        Paragraph dateCheckIn = new Paragraph();
        dateCheckIn.add(checkin);
        dateCheckSplitDesc.addCell(stylePdfService.getHotelBold12(dateCheckIn, plusJakarta));

        // time Check IN

        try {
            Date timeCheckin = oldTimeFormatWithoutSS.parse(transactionDto.getDetail().get("hotel").get("rules").get(0).get("value").asText());
            dateCheckSplitDesc.addCell(stylePdfService.getCheckTime(newTimeFormat.format(timeCheckin), plusJakarta));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        /*
                    SPACE BETWEEN CHECKIN AND CHECK OUT
         */
        dateCheckSplitDesc.addCell(new Cell().add(stylePdfService.spaceInColumn()).setHeight(30).setBorder(Border.NO_BORDER));
        dateCheckSplitDesc.addCell(stylePdfService.getCheck("Check-out", plusJakarta));
        // date Check OUT date formater

        Date checkoutDate;
        try {
            checkoutDate = inputDate.parse(transactionDto.getDetail().get("checkOut").asText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String checkOut = outputFormat.format(checkoutDate);

        Paragraph dateCheckOut = new Paragraph();
        dateCheckOut.add(checkOut);
        dateCheckSplitDesc.addCell(stylePdfService.getHotelBold12(dateCheckOut, plusJakarta));
        // time Check OUT
        try {
            Date checkOutTime = oldTimeFormatWithoutSS.parse(transactionDto.getDetail().get("hotel").get("rules").get(1).get("value").asText());
            dateCheckSplitDesc.addCell(stylePdfService.getCheckTime(newTimeFormat.format(checkOutTime), plusJakarta));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //closer in COL 1
        dateCheckSplit.addCell(new Cell().add(dateCheckSplitDesc).setBorder(Border.NO_BORDER));

        // 1.3.1.2 table in Col 2
        Table dateCheckSplitDuration = new Table(new float[]{80});
        dateCheckSplitDuration.addCell(new Cell().add(stylePdfService.smallSpaceInColumn()).setBorder(Border.NO_BORDER).setHeight(11.5f));
        dateCheckSplitDuration.addCell(new Cell().add(startImg).setRelativePosition(25, 0, 0, 0).setBorder(Border.NO_BORDER));
        dateCheckSplitDuration.addCell(new Cell().add(lineImg).setRelativePosition(29.5f, 0, 0, 0).setPaddingTop(-2).setBorder(Border.NO_BORDER));
        int durationDay = transactionDto.getDetail().get("durations").asInt();
        int durationNight = 0;
        if (durationDay == 1) {
            durationNight = 1;
        } else {
            durationNight = durationDay - 1;
        }
        int[] duration = {durationDay, durationNight};
        dateCheckSplitDuration.addCell(new Cell().add(duration[0] + " hari " + duration[1] + " malam").setFontSize(7).setFontColor(new DeviceRgb(71, 71, 71)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        dateCheckSplitDuration.addCell(new Cell().add(lineImg).setRelativePosition(29.5f, 0, 0, 0).setPaddingBottom(-2).setBorder(Border.NO_BORDER));
        dateCheckSplitDuration.addCell(new Cell().add(endImg).setRelativePosition(25, 0, 0, 0).setBorder(Border.NO_BORDER));
        //closer in COL 2
        dateCheckSplit.addCell(new Cell().add(dateCheckSplitDuration).setBorder(Border.NO_BORDER));


        dateCheck.addCell(new Cell().add(dateCheckSplit).setBorder(Border.NO_BORDER));
        //image Hotel
        dateCheck.addCell(new Cell().add(hotelImg).setBorder(Border.NO_BORDER).setRelativePosition(40, 0, 0, 0));
        // closer 1.3
        body1.addCell(new Cell().add(dateCheck).setBorder(Border.NO_BORDER));
        document.add(body1);
        document.add(stylePdfService.getSpacePara(10));

        // - - - - - BODY 2  - - - - -
        Table body2 = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        Paragraph titledetail = new Paragraph();
        titledetail.add("Detail Pemesanan");
        body2.addCell(stylePdfService.getHotelBold12(titledetail, plusJakarta).setTextAlignment(TextAlignment.LEFT));

        //       2.1 Detail Pemesanan

        document.add(body2);
        document.add(stylePdfService.brokeLineEvoucher(pdfDocument));
        document.add(stylePdfService.getSpacePara(0));
        Table detailPemesananTbl = new Table(new float[]{130, 10, 300});
        // nama 2.1.1
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut("Nama Lengkap", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut(":", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(transactionDto.getDetail().get("guest").get("name").asText(), plusJakarta));
        // jumlah tamu 2.1.2
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut("Jumlah tamu", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut(":", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(transactionDto.getDetail().get("guestAmount") + " orang", plusJakarta));

        // tipe kamar 2.1.3
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut("Tipe kamar", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut(":", plusJakarta));

        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(WordUtils.capitalizeFully(transactionDto.getDetail().get("room").get("name").asText()), plusJakarta));

        // fasilitas 2.1.4
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut("Fasilitas", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut(":", plusJakarta));

        List<HotelFasilitasDto> hotelFasilitasDtoList = null;
        try {
            hotelFasilitasDtoList = objectMapper.readValue(transactionDto.getDetail().get("hotel").get("facilities").traverse(), new TypeReference<List<HotelFasilitasDto>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> fasilitasList = new ArrayList<>();
        for (HotelFasilitasDto hotelfacility : hotelFasilitasDtoList) {
            int index = 0;
            hotelfacility.getItems().forEach(facilityFor -> {
                fasilitasList.add(facilityFor + ", ");
            });
        }

        Paragraph fasilitas = new Paragraph();
        String fasilitasText = "";

        for (int count = 0; count <= 10; count++) {
            String fasilitasTextPart = fasilitasList.get(count);
            fasilitasText = fasilitasText + fasilitasTextPart;
        }
        StringBuilder sb = new StringBuilder(fasilitasText);
        sb.deleteCharAt(fasilitasText.length() - 2);
        fasilitas.add(sb.toString());

        fasilitas.setFixedLeading(11);
        detailPemesananTbl.addCell(new Cell().add(fasilitas)
//                .setPaddings(6, 0, 0, 10)
                .setRelativePosition(10, 3, 0, 0)
                .setFontColor(new DeviceRgb(71, 71, 71)).setFontSize(10)
                .setBorder(Border.NO_BORDER)
                .setFont(plusJakarta));

        // permintaan 2.1.5
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut("Permintaan khusus", plusJakarta));
        detailPemesananTbl.addCell(stylePdfService.getDetailPemesananAtribut(":", plusJakarta));
        if (transactionDto.getDetail().get("guest").get("note") != null) {
            if (transactionDto.getDetail().get("guest").get("note").asText().equals("")) {
                detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(" - ", plusJakarta));
            } else {
                detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(transactionDto.getDetail().get("guest").get("note").asText(), plusJakarta));
            }

        } else {
            detailPemesananTbl.addCell(stylePdfService.getDetailPemesananOutput(" - ", plusJakarta));
        }

        // 2.1 closer
        document.add(detailPemesananTbl);


        // 3.1 Syarat dan Ketentuan
        Table term = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        term.addCell(new Cell().add(stylePdfService.getSpacePara(5)).setBorder(Border.NO_BORDER));
        term.addCell(new Cell().add("* Syarat dan Ketentuan " + transactionDto.getDetail().get("hotel").get("name").asText()).setBorder(Border.NO_BORDER).setFontSize(7).setFont(plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("Tidak bisa refund & reschedule", plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("• Pemesanan kamar ini tidak bisa di-refund atau di-reschedule. Jika tidak datang ke akomodasi untuk check-in, kamu akan dianggap no-show dan tidak diberikan pengembalian dana.", plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("• Nominal dari penggunaan Holi Point atau promo tidak bisa di-refund.", plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("Catatan", plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("• Saat check-in, tanda pengenal resmi yang dikeluarkan pemerintah dan kartu kredit atau setoran tunai mungkin diperlukan untuk kebutuhan biaya tak terduga." + "Terpenuhinya permintaan khusus bergantung pada ketersediaan pada saat check-in dan mungkin memerlukan biaya tambahan", plusJakarta));
        term.addCell(stylePdfService.getSyaratKetentuan("• Biaya tambahan seperti parkir, deposit, telepon, layanan kamar ditangani langsung antara tamu dan hotel. Tamu tambahan di kamar juga mungkin memerlukan biaya tambahan yang bervariasi tergantung pada kebijakan akomodasi\n", plusJakarta));


        document.add(stylePdfService.brokeLineEvoucher(pdfDocument));
        document.add(term);

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
            log.info("ERROR Download Evoucher,Invoice Number  -> {}", transactionDto.getInvoiceNumber());
        }

    }
}
