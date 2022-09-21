package id.holigo.services.holigoinvoiceservice.services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@RequiredArgsConstructor
@Service
@Slf4j
public class PdfAirlineServiceImpl implements PdfAirlineService {

    @Override
    public void airlineEreceipt(TransactionDto transactionDto) throws IOException {

        PdfWriter pdfWriter = new PdfWriter("airline-invoice.pdf");


        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        PdfFont plusJakarta = PdfFontFactory.createFont("fonts/PlusJakartaSans-Regular.ttf");
        PdfFont plusJakartaDisplayBold = PdfFontFactory.createFont("fonts/PlusJakartaDisplay-Bold.otf");
        PdfFont plusJakartaDisplayMedium = PdfFontFactory.createFont("fonts/PlusJakartaDisplay-Medium.otf");
        PdfFont plusJakartaDisplayLight = PdfFontFactory.createFont("fonts/PlusJakartaDisplay-Light.otf");
        pdfDocument.addNewPage();

//        Image
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataPaid = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/holigo-paid_ahXX6qW67gl.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143805976");
        Image imagePaid = new Image(imageDataPaid).scaleAbsolute(128, 128);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image ImageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);
//        Size Table Declarartion
        float threeCol = 190f;
        float col = 200f;
        float colHalf = 100f;
        float col150 = col + 150;
        float[] twoCol = {col150, col};
        float[] fullWidth = {threeCol * 3};
//        Color
        Color colorTitle = new DeviceRgb(0, 188, 22);
        Color colorSubTitle = new DeviceRgb(123, 123, 123);

        //        Line Header
        Border oneLineBd = new SolidBorder(Color.BLACK, 1f / 4f);
        Table oneLine = new Table(fullWidth);
        oneLine.setBorder(oneLineBd);
        //        Space
        Paragraph space = new Paragraph("\n");

        Table smallSpace = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

        //        Border Line putus
        Table brokeLine = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        brokeLine.addCell(new Cell().add(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setFontSize(20).setTextAlignment(TextAlignment.CENTER));

        //      --> HEADER START
        Table nestedBuktiTable = new Table(new float[]{col150});
        nestedBuktiTable.addCell(new Cell().add("Bukti Pembayaran")
                .setBorder(Border.NO_BORDER)
                .setFontColor(colorTitle)
                .setFontSize(22).setBold().setFont(plusJakarta)
                .setPaddings(0, 0, 0, 0));
        nestedBuktiTable.addCell(new Cell().add("Penerbangan Pesawat")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 1)
                .setFontColor(colorSubTitle)
                .setFont(plusJakarta).setFontSize(12).setBold());

        Table headerTable = new Table(twoCol);
        headerTable.setMarginTop(-24);
        headerTable.addCell(new Cell().add(nestedBuktiTable).setBorder(Border.NO_BORDER));
//        Logo Holigo
        headerTable.addCell(new Cell().add(imageLogo).setPaddings(20, 0, 0, 44).setBorder(Border.NO_BORDER));

        document.add(headerTable);

//      --> ID TRANSAKSI START
        Table idTransaksiTbl = new Table(new float[]{colHalf, colHalf / 2});
        idTransaksiTbl.addCell(getHeaderTextCell("ID Transaksi", plusJakarta));
        idTransaksiTbl.addCell(new Cell().add(transactionDto.getInvoiceNumber())
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setFontColor(new DeviceRgb(32, 34, 33))
                .setMargins(-2, 0, -2, 0)
                .setPaddingBottom(-10));
        idTransaksiTbl.setMarginBottom(8);

        document.add(idTransaksiTbl);
        document.add(oneLine);

//        Detail Pemesanan start
        Table detailPemesananHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        detailPemesananHead.addCell(getHeaderTextCell("Detail Pemesanan", plusJakarta));
        Table tblDetailPemesanan = new Table(new float[]{col, 30, col, 30, col});
        tblDetailPemesanan.setMarginLeft(8);
        tblDetailPemesanan.addCell(getTextDetail("Nama Lengkap", plusJakarta));
        tblDetailPemesanan.addCell(getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(getTextDetail("Email", plusJakarta));
        tblDetailPemesanan.addCell(getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(getTextDetail("Nomor Ponsel", plusJakarta));

//        Detail Pemesanan buyer

        tblDetailPemesanan.addCell(getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("name").asText(), plusJakartaDisplayBold));
        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("email").asText(), plusJakartaDisplayBold));
        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("phoneNumber").asText(), plusJakartaDisplayBold));


//        Header Detail Pembayaran
        Table detailPembayaranHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        detailPembayaranHead.addCell(getHeaderTextCell("Detail Pembayaran", plusJakarta));

//        Detail Pembayaran atribute
        Table detailPembayaran = new Table(new float[]{col - 50f, 10, 60, 20, col});
        detailPembayaran.setMarginLeft(8);
        detailPembayaran.addCell(getTextDetail("waktu pembayaran", plusJakarta));
        detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(getTextDetail("Metode pembayaran", plusJakarta));
        document.add(smallSpace);
        document.add(detailPemesananHead);
        document.add(tblDetailPemesanan);
        document.add(space);

//        Detail Pembayaran user
        //        Dummy payment
        detailPembayaran.addCell(getDetailUserBold("Senin, 12 Februari 2021", plusJakartaDisplayBold));
        detailPembayaran.addCell(getTextDetail(" ", plusJakartaDisplayBold));
        detailPembayaran.addCell(getDetailUserBold("13:5 WIB", plusJakartaDisplayBold).setTextAlignment(TextAlignment.CENTER)
                .setPaddings(0, 0, 0, 0));
        detailPembayaran.addCell(getTextDetail(" ", plusJakartaDisplayBold));
//        Payment method
//        transactionDto.getPayment().getPaymentService().getName()
        detailPembayaran.addCell(getDetailUserBold("Mandiri - Virtual Account", plusJakartaDisplayBold));


        document.add(detailPembayaranHead);
        document.add(detailPembayaran);
        document.add(smallSpace);
        document.add(brokeLine);

//        Detail Produk Head
        Table detailProdukTbl = new Table(new float[]{col, col, col, col, col});
        detailProdukTbl.setMarginLeft(20);
        detailProdukTbl.addCell(getHeaderTextCell("No.", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Produk", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Deskripsi", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Jumlah", plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(getHeaderTextCell("Harga", plusJakarta));

//        Detail Produk
        double adminAmount;
        double discount;
        double fareAmount;
        Table deskripsiProduk = new Table(new float[]{100f});
        deskripsiProduk.addCell(getDetailProdukOutput("" + transactionDto.getDetail().get("trips").get(0).get("itineraries").get(0).get("airlinesName").asText(), plusJakarta));
        deskripsiProduk.addCell(getDetailProdukOutput(transactionDto.getDetail().get("trips").get(0).get("itineraries").get(0).get("originAirport").get("id").asText() + "-" + transactionDto.getDetail().get("trips").get(0).get("itineraries").get(0).get("destinationAirport").get("id").asText(), plusJakarta).setFontSize(8).setPaddingTop(-6));
        //hardcode
        deskripsiProduk.addCell(getDetailProdukOutput("Kode Booking : VFYRSW", plusJakarta).setFontSize(8).setPaddingTop(-6));

        int count = 1;
        detailProdukTbl.addCell(getDetailProdukOutput("" + count, plusJakarta));
        detailProdukTbl.addCell(getDetailProdukOutput("Tiket pesawat", plusJakarta));
        detailProdukTbl.addCell(new Cell().add(deskripsiProduk).setBorder(Border.NO_BORDER));
        detailProdukTbl.addCell(getDetailProdukOutput("" + transactionDto.getDetail().get("trips").size(), plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(getDetailProdukOutput("Rp " + getPrice(transactionDto.getFareAmount().floatValue()) + ",-", plusJakarta));

        // pricing
        String fareAmountStr = transactionDto.getDetail().get("fareAmount").asText();
        fareAmount = Float.parseFloat(fareAmountStr);
        String adminAmountStr = transactionDto.getDetail().get("adminAmount").asText();
        adminAmount = Float.parseFloat(adminAmountStr);
        String discountStr = transactionDto.getDetail().get("discountAmount").asText();
        discount = Float.parseFloat(discountStr);

        document.add(detailProdukTbl);
        document.add(brokeLine);


//        Bill / Price / harga
        Table priceTable = new Table(twoCol);
        Table nestedPrice = new Table(new float[]{col / 2, col - 50});
        nestedPrice.addCell(getHeaderTextCell("Sub Total", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(fareAmount) + ",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Biaya Jasa", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(adminAmount) + ",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Discount", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(discount) + ",-", plusJakarta));

        double finalPrice = fareAmount + adminAmount - discount;
        nestedPrice.addCell(getDetailProdukOutput("- - - - - - - - - ", plusJakarta).setBold());
        nestedPrice.addCell(getDetailProdukOutput("  - - - - - - - - - - - - - - - - -", plusJakarta).setPaddingLeft(-23f).setBold());
        nestedPrice.addCell(getHeaderTextCell("Total", plusJakarta));
        nestedPrice.addCell(new Cell().add("Rp " + getPrice(finalPrice) + ",-")
                .setPaddings(0, 5, 0, 5)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setFontSize(12)
                .setBorder(Border.NO_BORDER)
                .setFontColor(Color.BLACK));

//        Paid Logo, logo position
        priceTable.addCell(new Cell().add(imagePaid).setPaddings(10, 0, 0, 100).setBorder(Border.NO_BORDER));
        priceTable.addCell(new Cell().add(nestedPrice).setBorder(Border.NO_BORDER));
        priceTable.setTextAlignment(TextAlignment.CENTER);

        document.add(priceTable);

//      Left Footer
        float defaultSizeWidth = pdfDocument.getDefaultPageSize().getWidth();
        Table footerTbl = new Table(new float[]{defaultSizeWidth / 2, 100});
//        Position of footer
        footerTbl.setFixedPosition(pdfDocument.getNumberOfPages(), 24, 24, 564);
        footerTbl.addCell(new Cell().add("PT. Aplikasi Liburan Indonesia")
                .setBold()
                .setFontSize(8)
                .setBorder(Border.NO_BORDER)
                .setFont(plusJakarta));
        footerTbl.addCell(new Cell().add("Contact Holi Care")
                .setBold()
                .setFontSize(8)
                .setBorder(Border.NO_BORDER)
                .setFont(plusJakarta));
        // detail text
        footerTbl.addCell(new Cell().add("Head Office")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER)
        );
//        Nested table for email
        Table emailHoligo = new Table(new float[]{10, 50});
        emailHoligo.addCell(new Cell().add(ImageMail)
                .setPaddings(5f, 0, 0, 0)
                .setBorder(Border.NO_BORDER)
        );
        emailHoligo.addCell(new Cell().add("cs@holigo.co.id")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setPaddings(0, 0, 0, 5)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER)
        );
        footerTbl.addCell(new Cell().add(emailHoligo)
                .setBorder(Border.NO_BORDER)
        );
        footerTbl.addCell(new Cell().add("Jl. Bukit Golf 1 P17 Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15310 Indonesia")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setPaddings(-7, 0, 0, 2)
                .setBorder(Border.NO_BORDER));

//        Right Footer
        Table contactNumberTbl = new Table(new float[]{10, 50});
        contactNumberTbl.addCell(new Cell().add(phoneImg).setBorder(Border.NO_BORDER)
                .setPaddings(-1, 0, 0, 0));
        contactNumberTbl.addCell(new Cell().add("+6281388882386")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setPaddings(-4, 0, 0, 5)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER));

        contactNumberTbl.setBorder(Border.NO_BORDER);

        footerTbl.addCell(new Cell().add(contactNumberTbl).setBorder(Border.NO_BORDER));
        document.add(footerTbl);

        document.close();


    }

    @Override
    public void airlineEticket(TransactionDto transactionDto) throws IOException {

        //starter pack
        String pathPdf = "etiket-airline.pdf";

        String jakartaPlusDisplayPath = "fonts/PlusJakartaSans-Regular.ttf";
        String jakartaPDLightPath = "fonts/PlusJakartaDisplay-Light.otf";
        String jakartaPDBoldPath = "fonts/PlusJakartaDisplay-Bold.otf";

        PdfWriter pdfWriter = new PdfWriter(pathPdf);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document eTiketDoc = new Document(pdfDocument);
        PdfFont plusJakarta = PdfFontFactory.createFont(jakartaPlusDisplayPath);
        PdfFont jakartaPDLight = PdfFontFactory.createFont(jakartaPDLightPath);
        PdfFont jakartaPDBold = PdfFontFactory.createFont(jakartaPDBoldPath);

        //        Image
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image ImageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);

        ImageData timeImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/time_GTSbqaC0g_82z2L8w2D.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783948");
        Image timeImg = new Image(timeImgData).scaleAbsolute(44, 44);
        ImageData ticketHelpData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/voucher_Rmwi88eJd_h5rAd0OAg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225784204");
        Image ticketHelpImg = new Image(ticketHelpData).scaleAbsolute(44, 44);
        ImageData informationImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/information_9VP2VqxAc_wLTMswxFx.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783892");
        Image informationImg = new Image(informationImgData).scaleAbsolute(44, 44);

        ImageData flightImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/flight_r7fOpcE6h__1__l7cScd9U-.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663238775147");
        Image flightImg = new Image(flightImgData).scaleAbsolute(94, 94);

        ImageData startPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/start-point_9MPllA5Gg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578713");
        Image startPointImg = new Image(startPointData).scaleAbsolute(10, 40);
        ImageData endPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/end-point_iijnQ0Xsb.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578673");
        Image endPointImg = new Image(endPointData).scaleAbsolute(10, 40);

        ImageData citilinkData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/maskapai_logo/Citilink_UiJ89WzRH_1eajJGV_d.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663312097692");
        Image citilinkImg = new Image(citilinkData).scaleAbsolute(64, 64);

        //        Size Table Declarartion
        float threeCol = 190f;
        float col = 200f;
        float colHalf = 100f;
        float col150 = col + 150;
        float[] twoCol = {col150, col};
        float[] fullWidth = {threeCol * 3};
        //        Color
        Color colorTitle = new DeviceRgb(0, 188, 22);
        Color colorSubTitle = new DeviceRgb(123, 123, 123);

        //        Space
        Paragraph space = new Paragraph("\n");
        Table smallSpace = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        //        Border Line putus
        Table brokeLine = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});

        brokeLine.addCell(new Cell().add(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setFontSize(20).setTextAlignment(TextAlignment.CENTER));

        //        Line Header
        Border oneLineBd = new SolidBorder(Color.BLACK, 1f / 4f);
        Table oneLine = new Table(fullWidth);
        oneLine.setBorder(oneLineBd);


        //      --> HEADER START
        Table nestedBuktiTable = new Table(new float[]{col150});
        nestedBuktiTable.addCell(new Cell().add("E-ticket")
                .setBorder(Border.NO_BORDER)
                .setFontColor(colorTitle)
                .setFontSize(22)
                .setBold()
                .setFont(jakartaPDBold)
                .setPaddings(0, 0, 0, 0));
        nestedBuktiTable.addCell(new Cell().add("Penerbangan Pesawat")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 1)
                .setFontColor(colorSubTitle)
                .setFont(plusJakarta).setFontSize(12).setBold());

        Table headerTable = new Table(twoCol);
        headerTable.setMarginTop(-24);
        headerTable.addCell(new Cell().add(nestedBuktiTable).setBorder(Border.NO_BORDER));

//        Logo Holigo
        headerTable.addCell(new Cell().add(imageLogo).setPaddings(20, 0, 0, 44).setBorder(Border.NO_BORDER));

        eTiketDoc.add(headerTable);

//      --> ID TRANSAKSI START
        Table idTransaksiTbl = new Table(new float[]{colHalf, colHalf / 2});
        idTransaksiTbl.addCell(getHeaderTextCell("ID Transaksi", plusJakarta));
        idTransaksiTbl.addCell(new Cell().add(transactionDto.getInvoiceNumber())
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setFontColor(new DeviceRgb(32, 34, 33))
                .setMargins(-2, 0, -2, 0)
                .setPaddings(0, 8, -10, 8));
        idTransaksiTbl.setMarginBottom(8);

        eTiketDoc.add(idTransaksiTbl);
        eTiketDoc.add(oneLine);
        eTiketDoc.add(space);

//        Body / container
        Table containerTbl = new Table(new float[]{155, 10, 10, 255, 102});

        //        --HELP/Intruction--
        Table helpTbl = new Table(new float[]{45, 104});
        helpTbl.addCell(new Cell().add(ticketHelpImg).setBorder(Border.NO_BORDER));

        Paragraph ticketPrg = new Paragraph();
        ticketPrg.setFixedLeading(10);
        ticketPrg.add("Tunjukan identitas diri dan bukti pemesanan saat check-in");
        helpTbl.addCell(getInfo(ticketPrg, plusJakarta));

        helpTbl.addCell(new Cell().add(timeImg).setBorder(Border.NO_BORDER));
        Paragraph timePrg = new Paragraph();
        timePrg.add("Check-in sesuai dengan waktu yang tertera pada bukti pemesanan").setFixedLeading(10);
        helpTbl.addCell(getInfo(timePrg, plusJakarta));

        helpTbl.addCell(new Cell().add(informationImg).setBorder(Border.NO_BORDER));
        Paragraph infoPrg = new Paragraph();
        infoPrg.add("Waktu yang tertera sesuai dengan waktu hotel setempat").setFixedLeading(10);
        helpTbl.addCell(getInfo(infoPrg, plusJakarta));

        containerTbl.addCell(new Cell().add(helpTbl).setBorder(Border.NO_BORDER));

//        SPACING
        containerTbl.addCell(new Cell().add(" ")
                .setBorder(Border.NO_BORDER));
        containerTbl.addCell(new Cell().add(" ")
                .setBorderBottom(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));

        //        --DESTINATION--
        Table destinationChild = new Table(new float[]{75, 20, 140});
        Table dateTbl = new Table(new float[]{15});
        Table terminalTbl = new Table(new float[]{115});
        Table dateTblEnd = new Table(new float[]{15});
        Table terminalTblEnd = new Table(new float[]{115});


        //START Date frame (COL 1)
        //date
        dateTbl.addCell(getDestinationDate(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("departureDate").asText(), jakartaPDLight));
        //time
        dateTbl.addCell(getDestinationDate(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("departureTime").asText(), jakartaPDLight).setPaddingTop(-3));

        //START Terminal Frame (COL 3)
        // nama kota / city airport
        terminalTbl.addCell(getDestinationBandara(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("originAirport")
                .get("city").asText()
                + "(" +
                transactionDto.getDetail()
                        .get("trips").get(0)
                        .get("itineraries").get(0)
                        .get("originAirport")
                        .get("id").asText() + ")", plusJakarta));

        // bandara name /airport name
        terminalTbl.addCell(getTerminal(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("originAirport")
                .get("name").asText(), plusJakarta).setPaddingTop(-8));

        terminalTbl.addCell(getTerminal("Terminal 1", plusJakarta).setPaddingTop(-12));

        //END Date frame (COL 1)
        //date
        dateTblEnd.addCell(getDestinationDate(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("arrivalDate").asText(), jakartaPDLight));
        //time
        dateTblEnd.addCell(getDestinationDate(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("arrivalTime").asText(), jakartaPDLight).setPaddingTop(-3));

        //END Terminal Frame (COL 3)
        // end airport city
        terminalTblEnd.addCell(getDestinationBandara(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("destinationAirport")
                .get("city").asText()
                + "(" +
                transactionDto.getDetail()
                        .get("trips").get(0)
                        .get("itineraries").get(0)
                        .get("destinationAirport")
                        .get("id").asText() + ")", plusJakarta));

        // end airport name
        terminalTblEnd.addCell(getTerminal(transactionDto.getDetail()
                .get("trips").get(0)
                .get("itineraries").get(0)
                .get("destinationAirport")
                .get("name").asText(), plusJakarta).setPaddingTop(-8));
        terminalTblEnd.addCell(getTerminal("Terminal 1", plusJakarta).setPaddingTop(-12));

        int countDestination;
        // destinationloop
        for (countDestination = 1; countDestination <= 1; countDestination++) {
            //START Date frame (COL 1)
            destinationChild.addCell(new Cell().add(dateTbl).setBorder(Border.NO_BORDER));
            //START Point Destination (COL 2)
            destinationChild.addCell(new Cell().add(startPointImg)
                    .setBorder(Border.NO_BORDER)
                    .setPaddings(7, 0, 0, 5));

            //START Terminal Frame (COL 3)
            destinationChild.addCell(new Cell().add(terminalTbl).setBorder(Border.NO_BORDER));

            destinationChild.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

            //time flight
            destinationChild.addCell(new Cell().add(transactionDto.getDetail()
                            .get("trips").get(0)
                            .get("duration").asText())
                    .setBorder(Border.NO_BORDER)
                    .setFontSize(9)
                    .setFont(jakartaPDLight)
                    .setTextAlignment(TextAlignment.LEFT));
            destinationChild.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

            //END Point Destination (COL 2)
            destinationChild.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            destinationChild.addCell(new Cell().add(endPointImg)
                    .setBorder(Border.NO_BORDER)
                    .setPaddings(7, 0, 0, 5));
            destinationChild.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

            destinationChild.addCell(new Cell().add(dateTblEnd)
                    .setPaddings(-20, 0, 0, 0)
                    .setBorder(Border.NO_BORDER));
            //END Point Destination (COL 2)
            destinationChild.addCell(new Cell().add("")
                    .setBorder(Border.NO_BORDER));
            destinationChild.addCell(new Cell().add(terminalTblEnd)
                    .setPaddings(-20, 0, 0, 0)
                    .setBorder(Border.NO_BORDER));
        }

        Table destinationParent = new Table(new float[]{235});
        destinationParent.addCell(new Cell().add(destinationChild)
                .setBorder(Border.NO_BORDER)

        );
//        Space between destination and Kode Booking
        destinationParent.addCell(new Cell().add("")
                .setFont(plusJakarta)
                .setFontSize(20)
                .setMinHeight(30)
                .setBorder(Border.NO_BORDER)
                .setBold()
        );
        if (countDestination - 1 > 3) {
            destinationParent.addCell(new Cell().add("Kode Booking")
                    .setFont(plusJakarta)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(97, 97, 97))
                    .setPaddings(0, 0, 0, 10)
                    .setBorder(Border.NO_BORDER)
                    .setFixedPosition(1, 475, 425, 235)

            );
            //kode booking
            destinationParent.addCell(new Cell().add("VFYRSW")
                    .setFont(plusJakarta)
                    .setFontSize(20)
                    .setFontColor(new DeviceRgb(0, 189, 23))
                    .setPaddings(-10, 0, 0, 10)
                    .setBorder(Border.NO_BORDER)
                    .setFixedPosition(1, 465, 395, 235)
                    .setBold()
            );
        } else {
            destinationParent.addCell(new Cell().add("Kode Booking")
                    .setFont(plusJakarta)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(97, 97, 97))
                    .setPaddings(0, 0, 0, 10)
                    .setBorder(Border.NO_BORDER)
            );
            destinationParent.addCell(new Cell().add("VFYRSW")
                    .setFont(plusJakarta)
                    .setFontSize(20)
                    .setFontColor(new DeviceRgb(0, 189, 23))
                    .setPaddings(-10, 0, 0, 10)
                    .setBorder(Border.NO_BORDER)
                    .setBold()
            );
        }


        //        --FLIGHT INFO--
        containerTbl.addCell(new Cell().add(destinationParent).setBorder(Border.NO_BORDER));

        //  Image Maskapai
        String maskapaiValid = "Citilink";

        Table maskapai = new Table(new float[]{col});

        maskapai.addCell(new Cell().add(citilinkImg.setFixedPosition(1, 500, 630)).setBorder(Border.NO_BORDER));

        maskapai.addCell(getSpaceVer());
        maskapai.addCell(getSpaceVer());
        maskapai.addCell(getHelpText("Citilink", jakartaPDBold)
                .setPaddings(0, 0, 0, 0)
                .setTextAlignment(TextAlignment.RIGHT));


        maskapai.addCell(getHelpText("ID-6170", jakartaPDBold)
                .setPaddings(-5, 0, 0, 0)
                .setTextAlignment(TextAlignment.RIGHT)

        );
        maskapai.addCell(getHelpText("Subclass G", jakartaPDBold)
                .setPaddings(-5, 0, 0, 0)
                .setTextAlignment(TextAlignment.RIGHT)

        );
        maskapai.addCell(new Cell().add(space).setBorder(Border.NO_BORDER));
        maskapai.addCell(new Cell().add(space).setBorder(Border.NO_BORDER));

        //flight Image placement
        maskapai.addCell(new Cell().add(flightImg.setFixedPosition(1, 470, 470))
                .setBorder(Border.NO_BORDER)
                .setPaddings(0, 0, 0, 0));

        containerTbl.addCell(new Cell().add(maskapai).setBorder(Border.NO_BORDER));
        eTiketDoc.add(containerTbl);
        Table container2;
//        Detail Penumpang and positioning
        if (countDestination - 1 >= 2) {
            container2 = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()}).setFixedPosition(2, 24, 600, pdfDocument.getDefaultPageSize().getWidth());
        } else {
            container2 = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        }

//        container2.addCell(getSpaceVer());

        container2.addCell(getHeaderTextCell("Detail Penumpang", plusJakarta).setMinHeight(20));
        container2.addCell(new Cell()
                .add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 0)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setBold()
                .setMaxHeight(20)
        );

        Table detailPenumpang = new Table(new float[]{colHalf, col, col, col});
        detailPenumpang.setMargins(0, 0, 0, 10);

        int countPenumpang = transactionDto.getDetail().get("trips").get(0).get("segment").asInt();

        detailPenumpang.addCell(getHeaderTextCell("No.", plusJakarta));
        detailPenumpang.addCell(getHeaderTextCell("Nama Penumpang", plusJakarta));
        detailPenumpang.addCell(getHeaderTextCell("Nomor tiket", plusJakarta));
        detailPenumpang.addCell(getHeaderTextCell("Fasilitas", plusJakarta));

        for (int passanger = 1; countPenumpang >= passanger; passanger++){
            //no.
            detailPenumpang.addCell(getDetailPenumpangOutput(passanger+"", plusJakarta));
            //nama penumpang
            detailPenumpang.addCell(getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(0).get("passengers").get(0).get("passenger").get("name").asText(), plusJakarta));
            //nomer tiket
            detailPenumpang.addCell(getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(0).get("passengers").get(0).get("ticketNumber").asText(), plusJakarta));
            //fasilitas
            detailPenumpang.addCell(getDetailPenumpangOutput("Bagasi 20g", plusJakarta));
        }

        container2.addCell(new Cell().add(detailPenumpang).setBorder(Border.NO_BORDER));

        container2.addCell(getSpaceVer());
        container2.addCell(new Cell()
                .add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 0)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setBold()
                .setMaxHeight(20)
        );

        eTiketDoc.add(container2);

//        Syarat dan Ketentuan Maskapai
        Table syaratNketentuanTbl;

        if (countPenumpang > 4 || countDestination - 1 > 1) {
            syaratNketentuanTbl = new Table(fullWidth).setFixedPosition(2, 24, 95, threeCol * 3);
        } else {
            syaratNketentuanTbl = new Table(fullWidth);
        }
        syaratNketentuanTbl.addCell(new Cell().add("* Syarat dan ketentuan dari maskapai")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setBorder(Border.NO_BORDER));

        Text holigoLink = new Text("https://holigo.co.id/newspenerbangan");
        holigoLink.setBold();
        Paragraph ketentuanPrg = new Paragraph();
        ketentuanPrg.setFixedLeading(11);
        ketentuanPrg.add("• Untuk penerbangan domestik dan internasional, penumpang wajib memperhatikan dan melengkapi persyaratan perjalanan, seperti dokumen ");
        ketentuanPrg.add("kesehatan terkait COVID-19, ketentuan paspor & visa, kartu identitas, dan ketentuan transit/koneksi. Ketentuan regulasi tiap negara dapat berubah");
        ketentuanPrg.add("sewaktu-waktu tanpa pemberitahuan sebelumnya. Penumpang sepenuhnya bertanggung jawab terhadap pemenuhan kelengkapan persyaratan ");
        ketentuanPrg.add("perjalanan yang dibutuhkan pada saat keberangkatan dan/atau saat kedatangan. Untuk informasi lebih lanjut, silakan cek\n");
        ketentuanPrg.add(holigoLink);
        ketentuanPrg.add("\n");
        ketentuanPrg.add("• Penumpang diharapkan tiba di terminal keberangkatan selambat-lambatnya 4 (empat) jam sebelum waktu keberangkatan pada penerbangan domestik dan internasional untuk melakukan check-in.\n");
        ketentuanPrg.add("• Check-in ditutup 60 menit sebelum waktu keberangkatan.\n");
        ketentuanPrg.add("• Penumpang diharapkan tiba di gerbang keberangkatan (Boarding Gate) 45 menit sebelum waktu keberangkatan.\n");
        ketentuanPrg.add("• Penumpang wajib membawa KTP atau Kartu Keluarga (hard/soft copy untuk penumpang anak dan bayi).\n");


        syaratNketentuanTbl.addCell(getSyaratKetentuan(ketentuanPrg, plusJakarta)
                .setPaddings(0, 0, 0, 10)
                .setBorder(Border.NO_BORDER));

        eTiketDoc.add(syaratNketentuanTbl);


//      Left Footer
        float defaultSizeWidth = pdfDocument.getDefaultPageSize().getWidth();
        Table footerTbl = new Table(new float[]{defaultSizeWidth / 2, 100});
//        Position of footer
        footerTbl.setFixedPosition(pdfDocument.getNumberOfPages(), 24, 14, 564);
        footerTbl.addCell(new Cell().add("PT. Aplikasi Liburan Indonesia")
                .setBold()
                .setFontSize(8)
                .setBorder(Border.NO_BORDER)
                .setFont(plusJakarta));
        footerTbl.addCell(new Cell().add("Contact Holi Care")
                .setBold()
                .setFontSize(8)
                .setBorder(Border.NO_BORDER)
                .setFont(plusJakarta));
        // detail text
        footerTbl.addCell(new Cell().add("Head Office")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER)
        );
//        Nested table for email
        Table emailHoligo = new Table(new float[]{10, 50});
        emailHoligo.addCell(new Cell().add(ImageMail)
                .setPaddings(5f, 0, 0, 0)
                .setBorder(Border.NO_BORDER)
        );
        emailHoligo.addCell(new Cell().add("cs@holigo.co.id")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setPaddings(0, 0, 0, 5)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER)
        );
        footerTbl.addCell(new Cell().add(emailHoligo)
                .setBorder(Border.NO_BORDER)
        );
        footerTbl.addCell(new Cell().add("Jl. Bukit Golf 1 P17 Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15310 Indonesia")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setPaddings(-7, 0, 0, 2)
                .setBorder(Border.NO_BORDER));

//        Right Footer
        Table contactNumberTbl = new Table(new float[]{10, 50});
        contactNumberTbl.addCell(new Cell().add(phoneImg).setBorder(Border.NO_BORDER)
                .setPaddings(-1, 0, 0, 0));
        contactNumberTbl.addCell(new Cell().add("+6281388882386")
                .setFont(plusJakarta)
                .setFontSize(7)
                .setPaddings(-4, 0, 0, 5)
                .setFontColor(new DeviceRgb(71, 71, 71))
                .setBorder(Border.NO_BORDER));

        contactNumberTbl.setBorder(Border.NO_BORDER);
        footerTbl.addCell(new Cell().add(contactNumberTbl).setBorder(Border.NO_BORDER));

        eTiketDoc.add(footerTbl);

        eTiketDoc.close();
    }

    // style
    public static Cell getHeaderTextCell(String textVelue, PdfFont plusJakarta) {
        return new Cell().add(textVelue)
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setFont(plusJakarta)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(new DeviceRgb(32, 34, 33));
    }

    public static Cell getTextDetail(String textVelue, PdfFont plusJakarta) {
        return new Cell().add(textVelue)
                .setBorder(Border.NO_BORDER)
                .setFontSize(9)
                .setFont(plusJakarta)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public static Cell getDetailUserBold(String textVelue, PdfFont plusJakarta) {
        return new Cell().add(textVelue)
                .setBorder(Border.NO_BORDER)
                .setFontSize(9)
                .setBold()
//                .setHeight(15)
                .setFont(plusJakarta)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setPaddings(0, 0, 0, 7)
                .setFontColor(new DeviceRgb(32, 34, 33));
    }

    public static Cell getDetailProdukOutput(String text, PdfFont plusJakarta) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.LEFT)
                .setFont(plusJakarta)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    private String getPrice(double price) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(otherSymbols);
        return df.format(price);
    }

    public static Cell getHelpText(String text, PdfFont plusJarkarta) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.LEFT)
                .setFont(plusJarkarta)
                .setFontColor(new DeviceRgb(123, 123, 123));
    }

    public static Cell getDestinationDate(String text, PdfFont font) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(10)
                .setBold()
                .setFont(font)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(Color.BLACK);
    }

    public static Cell getDestinationBandara(String text, PdfFont font) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(10)
                .setBold()
                .setFont(font)
//                .setCharacterSpacing(1)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(Color.BLACK);
    }

    public static Cell getTerminal(String text, PdfFont plusJarkarta) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.LEFT)
                .setFont(plusJarkarta)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public static Cell getSpaceVer() {
        return new Cell().add("")
                .setMinHeight(20)
                .setBorder(Border.NO_BORDER);
    }

    public static Cell getDetailPenumpangOutput(String text, PdfFont font) {
        return new Cell().add(text)
                .setFontSize(11)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setFont(font)
                .setBorder(Border.NO_BORDER);
    }

    public static Cell getSyaratKetentuan(Paragraph text, PdfFont font) {
        return new Cell().add(text)
                .setFont(font)
                .setFontSize(8)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public static Cell getInfo(Paragraph text, PdfFont font) {
        return new Cell().add(text)
                .setFont(font)
                .setFontSize(8)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setPaddings(8, 0, 0, 5)
                .setBorder(Border.NO_BORDER);
    }


}
