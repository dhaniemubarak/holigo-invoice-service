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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@RequiredArgsConstructor
@Service
public class PdfAirlineServiceImpl implements PdfAirlineService {

    public void airline(TransactionDto transactionDto) throws IOException {

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
            // __Test Column Rounded
//        Table nameCustomer = new Table(new float[] {col});
//        nameCustomer.addCell(getDetailUser(transactionDto.getDetail().get("contactPerson").get("name").asText(),plusJakarta));
//        nameCustomer.setBorder(Border.NO_BORDER);
//        nameCustomer.setNextRenderer(new TableBorderRenderer(nameCustomer));
//        tblDetailPemesanan.addCell(new Cell().add(nameCustomer).setBorder(Border.NO_BORDER));

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
            detailProdukTbl.addCell(getDetailProdukOutput("Rp "+getPrice(transactionDto.getFareAmount().floatValue())+",-", plusJakarta));

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
        Table nestedPrice = new Table(new float[]{col / 2, col-50});
        nestedPrice.addCell(getHeaderTextCell("Sub Total", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp "+getPrice(fareAmount)+",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Biaya Jasa", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp "+getPrice(adminAmount)+",-", plusJakarta));
        nestedPrice.addCell(getHeaderTextCell("Discount", plusJakarta));
        nestedPrice.addCell(getDetailProdukOutput("Rp "+getPrice(discount)+",-", plusJakarta));

        double finalPrice = fareAmount + adminAmount - discount;
        nestedPrice.addCell(getDetailProdukOutput("- - - - - - - - - ", plusJakarta).setBold());
        nestedPrice.addCell(getDetailProdukOutput("  - - - - - - - - - - - - - - - - -", plusJakarta).setPaddingLeft(-23f).setBold());
        nestedPrice.addCell(getHeaderTextCell("Total", plusJakarta));
        nestedPrice.addCell(new Cell().add("Rp "+getPrice(finalPrice)+",-")
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
}
