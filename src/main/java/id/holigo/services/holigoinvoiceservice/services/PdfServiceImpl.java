package id.holigo.services.holigoinvoiceservice.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private final MessageSource messageSource;

    public void export(HttpServletResponse response, TransactionDto transactionDto)
            throws DocumentException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String titleLbl = "";
        String subtitleLbl = messageSource.getMessage("invoice.description", null, LocaleContextHolder.getLocale());
        String invoiceNumberLblText = messageSource.getMessage("invoice.number", null, LocaleContextHolder.getLocale());
        String invoiceDateLblText = messageSource.getMessage("invoice.date", null, LocaleContextHolder.getLocale());
        String invoiceDateValueText = sdf.format(transactionDto.getCreatedAt());// sdf.format(transactionDto.getCreatedAt()).toString();
        String invoiceNumberValueText = transactionDto.getInvoiceNumber();
        String statusText = getStatus(transactionDto.getOrderStatus());
        String paymentMethodText = transactionDto.getPayment().getPaymentService().getName();
        String productText = getProduct(transactionDto.getIndexProduct());
        String customerNumberText = transactionDto.getDetail().get("customerNumber").asText();
        String getFareText = messageSource.getMessage("invoice.rupiah", null, LocaleContextHolder.getLocale()) + " ";
        String getTotalFareText = messageSource.getMessage("invoice.rupiah", null, LocaleContextHolder.getLocale())
                + " ";
        String infoText = messageSource.getMessage("invoice.tax-included", null, LocaleContextHolder.getLocale());
        String serialNumberText = "";
        String customerNumberLbl = "invoice.customer-number";
        String serialNumberLbl = "invoice.serial-number";
        String fareLbl = "invoice.fare";
        int startingPoint = 620;
        int endingPoint = 340;
        switch (transactionDto.getTransactionType()) {
            case "PUL":
                titleLbl = messageSource.getMessage("invoice.prepaid-pulsa", null,
                        LocaleContextHolder.getLocale());
                getFareText += getPrice(transactionDto.getFareAmount());
                break;
            case "PR":
                titleLbl = messageSource.getMessage("invoice.prepaid-pulsa", null,
                        LocaleContextHolder.getLocale());
                getFareText += getPrice(transactionDto.getFareAmount());
                break;
            case "PD":
                titleLbl = messageSource.getMessage("invoice.prepaid-pulsa", null,
                        LocaleContextHolder.getLocale());
                getFareText += getPrice(transactionDto.getFareAmount());
                break;
            case "PRA":
                titleLbl = messageSource.getMessage("invoice.prepaid-electricity", null,
                        LocaleContextHolder.getLocale());
                serialNumberLbl = "invoice.token-number";
                customerNumberLbl = "invoice.customer-number";
                fareLbl = "invoice.kwh";
                getFareText = transactionDto.getDetail().get("customerName").asText();
                serialNumberText = transactionDto.getDetail().get("token").asText();
                productText = transactionDto.getDetail().get("productName").asText();
                getTotalFareText += getPrice(transactionDto.getFareAmount());
                startingPoint = 620;
                endingPoint = 260;
                break;
            case "PAS":
                titleLbl = messageSource.getMessage("invoice.prepaid-electricity", null,
                        LocaleContextHolder.getLocale());
                serialNumberText = transactionDto.getDetail().get("standMeter").asText();
                productText = transactionDto.getDetail().get("productName").asText();
                startingPoint = 620;
                endingPoint = 180;
                getFareText += getPrice(transactionDto.getFareAmount());
                getTotalFareText += getPrice(transactionDto.getFareAmount());
                break;
            case "CC":
                titleLbl = messageSource.getMessage("invoice.postpaid-cc", null,
                        LocaleContextHolder.getLocale());
                serialNumberLbl = "invoice.reference";
                customerNumberLbl = "invoice.card-number";
                serialNumberText = transactionDto.getDetail().get("reference").asText();
                fareLbl = "invoice.admin-fee";
                getFareText += getPrice(BigDecimal.valueOf(transactionDto.getDetail().get("adminAmount").asLong()));
                getTotalFareText += getPrice(transactionDto.getFareAmount());
                break;
            default:
                getFareText += getPrice(transactionDto.getFareAmount());
                getTotalFareText += getPrice(transactionDto.getFareAmount());
                serialNumberText = transactionDto.getDetail().get("serialNumber").asText();
                customerNumberLbl = "invoice.customer-number";
                break;

        }

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
        Chunk holigoText = new Chunk(messageSource.getMessage("invoice.app", null, LocaleContextHolder.getLocale()));
        holigoText.setFont(holigoFont);
        ColumnText holigoCt = new ColumnText(cb);
        holigoCt.setSimpleColumn(102, 765, 200, 805, 40, Element.ALIGN_LEFT);
        holigoCt.addText(holigoText);
        holigoCt.go();

        // Service title text
        Font titleFont = new Font(bf, 12, Font.NORMAL, new Color(0, 0, 0, 1));
        Chunk titleText = new Chunk(titleLbl);
        titleText.setFont(titleFont);
        ColumnText titleCt = new ColumnText(cb);
        titleCt.setSimpleColumn(50, 720, 335, 744, 24, Element.ALIGN_LEFT);
        titleCt.addText(titleText);
        titleCt.go();

        // Service subtitle text
        Font subtitleFont = new Font(bf, 12, Font.NORMAL, new Color(123, 123, 123, 1));
        Chunk subtitleText = new Chunk(subtitleLbl);
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
        Chunk invoiceNumberLbl = new Chunk(invoiceNumberLblText + "\n");
        invoiceNumberLbl.setFont(invoiceLblFont);
        Chunk invoiceDateLbl = new Chunk(invoiceDateLblText);
        invoiceDateLbl.setFont(invoiceLblFont);

        ColumnText invoiceNumberLblCt = new ColumnText(cb);
        invoiceNumberLblCt.setSimpleColumn(105, 605, 155, 673, 24, Element.ALIGN_LEFT);
        invoiceNumberLblCt.addText(invoiceNumberLbl);
        invoiceNumberLblCt.addText(invoiceDateLbl);
        invoiceNumberLblCt.go();

        // Invoice number value
        Font InvoiceNumberValueFont = new Font(bf, 12, Font.NORMAL, new Color(123, 123, 123, 1));
        Chunk invoiceNumberValue = new Chunk(invoiceNumberValueText + "\n");
        invoiceNumberValue.setFont(InvoiceNumberValueFont);
        Chunk invoiceDateValue = new Chunk(invoiceDateValueText);
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
        ct.setSimpleColumn(50, startingPoint, 200, endingPoint, 40, Element.ALIGN_LEFT);
        ct.addText(new Phrase(40,
                messageSource.getMessage("invoice.status", null, LocaleContextHolder.getLocale()) + "\n", detailLbl));
        ct.addText(new Phrase(40,
                messageSource.getMessage("invoice.payment-method", null, LocaleContextHolder.getLocale()) + "\n",
                detailLbl));
        ct.addText(new Phrase(40,
                messageSource.getMessage("invoice.product", null, LocaleContextHolder.getLocale()) + "\n", detailLbl));
        ct.addText(new Phrase(40,
                messageSource.getMessage(customerNumberLbl, null, LocaleContextHolder.getLocale()) + "\n",
                detailLbl));
        switch (transactionDto.getTransactionType()) {
            case "PRA":
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.customer-name", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.power-electricity", null, LocaleContextHolder.getLocale())
                                + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage(fareLbl, null, LocaleContextHolder.getLocale()) + "\n", detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage(serialNumberLbl, null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                break;
            case "PAS":
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.customer-name", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.power-electricity", null, LocaleContextHolder.getLocale())
                                + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.period", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.stand-meter", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.bill-amount", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage("invoice.admin-fee", null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));
                break;

            default:
                ct.addText(new Phrase(40,
                        messageSource.getMessage(fareLbl, null, LocaleContextHolder.getLocale()) + "\n", detailLbl));
                ct.addText(new Phrase(40,
                        messageSource.getMessage(serialNumberLbl, null, LocaleContextHolder.getLocale()) + "\n",
                        detailLbl));

                break;
        }

        ct.addText(new Phrase(40,
                messageSource.getMessage("invoice.additional-information", null, LocaleContextHolder.getLocale())
                        + "\n",
                detailLbl));
        ct.go();

        // Detail value
        ct.setSimpleColumn(205, startingPoint, 400, endingPoint, 40, Element.ALIGN_LEFT);
        ct.addText(new Phrase(40, statusText + "\n", detailValue));
        ct.addText(new Phrase(40, paymentMethodText + "\n", detailValue));
        ct.addText(new Phrase(40, productText + "\n", detailValue));
        ct.addText(new Phrase(40, customerNumberText + "\n", detailValue));

        switch (transactionDto.getTransactionType()) {
            case "PRA":
                ct.addText(new Phrase(40, getFareText + "\n", detailValue));
                ct.addText(new Phrase(40, transactionDto.getDetail().get("tariffAdjustment").asText() + "\n",
                        detailValue));
                ct.addText(new Phrase(40, transactionDto.getDetail().get("kwh").asText() + "\n", detailValue));
                ct.addText(new Phrase(40, serialNumberText + "\n", detailValue));
                break;
            case "PAS":
                String rupiah = messageSource.getMessage("invoice.rupiah", null, LocaleContextHolder.getLocale())
                        + " ";
                ct.addText(new Phrase(40, transactionDto.getDetail().get("customerName").asText() + "\n", detailValue));
                ct.addText(new Phrase(40, transactionDto.getDetail().get("tariffAdjustment").asText() + "\n",
                        detailValue));
                ct.addText(new Phrase(40, transactionDto.getDetail().get("period").asText() + "\n", detailValue));
                ct.addText(new Phrase(40, serialNumberText + "\n", detailValue));
                ct.addText(new Phrase(40, rupiah + getPrice(BigDecimal
                        .valueOf(Double.parseDouble((transactionDto.getDetail().get("billAmount").asText())))) + "\n",
                        detailValue));
                ct.addText(new Phrase(40, rupiah + getPrice(BigDecimal
                        .valueOf(Double.parseDouble(transactionDto.getDetail().get("adminAmount").asText()))) + "\n",
                        detailValue));
                break;
            default:
                ct.addText(new Phrase(40, getFareText + "\n", detailValue));
                ct.addText(new Phrase(40, serialNumberText + "\n", detailValue));
                break;
        }

        ct.addText(new Phrase(40, infoText + "\n", detailValue));
        ct.go();

        // Fare amount label
        Font fareAmountFont = new Font(bf, 14, Font.NORMAL, new Color(0, 138, 17, 1));
        Chunk fareAmountLbl = new Chunk("Total Bayar");
        fareAmountLbl.setFont(fareAmountFont);
        ColumnText fareAmountLblCt = new ColumnText(cb);
        // label y1= 340
        // lable y2= 300
        fareAmountLblCt.setSimpleColumn(205, endingPoint - 10, 305, endingPoint - 50, 40, Element.ALIGN_LEFT);
        fareAmountLblCt.addText(fareAmountLbl);
        fareAmountLblCt.go();

        // Fare amount value
        Chunk fareAmountValue = new Chunk(getTotalFareText);
        fareAmountValue.setFont(fareAmountFont);
        ColumnText fareAmountValueCt = new ColumnText(cb);
        fareAmountValueCt.setSimpleColumn(305, endingPoint - 10, 400, endingPoint - 50, 40, Element.ALIGN_RIGHT);
        fareAmountValueCt.addText(fareAmountValue);
        fareAmountValueCt.go();

        // Background line
        PdfContentByte cbu = writer.getDirectContentUnder();
        // set fill color
        cbu.setRGBColorFill(250, 250, 250);
        // set line color
        cbu.setRGBColorStroke(250, 250, 250);
        ;
        // draw a rectangle
        cbu.rectangle(25, 565, 400, 40);
        cbu.rectangle(25, 485, 400, 40);
        cbu.rectangle(25, 405, 400, 40);
        cbu.rectangle(25, 325, 400, 40);
        if (transactionDto.getTransactionType().equals("PRA")) {
            cbu.rectangle(25, 245, 400, 40);
        }
        // stroke the lines
        cbu.closePathFillStroke();
        cbu.stroke();
        // reset RGB color
        cbu.resetRGBColorStroke();
        cbu.resetRGBColorFill();
        cbu.sanityCheck();

        document.close();

    }

    private String getProduct(String indexProduct) {
        String product = indexProduct.split("\\|")[1] + " " + indexProduct.split("\\|")[2];
        if (product.length() > 30) {
            product.substring(0, 28);
            product += product + "...";
        }
        return product;
    }

    private String getStatus(OrderStatusEnum status) {
        String result = "";
        switch (status) {
            case PROCESS_BOOK:
                result = messageSource.getMessage("invoice.status-process-book", null, LocaleContextHolder.getLocale());
                break;
            case BOOKED:
                result = messageSource.getMessage("invoice.status-booked", null, LocaleContextHolder.getLocale());
                break;
            case BOOK_FAILED:
                result = messageSource.getMessage("invoice.status-book-failed", null, LocaleContextHolder.getLocale());
                break;
            case PROCESS_ISSUED:
                result = messageSource.getMessage("invoice.status-process-issued", null,
                        LocaleContextHolder.getLocale());
                break;
            case WAITING_ISSUED:
                result = messageSource.getMessage("invoice.status-waiting-issued", null,
                        LocaleContextHolder.getLocale());
                break;
            case ISSUED:
                result = messageSource.getMessage("invoice.status-issued", null, LocaleContextHolder.getLocale());
                break;
            case RETRYING_ISSUED:
                result = messageSource.getMessage("invoice.status-retrying-issued", null,
                        LocaleContextHolder.getLocale());
                break;
            case ISSUED_FAILED:
                result = messageSource.getMessage("invoice.status-issued-failed", null,
                        LocaleContextHolder.getLocale());
                break;
            case ORDER_EXPIRED:
                result = messageSource.getMessage("invoice.status-order-expired", null,
                        LocaleContextHolder.getLocale());
                break;
            case ORDER_CANCELED:
                result = messageSource.getMessage("invoice.status-order-canceled", null,
                        LocaleContextHolder.getLocale());
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    private String getPrice(BigDecimal price) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(otherSymbols);
        return df.format(price.round(MathContext.UNLIMITED));
    }

}
