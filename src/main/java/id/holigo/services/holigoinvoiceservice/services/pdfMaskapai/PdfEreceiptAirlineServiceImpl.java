package id.holigo.services.holigoinvoiceservice.services.pdfMaskapai;

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
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.services.style.RoundedBorderCellRenderer;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.*;
import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class PdfEreceiptAirlineServiceImpl extends HttpServlet implements PdfEreceiptAirlineService {


    @Autowired
    private final MessageSource messageSource;

    @Override
    public void airlineEreceipt(TransactionDto transactionDto, HttpServletResponse response) throws IOException {

//        PdfWriter pdfWriter = new PdfWriter("airline-invoice.pdf");

        StylePdfService stylePdfService = new StylePdfService();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));

        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
//        PdfFont plusJakarta = PdfFontFactory.createFont("src/main/resources/static/fonts/PlusJakartaSans-Regular.ttf");
        PdfFont plusJakarta = PdfFontFactory.createFont();
//        PdfFont plusJakartaDisplayBold = PdfFontFactory.createFont("src/main/resources/static/fonts/PlusJakartaDisplay-Bold.otf");
        PdfFont plusJakartaDisplayBold = PdfFontFactory.createFont();
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

//        //      --> HEADER START
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, "Bukti Pembayaran", "Penerbangan Pesawat"));

//      --> ID TRANSAKSI START
//        String transactionId = messageSource.getMessage("invoice.id-transaksi",null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.transaksiId("ID Transaksi", plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));


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

        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("name").asText(), plusJakartaDisplayBold));
        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("email").asText(), plusJakartaDisplayBold));
        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(stylePdfService.getDetailUserBold(transactionDto.getDetail().get("contactPerson").get("phoneNumber").asText(), plusJakartaDisplayBold));


//        Header Detail Pembayaran
        try {
            Table detailPembayaranHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
            Table detailPembayaran = new Table(new float[]{150, 10, 60, 20, col});

            String detailiPembayaran = messageSource.getMessage("invoice.generic-method-payment", null, LocaleContextHolder.getLocale());
            detailPembayaranHead.addCell(getHeaderTextCell(detailiPembayaran, plusJakarta));
            detailPembayaran.setMarginLeft(8);
            String waktuPembayaran = messageSource.getMessage("invoice.generic-date-payment", null, LocaleContextHolder.getLocale());
            detailPembayaran.addCell(getTextDetail(waktuPembayaran, plusJakarta));
            detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
            detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
            detailPembayaran.addCell(getTextDetail(" ", plusJakarta));
            String metodePembayaran = messageSource.getMessage("invoice.generic-method-payment", null, LocaleContextHolder.getLocale());
            detailPembayaran.addCell(getTextDetail(metodePembayaran, plusJakarta));
            document.add(smallSpace);
            document.add(detailPemesananHead);
            document.add(tblDetailPemesanan);
            document.add(space);

//        Detail Pembayaran user
            //     payment
            DateFormat inputDatePayment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            DateFormat outputDatePayment = new SimpleDateFormat("EE, dd MMM yyyy");
            DateFormat outputTimePayment = new SimpleDateFormat("HH:mm");

            Date datePayment = inputDatePayment.parse(transactionDto.getPayment().getUpdatedAt().toString());
            detailPembayaran.addCell(stylePdfService.getDetailUserBold(outputDatePayment.format(datePayment), plusJakartaDisplayBold)); // tanggal
            detailPembayaran.addCell(getTextDetail(" ", plusJakartaDisplayBold));

            Date paymentTime = inputDatePayment.parse(transactionDto.getPayment().getUpdatedAt().toString());
            detailPembayaran.addCell(stylePdfService.getDetailUserBold(outputTimePayment.format(paymentTime) + " WIB", plusJakartaDisplayBold).setTextAlignment(TextAlignment.CENTER) // time
                    .setPaddings(0, 0, 0, 0));


            detailPembayaran.addCell(getTextDetail(" ", plusJakartaDisplayBold));
//        Payment method
            String paymentMetode = transactionDto.getPayment().getPaymentService().getName();
            detailPembayaran.addCell(stylePdfService.getDetailUserBold(paymentMetode, plusJakartaDisplayBold)); // metode


            document.add(detailPembayaranHead);
            document.add(detailPembayaran);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        document.add(smallSpace);
        document.add(brokeLine);

//        Detail Produk Head
        Table detailProdukTbl = new Table(new float[]{colHalf, col, col, col, col});
        detailProdukTbl.setMarginLeft(20);
        detailProdukTbl.addCell(getHeaderTextCell("No.", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Produk", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Deskripsi", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Jumlah", plusJakarta));
        detailProdukTbl.addCell(getHeaderTextCell("Harga", plusJakarta));

//        Detail Produk
        double adminAmount;
        double discount;
        Table deskripsiProduk = new Table(new float[]{100f});
        int sizeTrips = transactionDto.getDetail().get("trips").size();


        // pricing

        String adminAmountStr = transactionDto.getDetail().get("adminAmount").asText();
        adminAmount = Float.parseFloat(adminAmountStr);
        String discountStr = transactionDto.getDetail().get("discountAmount").asText();
        discount = Float.parseFloat(discountStr);

        int count = 1;
        for (int index = 0; index < sizeTrips; index++) {
//            detailProdukTbl = new Table(new float[]{colHalf, col, col, col, col});

            String airlanesName = transactionDto.getDetail().get("trips").get(index).get("itineraries").get(0).get("airlinesName").asText();
            String originAirport = transactionDto.getDetail().get("trips").get(index).get("itineraries").get(0).get("originAirport").get("id").asText();
            String destinationAirport = transactionDto.getDetail().get("trips").get(index).get("itineraries").get(0).get("destinationAirport").get("id").asText();
            String kodeBooking = transactionDto.getDetail().get("trips").get(index).get("itineraries").get(0).get("pnr").asText();


            detailProdukTbl.addCell(getDetailProdukOutput(Integer.toString(count), plusJakarta));
            //judul/title
            detailProdukTbl.addCell(getDetailProdukOutput("Tiket pesawat", plusJakarta));

            //deskripsi
            deskripsiProduk.addCell(getDetailProdukOutput(airlanesName, plusJakarta));
            deskripsiProduk.addCell(getDetailProdukOutput(originAirport + "-" + destinationAirport, plusJakarta).setFontSize(8).setPaddingTop(-6));
            deskripsiProduk.addCell(getDetailProdukOutput("Kode Booking : " + kodeBooking, plusJakarta).setFontSize(8).setPaddingTop(-6));
            detailProdukTbl.addCell(new Cell().add(deskripsiProduk).setBorder(Border.NO_BORDER));
            deskripsiProduk = new Table(new float[]{100f});
            //jumlah
            int sizePassager = transactionDto.getDetail().get("trips").get(index).get("passengers").size();
            detailProdukTbl.addCell(getDetailProdukOutput(String.valueOf(sizePassager), plusJakarta));
            String fareAmountStr = transactionDto.getDetail().get("trips").get(index).get("fareAmount").asText();
            double fareAmount = Float.parseFloat(fareAmountStr);
            detailProdukTbl.addCell(getDetailProdukOutput("Rp " + fareAmount + ",-", plusJakarta));

            count = count + 1;
        }
        String fareAmountStr = transactionDto.getFareAmount().toString();
        double fareAmount = Float.parseFloat(fareAmountStr);
        double finalPrice = fareAmount;
        double billAmount = fareAmount - adminAmount + discount;

        document.add(detailProdukTbl);
        document.add(brokeLine);
//        Bill / Price / harga

        Table priceTable = new Table(twoCol);
        Table nestedPrice = new Table(new float[]{col / 2, col - 50});
        nestedPrice.addCell(getHeaderTextCell("Sub Total", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(billAmount) + ",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Biaya Jasa", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(adminAmount) + ",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Discount", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp " + getPrice(discount) + ",-", plusJakarta));

        nestedPrice.addCell(getDetailProdukOutput("- - - - - - - - - ", plusJakarta).setBold());
        nestedPrice.addCell(getDetailProdukOutput("  - - - - - - - - - - - - - - - - -", plusJakarta).setPaddingLeft(-23f).setBold());
        nestedPrice.addCell(getHeaderTextCell("Total", plusJakarta));
        Cell totalCell = new Cell().add("Rp " + stylePdfService.getPrice(finalPrice) + ",-")
                .setPaddings(0, 5, 0, 5)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setFontSize(12)
                .setBorder(Border.NO_BORDER)
                .setFontColor(Color.BLACK);
        totalCell.setNextRenderer(new RoundedBorderCellRenderer(totalCell, true));
        nestedPrice.addCell(totalCell);

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

        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
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
                .setFontSize(9)
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
                .setMinHeight(10)
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
