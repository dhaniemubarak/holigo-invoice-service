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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdf;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfHotelEreceiptImpl implements PdfHotelEreceipt {
    @Override
    public void eReceiptHotel(TransactionDto transactionDto) throws IOException {

        PdfWriter pdfWriter = new PdfWriter("hotel-eReceipt.pdf");

        StylePdf stylePdf = new StylePdf();


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

        float col = 200f;
        float colHalf = 100f;
        float col350 = 350f;
        float[] twoCol = {350f, 200f};

        // - - - - - HEADER  - - - - -
        Table nestedBuktiTable = new Table(new float[]{col350});
        nestedBuktiTable.addCell(new Cell().add("Bukti Pembayaran")
                .setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(0, 188, 22))
                .setFontSize(22).setBold().setFont(plusJakarta)
                .setPaddings(0, 0, 0, 0));
        nestedBuktiTable.addCell(new Cell().add("Hotel")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 1)
                .setFontColor(new DeviceRgb(123, 123, 123))
                .setFont(plusJakarta).setFontSize(12).setBold());

        Table headerTable = new Table(twoCol);
        headerTable.setMarginTop(-24);
        headerTable.addCell(new Cell().add(nestedBuktiTable).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(imageLogo).setPaddings(20, 0, 0, 44).setBorder(Border.NO_BORDER));
        document.add(headerTable);

        //--> ID TRANSAKSI
        Table idTransaksiTbl = new Table(new float[]{colHalf, colHalf / 2});
        idTransaksiTbl.addCell(stylePdf.getHeaderTextCell("ID Transaksi", plusJakarta));
        idTransaksiTbl.addCell(new Cell().add(transactionDto.getInvoiceNumber().toString() + "")
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
        document.add(stylePdf.oneLine(pdfDocument));

        // - - - - - Body Part  - - - - -

        //        Detail Pemesanan buyer
        Table detailPemesananHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        detailPemesananHead.addCell(stylePdf.getHeaderTextCell("Detail Pemesanan", plusJakarta));
        Table tblDetailPemesanan = new Table(new float[]{col, 30, col, 30, col});
        tblDetailPemesanan.setMarginLeft(8);
        tblDetailPemesanan.addCell(stylePdf.getTextDetail("Nama Lengkap", plusJakarta));
        tblDetailPemesanan.addCell(stylePdf.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdf.getTextDetail("Email", plusJakarta));
        tblDetailPemesanan.addCell(stylePdf.getTextDetail("", plusJakarta));
        tblDetailPemesanan.addCell(stylePdf.getTextDetail("Nomor Ponsel", plusJakarta));



        tblDetailPemesanan.addCell(stylePdf.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("name").asText(), plusJakartaDisplayBold));

        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        tblDetailPemesanan.addCell(stylePdf.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("email").asText(), plusJakartaDisplayBold));

        tblDetailPemesanan.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

        tblDetailPemesanan.addCell(stylePdf.getDetailUserBold(
                transactionDto.getDetail()
                        .get("contactPerson")
                        .get("phoneNumber").asText(), plusJakartaDisplayBold));


        //      Detail Pembayaran Title
        Table detailPembayaranHead = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        detailPembayaranHead.addCell(stylePdf.getHeaderTextCell("Detail Pembayaran", plusJakarta));

        Table detailPembayaran = new Table(new float[]{150, 10, 60, 20, col});
        detailPembayaran.setMarginLeft(8);
        detailPembayaran.addCell(stylePdf.getTextDetail("waktu pembayaran", plusJakarta));
        detailPembayaran.addCell(stylePdf.getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(stylePdf.getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(stylePdf.getTextDetail(" ", plusJakarta));
        detailPembayaran.addCell(stylePdf.getTextDetail("Metode pembayaran", plusJakarta));
        document.add(stylePdf.smallSpace(pdfDocument));
        document.add(detailPemesananHead);
        document.add(tblDetailPemesanan);
        document.add(stylePdf.space(pdfDocument));

        //        Detail Pembayaran output
        detailPembayaran.addCell(stylePdf.getDetailUserBold("Senin, 12 Februari 2021", plusJakartaDisplayBold));
        detailPembayaran.addCell(stylePdf.getTextDetail(" ", plusJakartaDisplayBold));
        detailPembayaran.addCell(stylePdf.getDetailUserBold("13:5 WIB", plusJakartaDisplayBold)
                .setTextAlignment(TextAlignment.CENTER)
                .setPaddings(0, 0, 0, 0));
        detailPembayaran.addCell(stylePdf.getTextDetail(" ", plusJakartaDisplayBold));
        detailPembayaran.addCell(stylePdf.getDetailUserBold("Mandiri - Virtual Account", plusJakartaDisplayBold));

        document.add(detailPembayaranHead);
        document.add(detailPembayaran);
        document.add(stylePdf.smallSpace(pdfDocument));
        document.add(stylePdf.brokeLine(pdfDocument));


//        Detail Produk title
        Table detailProdukTbl = new Table(new float[]{colHalf, colHalf, col + colHalf, col, col});
        detailProdukTbl.setMarginLeft(20);
        detailProdukTbl.addCell(stylePdf.getHeaderTextCell("No.", plusJakarta));
        detailProdukTbl.addCell(stylePdf.getHeaderTextCell("Produk", plusJakarta));
        detailProdukTbl.addCell(stylePdf.getHeaderTextCell("Deskripsi", plusJakarta));
        detailProdukTbl.addCell(stylePdf.getHeaderTextCell("Jumlah", plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(stylePdf.getHeaderTextCell("Harga", plusJakarta));

//        Detail Produk output
        Table deskripsiProduk = new Table(new float[]{150f});
        //nama hotel
        String []  namaHotel = transactionDto.getDetail().get("hotel").get("name").asText().split(" ");
        deskripsiProduk.addCell(stylePdf.getDetailProdukOutput(namaHotel[0]+" "+namaHotel[1]+" "+namaHotel[2], plusJakarta));
        //nama room
        deskripsiProduk.addCell(stylePdf.getDetailProdukOutput(transactionDto.getDetail().get("room").get("name").asText(), plusJakarta).setFontSize(8).setPaddingTop(-6));

        double adminAmount;
        double discount;
        double fareAmount;


        int count = 1;
        detailProdukTbl.addCell(stylePdf.getDetailProdukOutput("" + count, plusJakarta));
        detailProdukTbl.addCell(stylePdf.getDetailProdukOutput("Hotel", plusJakarta));
        detailProdukTbl.addCell(new Cell().add(deskripsiProduk).setBorder(Border.NO_BORDER));
        detailProdukTbl.addCell(stylePdf.getDetailProdukOutput("1", plusJakarta).setTextAlignment(TextAlignment.CENTER));
        detailProdukTbl.addCell(stylePdf.getDetailProdukOutput("Rp " + stylePdf.getPrice(transactionDto.getFareAmount().floatValue()) + ",-", plusJakarta));



        String fareAmountStr = transactionDto.getFareAmount().toString();
        fareAmount = Float.parseFloat(fareAmountStr);

        // pricing on produk
        if (transactionDto.getDetail().get("adminAmount") != null){
            String adminAmountStr = transactionDto.getDetail().get("adminAmount").asText();
            adminAmount = Float.parseFloat(adminAmountStr);

        }else {
            String adminAmountStr = "0";
            adminAmount = Float.parseFloat(adminAmountStr);
        }

        if (transactionDto.getDetail().get("discountAmount") != null){
            String discountStr = transactionDto.getDetail().get("discountAmount").asText();
            discount = Float.parseFloat(discountStr);
        }{
            String discountStr = "0";
            discount = Float.parseFloat(discountStr);
        }


        document.add(detailProdukTbl);
        document.add(stylePdf.brokeLine(pdfDocument));


//        Bill / Price / harga
        Table priceTable = new Table(twoCol);
        Table nestedPrice = new Table(new float[]{col / 2, col - 50});
        nestedPrice.addCell(stylePdf.getHeaderTextCell("Sub Total", plusJakarta));

        nestedPrice.addCell(stylePdf.getDetailProdukOutput("Rp " + stylePdf.getPrice(fareAmount) + ",-", plusJakarta));
//        nestedPrice.addCell(stylePdf.getHeaderTextCell("Biaya Jasa", plusJakarta));
//        nestedPrice.addCell(stylePdf.getDetailProdukOutput("Rp " + stylePdf.getPrice(adminAmount) + ",-", plusJakarta));
        nestedPrice.addCell(stylePdf.getHeaderTextCell("Discount", plusJakarta));
        nestedPrice.addCell(stylePdf.getDetailProdukOutput("Rp -" + stylePdf.getPrice(discount) + ",-", plusJakarta));

        double finalPrice = fareAmount + adminAmount - discount;
        nestedPrice.addCell(stylePdf.getDetailProdukOutput("- - - - - - - - - ", plusJakarta).setBold());
        nestedPrice.addCell(stylePdf.getDetailProdukOutput(" - - - - - - - - - - - - - - - - -", plusJakarta).setPaddingLeft(-25f).setBold());
        nestedPrice.addCell(stylePdf.getHeaderTextCell("Total", plusJakarta));
        nestedPrice.addCell(new Cell().add("Rp " + stylePdf.getPrice(finalPrice) + ",-")
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

        //footer
        //      Left Footer
        float defaultSizeWidth = pdfDocument.getDefaultPageSize().getWidth();
        Table footerTbl = new Table(new float[]{defaultSizeWidth / 2, 100});
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

        //- - - - - -closer- - - - - -
        pdfDocument.close();
        document.close();
    }
}
