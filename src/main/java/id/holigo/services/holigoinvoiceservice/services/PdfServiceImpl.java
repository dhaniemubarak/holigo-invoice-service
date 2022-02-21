package id.holigo.services.holigoinvoiceservice.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

import org.springframework.stereotype.Service;

@Service
public class PdfServiceImpl implements PdfService {

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);

        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);


        // Holigo image
        Image holigo = Image.getInstance("https://sandbox.holigo.id/images/invoice-holigo.png");
        holigo.setAbsolutePosition(50, 750);
        holigo.scaleAbsolute(40, 40);
        cb.addImage(holigo);

        // Holigo Text
        Font holigoFont = new Font(bf, 18, Font.BOLD, new Color(0, 189, 23, 1));
        Chunk holigoText = new Chunk("Holigo");
        holigoText.setFont(holigoFont);
        ColumnText holigoCt = new ColumnText(cb);
        holigoCt.setSimpleColumn(102, 765, 200, 805, 40, Element.ALIGN_LEFT);
        holigoCt.addText(holigoText);
        holigoCt.go();

        // Service title text
        Font titleFont = new Font(bf, 12, Font.NORMAL, new Color(0, 0, 0, 1));
        Chunk titleText = new Chunk("Invoice Paket Data");
        titleText.setFont(titleFont);
        ColumnText titleCt = new ColumnText(cb);
        titleCt.setSimpleColumn(50, 720, 335, 744, 24, Element.ALIGN_LEFT);
        titleCt.addText(titleText);
        titleCt.go();

        // Service subtitle text
        Font subtitleFont = new Font(bf, 12, Font.NORMAL, new Color(123, 123, 123, 1));
        Chunk subtitleText = new Chunk("Invoice ini merupakan bukti pembayaran yang sah dari holigo");
        subtitleText.setFont(subtitleFont);
        ColumnText subtitleCt = new ColumnText(cb);
        subtitleCt.setSimpleColumn(50, 682, 335, 720, 19, Element.ALIGN_LEFT);
        subtitleCt.addText(subtitleText);
        subtitleCt.go();

        // Phone image
        Image phone = Image.getInstance("https://sandbox.holigo.id/images/pul.png");
        phone.setAbsolutePosition(50, 625);
        phone.scaleAbsolute(43, 32);
        phone.setInterpolation(true);
        cb.addImage(phone);

        // Invoice number label
        Font invoiceLblFont = new Font(bf, 12, Font.NORMAL, new Color(0, 0, 0, 1));
        Chunk invoiceNumberLbl = new Chunk("Nomor\n");
        invoiceNumberLbl.setFont(invoiceLblFont);
        Chunk invoiceDateLbl = new Chunk("Tanggal");
        invoiceDateLbl.setFont(invoiceLblFont);

        ColumnText invoiceNumberLblCt = new ColumnText(cb);
        invoiceNumberLblCt.setSimpleColumn(105, 605, 155, 673, 24, Element.ALIGN_LEFT);
        invoiceNumberLblCt.addText(invoiceNumberLbl);
        invoiceNumberLblCt.addText(invoiceDateLbl);
        invoiceNumberLblCt.go();

        // Invoice number value
        Font InvoiceNumberValueFont = new Font(bf, 12, Font.NORMAL, new Color(123, 123, 123, 1));
        Chunk invoiceNumberValue = new Chunk("433/20210228/99849586\n");
        invoiceNumberValue.setFont(InvoiceNumberValueFont);
        Chunk invoiceDateValue = new Chunk("28 Feb 2022 15:30");
        invoiceDateValue.setFont(InvoiceNumberValueFont);

        ColumnText invoiceNumberValueCt = new ColumnText(cb);
        invoiceNumberValueCt.setSimpleColumn(160, 605, 300, 673, 24, Element.ALIGN_LEFT);
        invoiceNumberValueCt.addText(invoiceNumberValue);
        invoiceNumberValueCt.addText(invoiceDateValue);
        invoiceNumberValueCt.go();


        // Font detail label
        Font detailLbl = new Font(bf, 12, Font.NORMAL, new Color(71, 71, 71, 1));
        
        // Font detail value
        Font detailValue = new Font(bf, 12, Font.NORMAL, new Color(0, 0, 0, 1));

        ColumnText ct = new ColumnText(cb);

        // Detail label
        ct.setSimpleColumn(50, 340 + 28 * 10, 200, 340, 40, Element.ALIGN_LEFT);
        ct.addText(new Phrase(40, "Status\n", detailLbl));
        ct.addText(new Phrase(40, "Methode Pembayaran\n", detailLbl));
        ct.addText(new Phrase(40, "Produk\n", detailLbl));
        ct.addText(new Phrase(40, "Nomer Pelanggan\n", detailLbl));
        ct.addText(new Phrase(40, "Harga\n", detailLbl));
        ct.addText(new Phrase(40, "Serial Number\n", detailLbl));
        ct.addText(new Phrase(40, "Info tambahan\n", detailLbl));
        ct.go();

        // Detail value
        ct.setSimpleColumn(205, 340 + 28 * 10, 400, 340, 40, Element.ALIGN_LEFT);
        ct.addText(new Phrase(40, "Berhasil\n", detailValue));
        ct.addText(new Phrase(40, "Transfer Bank BCA\n", detailValue));
        ct.addText(new Phrase(40, "Paket Data Indosat ...\n", detailValue));
        ct.addText(new Phrase(40, "085718187373\n", detailValue));
        ct.addText(new Phrase(40, "Rp. 105.000\n", detailValue));
        ct.addText(new Phrase(40, "76TYYYHG67665434HB\n", detailValue));
        ct.addText(new Phrase(40, "Harga sudah termasuk PPN 10%\n", detailValue));
        ct.go();

        // Fare amount label
        Font fareAmountFont = new Font(bf, 14, Font.NORMAL, new Color(0, 138, 17, 1));
        Chunk fareAmountLbl = new Chunk("Total Bayar");
        fareAmountLbl.setFont(fareAmountFont);
        ColumnText fareAmountLblCt = new ColumnText(cb);
        fareAmountLblCt.setSimpleColumn(205, 340, 305, 300, 40, Element.ALIGN_LEFT);
        fareAmountLblCt.addText(fareAmountLbl);
        fareAmountLblCt.go();

        // Fare amount value
        Chunk fareAmountValue = new Chunk("Rp 64.000.000");
        fareAmountValue.setFont(fareAmountFont);
        ColumnText fareAmountValueCt = new ColumnText(cb);
        fareAmountValueCt.setSimpleColumn(305, 340, 400, 300, 40, Element.ALIGN_RIGHT);
        fareAmountValueCt.addText(fareAmountValue);
        fareAmountValueCt.go();
        
        // Background line
        PdfContentByte cbu = writer.getDirectContentUnder();
        // set fill color
        cbu.setRGBColorFill(250, 250, 250);
        // set line color
        cbu.setRGBColorStroke(250, 250, 250);;
        // draw a rectangle
        cbu.rectangle(25, 565, 400, 40);
        cbu.rectangle(25, 485, 400, 40);
        cbu.rectangle(25, 405, 400, 40);
        cbu.rectangle(25, 325, 400, 40);
        // stroke the lines
        cbu.closePathFillStroke();
        cbu.stroke();
        //reset RGB color
        cbu.resetRGBColorStroke();
        cbu.resetRGBColorFill();
        cbu.sanityCheck();

        document.close();

    }

}
