package id.holigo.services.holigoinvoiceservice.services.style;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class StylePdfService {
    public Paragraph space(PdfDocument pdfDocument) {
        return new Paragraph("\n");
    }

    public Table oneLine(PdfDocument pdfDocument) {
        //        Line Header
        Border oneLineBd = new SolidBorder(Color.BLACK, 1f / 4f);
        Table oneLine = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        oneLine.setBorder(oneLineBd);

        return oneLine;
    }

    public Table smallSpace(PdfDocument pdfDocument) {
        Table smallSpace = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        smallSpace.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        return smallSpace;
    }

    public Table brokeLine(PdfDocument pdfDocument) {
        //        Border Line putus
        Table brokeLine = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        brokeLine.addCell(new Cell().add(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setFontSize(20).setTextAlignment(TextAlignment.CENTER));

        return brokeLine;

    }

    public Cell getHeaderTextCell(String textVelue, PdfFont plusJakarta) {
        return new Cell().add(textVelue)
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setFont(plusJakarta)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(new DeviceRgb(32, 34, 33));
    }


    public Cell getTextDetail(String textVelue, PdfFont plusJakarta) {
        return new Cell().add(textVelue)
                .setBorder(Border.NO_BORDER)
                .setFontSize(9)
                .setFont(plusJakarta)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public Cell getDetailUserBold(String textVelue, PdfFont plusJakarta) {
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

    public Cell getDetailProdukOutput(String text, PdfFont plusJakarta) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.LEFT)
                .setFont(plusJakarta)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public Paragraph spaceInColumn() {
        return new Paragraph("\n");
    }

    public Cell getInfo(Paragraph text, PdfFont font) {
        return new Cell().add(text)
                .setFont(font)
                .setFontSize(8)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setPaddings(8, 0, 0, 5)
                .setBorder(Border.NO_BORDER);
    }

    public Cell logoPositionHtl(Image image) {
        return new Cell().add(image)
                .setRelativePosition(5, 2, 0, 0)
                .setBorder(Border.NO_BORDER);
    }

    public String getPrice(double price) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(otherSymbols);
        return df.format(price);
    }


    public Cell smallSpaceInColumn() {
        return new Cell().add("")
                .setBorder(Border.NO_BORDER)
                .setHeight(3);
    }

    public Paragraph getSpacePara(float height) {
        Paragraph spaceMaxWidth = new Paragraph();
        spaceMaxWidth.add(" ");
        spaceMaxWidth.setHeight(height);
        return spaceMaxWidth;
    }

    public Table brokeLineEvoucher(PdfDocument pdfDocument) {
        //        Border Line putus
        Table brokeLine = new Table(new float[]{pdfDocument.getDefaultPageSize().getWidth()});
        brokeLine.addCell(new Cell().add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(199, 199, 199))
//                .setFixedPosition(1,27,386,pdfDocument.getDefaultPageSize().getWidth())
                .setFontSize(20).setTextAlignment(TextAlignment.CENTER)
                .setPaddings(-10, -10, -10, -10));
        return brokeLine;
    }

    public Cell getCheckTime(String text, PdfFont font) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(text);
        paragraph.setFixedLeading(10);
        return new Cell().add(paragraph)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(font)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setBorder(Border.NO_BORDER);
    }

    public Cell getHotelBold12(Paragraph text, PdfFont font) {
        return new Cell().add(text)
                .setBold()
                .setFont(font)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(12)
                .setPaddings(-4, 0, -4, 0);
    }

    public Cell getDetailPemesananOutput(String text, PdfFont font) {
        return new Cell().add(text)
//                .setPaddings(0,0,0,10)
                .setRelativePosition(10, 1, 0, 0)
                .setFontColor(new DeviceRgb(71, 71, 71)).setFontSize(10)
                .setBorder(Border.NO_BORDER)
                .setFont(font);
    }

    public Cell getDetailPemesananAtribut(String text, PdfFont font) {
        return new Cell().add(text)
                .setRelativePosition(0, 0, 0, 0)
                .setFontSize(10)
                .setBorder(Border.NO_BORDER)
                .setBold()
                .setFont(font);
    }

    public Cell getSyaratKetentuan(String text, PdfFont font) {

        return new Cell().add(text)
                .setPaddings(0, 0, 0, 10)
//                .setPaddings(-3,0,-3,10)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setFontSize(8)
                .setBorder(Border.NO_BORDER)
                .setFont(font);
    }


    public Cell getCheck(String text, PdfFont pdfFont) {
        return new Cell().add(text)
                .setBorder(Border.NO_BORDER)
                .setFont(pdfFont)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(9)
//                .setPaddings(-4, 0, -4, 0)
                .setFontColor(new DeviceRgb(97, 97, 97));
    }

    public Cell outputHotelBody(String text, PdfFont font) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(text);
        paragraph.setFixedLeading(10);
        return new Cell().add(paragraph)
                .setFontSize(8)
                .setFont(font)
                .setRelativePosition(0, 3, 0, 0)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setBorder(Border.NO_BORDER);
    }


    public Table headerTitle(PdfFont plusJakarta, Image imageLogo, String title, String subTitle) {

        Table nestedBuktiTable = new Table(new float[]{350f});
        nestedBuktiTable.addCell(new Cell().add(title)
                .setBorder(Border.NO_BORDER)
                        .setFontColor(new DeviceRgb(0, 188, 22))
                        .setFontSize(22).setBold().setFont(plusJakarta)
                        .setRelativePosition(0, 5, 0, 0)
                        .setPaddings(0, 0, 0, 0)
        );
        nestedBuktiTable.addCell(new Cell().add(subTitle)
                .setBorder(Border.NO_BORDER)
//                .setPaddings(-10, 0, 0, 1)
                .setRelativePosition(0, 0, 1, 3)
                .setFontColor(new DeviceRgb(123, 123, 123))
                .setFont(plusJakarta).setFontSize(12).setBold());
        Table headerTable = new Table(new float[]{350f, 200f});
        headerTable.setMarginTop(-24);
        headerTable.addCell(new Cell().add(nestedBuktiTable).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(imageLogo)
                        .setRelativePosition(20,0,0,0)
//                        .setPaddings(20, 0, 0, 44)
                .setBorder(Border.NO_BORDER)
        );

        return headerTable;
    }

    public Table transaksiId(PdfFont plusJakarta, TransactionDto transactionDto) {
        Table idTransaksiTbl = new Table(new float[]{100f, 50f});
        idTransaksiTbl.addCell(getHeaderTextCell("ID Transaksi", plusJakarta));
        idTransaksiTbl.addCell(new Cell().add(transactionDto.getInvoiceNumber().toString())
                .setBorder(Border.NO_BORDER)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(plusJakarta)
                .setBackgroundColor(new DeviceRgb(209, 244, 206))
                .setFontColor(new DeviceRgb(32, 34, 33))
                .setMargins(-2, 0, -2, 0)
                .setPaddingBottom(-10));
        idTransaksiTbl.setMarginBottom(8);
        return idTransaksiTbl;
    }

    public Table footer(PdfFont plusJakarta, PdfDocument pdfDocument, Image imageMail, Image phoneImg) {
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
        emailHoligo.addCell(new Cell().add(imageMail)
                        .setRelativePosition(0, 1, 0, 0)
//                .setPaddings(5f, 0, 0, 0)
                        .setBorder(Border.NO_BORDER)
        );
        emailHoligo.addCell(new Cell().add("cs@holigo.co.id")
                        .setFont(plusJakarta)
                        .setFontSize(7)
//                .setPaddings(0, 0, 0, 5)
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
                        .setRelativePosition(0, 0, 0, 10)
//                .setPaddings(-7, 0, 0, 2)
                        .setBorder(Border.NO_BORDER)
        );

//        Right Footer
        Table contactNumberTbl = new Table(new float[]{10, 50});
        contactNumberTbl.addCell(new Cell().add(phoneImg)
                        .setBorder(Border.NO_BORDER)
                        .setRelativePosition(0, 1, 0, 0)
//                .setPaddings(-1, 0, 0, 0)
        );
        contactNumberTbl.addCell(new Cell().add("+6281388882386")
                        .setFont(plusJakarta)
                        .setFontSize(7)
//                .setPaddings(-4, 0, 0, 5)
                        .setFontColor(new DeviceRgb(71, 71, 71))
                        .setBorder(Border.NO_BORDER)
        );

        contactNumberTbl.setBorder(Border.NO_BORDER);

        footerTbl.addCell(new Cell().add(contactNumberTbl).setBorder(Border.NO_BORDER));

        return footerTbl;
    }


}
