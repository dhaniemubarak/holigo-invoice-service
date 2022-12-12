package id.holigo.services.holigoinvoiceservice.services.pdfKereta;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
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
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class EticketKeretaServiceImpl implements EticketKeretaService {

    @Autowired
    private final MessageSource messageSource;

    @Override
    public void eticketKereta(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService) throws MalformedURLException {
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

        //        Image
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image imageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);

        ImageData timeImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/time_GTSbqaC0g_82z2L8w2D.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783948");
        Image timeImg = new Image(timeImgData).scaleAbsolute(44, 44);
        ImageData ticketHelpData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/voucher_Rmwi88eJd_h5rAd0OAg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225784204");
        Image ticketHelpImg = new Image(ticketHelpData).scaleAbsolute(44, 44);
        ImageData informationImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/information_9VP2VqxAc_wLTMswxFx.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663225783892");
        Image informationImg = new Image(informationImgData).scaleAbsolute(44, 44);

        ImageData trainImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/Train_XNV1h0NTt.png?ik-sdk-version=javascript-1.4.3&updatedAt=1669881619923");
        Image trainImg = new Image(trainImgData).scaleAbsolute(94, 94);

        ImageData startPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/start-point_9MPllA5Gg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578713");
        Image startPointImg = new Image(startPointData).scaleAbsolute(10, 40);
        ImageData endPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/end-point_iijnQ0Xsb.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578673");
        Image endPointImg = new Image(endPointData).scaleAbsolute(10, 40);

        ImageData maskapaiImgData = ImageDataFactory.create(transactionDto.getDetail().get("trips").get(0).get("imageUrl").asText());
        Image maskapaiImg = new Image(maskapaiImgData).scaleAbsolute(maskapaiImgData.getWidth(), maskapaiImgData.getHeight());

        //        Size Table Declarartion
        float threeCol = 190f;
        float col = 200f;
        float col150 = col + 150;

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
        String title = messageSource.getMessage("invoice.generic-title", null, LocaleContextHolder.getLocale());
        String subTitle = messageSource.getMessage("invoice.subtitle-kereta", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, title, subTitle));

        //      --> ID TRANSAKSI START
        String transactionId = messageSource.getMessage("invoice.id-transaksi", null, LocaleContextHolder.getLocale());

        document.add(stylePdfService.transaksiId(transactionId, plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));
        document.add(space);

        //        Body / container
        Table container1 = new Table(new float[]{155, 5, 265, 100});

        //        --HELP/Intruction-- 1
        Table helpTbl = new Table(new float[]{45, 105});
        helpTbl.addCell(new Cell().add(ticketHelpImg).setBorder(Border.NO_BORDER));

        Paragraph ticketPrg = new Paragraph();
        ticketPrg.setFixedLeading(10);
        String ticketStr = messageSource.getMessage("invoice.kereta-ticket", null, LocaleContextHolder.getLocale());
        ticketPrg.add(ticketStr);
        helpTbl.addCell(stylePdfService.getInfo(ticketPrg, plusJakarta));
        helpTbl.addCell(new Cell().add(timeImg).setBorder(Border.NO_BORDER));
        Paragraph timePrg = new Paragraph();
        String timeStr = messageSource.getMessage("invoice.kereta-time", null, LocaleContextHolder.getLocale());
        timePrg.add(timeStr).setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(timePrg, plusJakarta));
        helpTbl.addCell(new Cell().add(informationImg).setBorder(Border.NO_BORDER));
        Paragraph infoPrg = new Paragraph();
        String infoStr = messageSource.getMessage("invoice.kereta-info", null, LocaleContextHolder.getLocale());
        infoPrg.add(infoStr).setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(infoPrg, plusJakarta));
        container1.addCell(new Cell().add(helpTbl).setBorder(Border.NO_BORDER));

        //        --SPACE-- 1.5
        container1.addCell(new Cell().add(" ")
                .setBorderBottom(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));
        //        --DESTINATION-- 2
        Table destinationTabel = new Table(new float[]{130, 10, 130});

        // Formating Date
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy");
        // Formating Time
        DateFormat inputTime = new SimpleDateFormat("hh:mm:ss");
        DateFormat outputTime = new SimpleDateFormat("hh:mm");

        Table dstDate = new Table(new float[]{140});
        Date originDate;
        try {
            originDate = inputDate.parse(transactionDto.getDetail()
                    .get("trips").get(0)
                    .get("departureDate").asText());
            dstDate.addCell(stylePdfService.getDestination(outputDate.format(originDate), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }

        Date originTime;
        try {
            originTime = inputTime.parse(transactionDto.getDetail()
                    .get("trips").get(0)
                    .get("departureTime").asText());
            dstDate.addCell(stylePdfService.getDestination(outputTime.format(originTime), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }
        destinationTabel.addCell(new Cell().add(dstDate).setBorder(Border.NO_BORDER));
        destinationTabel.addCell(new Cell().add(startPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));

        Table kotaDepartureTable = new Table(new float[]{120});
        String departureCity = transactionDto.getDetail().get("trips").get(0).get("originStation").get("city").asText() + " (" + transactionDto.getDetail().get("trips").get(0).get("originStation").get("id").asText() + ")";
        kotaDepartureTable.addCell(stylePdfService.getDestination(departureCity, plusJakarta, TextAlignment.LEFT, true));
        String departureTerminal = transactionDto.getDetail().get("trips").get(0).get("originStation").get("name").asText();
        kotaDepartureTable.addCell(stylePdfService.getDestination(departureTerminal, plusJakarta, TextAlignment.LEFT, false));
        destinationTabel.addCell(new Cell().add(kotaDepartureTable).setBorder(Border.NO_BORDER));

        //time flight
        destinationTabel.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        String timeFlight = " ";
        String departureDateStr = transactionDto.getDetail().get("trips").get(0).get("departureDate").asText() + " " + transactionDto.getDetail().get("trips").get(0).get("departureTime").asText();
        String arrivalDateStr = transactionDto.getDetail().get("trips").get(0).get("arrivalDate").asText() + " " + transactionDto.getDetail().get("trips").get(0).get("arrivalTime").asText();

        SimpleDateFormat formatDateDestination = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");
        long diff = 0;
        try {
            Date departureDate = formatDateDestination.parse(departureDateStr);
            Date arrivalDate = formatDateDestination.parse(arrivalDateStr);

            diff = arrivalDate.getTime() - departureDate.getTime();

        } catch (ParseException e) {
            throw new RuntimeException(" date error ");
        }

        long diffMinute = diff / (60 * 1000);
        long durationFlightHour = 0;
        long durationFlightMinute;
        while (diffMinute >= 60) {
            durationFlightHour = durationFlightHour + 1;
            diffMinute = diffMinute - 60;
        }
        durationFlightMinute = diffMinute;
        timeFlight = durationFlightHour + "j" + durationFlightMinute + "m";

        destinationTabel.addCell(new Cell().add(timeFlight).setBorder(Border.NO_BORDER)
                .setFontSize(9)
                .setFont(plusJakarta)
                .setTextAlignment(TextAlignment.CENTER));
        destinationTabel.addCell(new Cell().add("").setBorder(Border.NO_BORDER));

        // arrival part
        Table dstDateArrival = new Table(new float[]{140});
        Date arrivalDate;
        try {
            arrivalDate = inputDate.parse(transactionDto.getDetail()
                    .get("trips").get(0)
                    .get("arrivalDate").asText());
            dstDateArrival.addCell(stylePdfService.getDestination(outputDate.format(arrivalDate), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }

        Date arrivalTime;
        try {
            arrivalTime = inputTime.parse(transactionDto.getDetail()
                    .get("trips").get(0)
                    .get("arrivalTime").asText());
            dstDateArrival.addCell(stylePdfService.getDestination(outputTime.format(arrivalTime), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }
        destinationTabel.addCell(new Cell().add(dstDateArrival).setBorder(Border.NO_BORDER));
        destinationTabel.addCell(new Cell().add(endPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));
        Table kotaArrivalTable = new Table(new float[]{120});
        String arrivalCity = transactionDto.getDetail().get("trips").get(0).get("destinationStation").get("city").asText() +
                " (" + transactionDto.getDetail().get("trips").get(0).get("destinationStation").get("id").asText() + ")";
        kotaArrivalTable.addCell(stylePdfService.getDestination(arrivalCity, plusJakarta, TextAlignment.LEFT, true));
        String arrivalTerminal = transactionDto.getDetail().get("trips").get(0).get("destinationStation").get("name").asText();
        kotaArrivalTable.addCell(stylePdfService.getDestination(arrivalTerminal, plusJakarta, TextAlignment.LEFT, false));
        destinationTabel.addCell(new Cell().add(kotaArrivalTable).setBorder(Border.NO_BORDER));

        Table parentDestination = new Table(new float[]{270});
        parentDestination.addCell(new Cell().add(destinationTabel).setBorder(Border.NO_BORDER));
        String kodebooking = messageSource.getMessage("invoice.kereta-kodeBooking", null, LocaleContextHolder.getLocale());
        parentDestination.addCell(new Cell().add(kodebooking).setFont(plusJakarta)
                .setFontSize(9)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setRelativePosition(5, 10, 0, 0)
                .setBorder(Border.NO_BORDER)
        );
        String bookingCodeOut = transactionDto.getDetail().get("trips").get(0).get("bookCode").asText();
        parentDestination.addCell(new Cell().add(bookingCodeOut).setFont(plusJakarta)
                .setFontSize(20)
                .setFontColor(new DeviceRgb(0, 189, 23))
                .setRelativePosition(5, 0, 0, 0)
                .setBorder(Border.NO_BORDER)
                .setBold());
        container1.addCell(new Cell().add(parentDestination).setBorder(Border.NO_BORDER)); // end Destination

        //        --MASKAPAI-- 3
        Table maskapaiTabel = new Table(new float[]{135});

        maskapaiTabel.addCell(new Cell().add(maskapaiImg).setBorder(Border.NO_BORDER));
        String trainName = transactionDto.getDetail().get("trips").get(0).get("trainName").asText();
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainName,plusJakarta));

        String trainNumber = transactionDto.getDetail().get("trips").get(0).get("trainNumber").asText();
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainNumber,plusJakarta));

        String[] trainClass = {transactionDto.getDetail().get("trips").get(0).get("trainClass").asText(), transactionDto.getDetail().get("trips").get(0).get("trainSubClass").asText()};
        String trainClassInfo = trainClass[0] + "-subclass " + trainClass[1];
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainClassInfo,plusJakarta));
        maskapaiTabel.addCell(new Cell().add(" ").setFontSize(20).setHeight(20).setBorder(Border.NO_BORDER));

        maskapaiTabel.addCell(new Cell().add(trainImg).setBorder(Border.NO_BORDER));
        container1.addCell(new Cell().add(maskapaiTabel).setBorder(Border.NO_BORDER));

        // END CONTAINER 1
        document.add(container1);

        Table container2 = new Table(new float[]{525});
        String detailPenumpang = messageSource.getMessage("invoice.generic-detailPenumpang", null, LocaleContextHolder.getLocale());
        container2.addCell(stylePdfService.getHeaderTextCell(detailPenumpang, plusJakarta).setMinHeight(20));
        container2.addCell(new Cell()
                .add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 0)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setBold()
                .setMaxHeight(20)
        );
        Table detailPenumpangTable = new Table(new float[]{100, col, col,col});
        detailPenumpangTable.setMargins(0, 0, 0, 10);


        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell("No.", plusJakarta));
        String namaPenumpang = messageSource.getMessage("invoice.generic-namaPenumpang",null,LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(namaPenumpang, plusJakarta));
        String nomorIdentitas = messageSource.getMessage("invoice.generic-nomorIdentitas",null,LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(nomorIdentitas, plusJakarta));
        String nomorKursi = messageSource.getMessage("invoice.generic-nomorKursi",null,LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(nomorKursi, plusJakarta));

        int countPenumpang = transactionDto.getDetail().get("trips").get(0).get("passengers").size();
        int count = 1;
        for (int passanger = 0; countPenumpang > passanger; passanger++) {
            //no.
            detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(count + "", plusJakarta));
            count = count + 1;
            //nama penumpang
            detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(0).get("passengers").get(passanger).get("passenger").get("name").asText(), plusJakarta));
            //nomor identitas
            try{
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(0).get("passengers").get(passanger).get("passenger").get("identityCard").get("idCardNumber").asText(), plusJakarta));
            }
            catch (NullPointerException e){
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput("-", plusJakarta));
            }
            //nomer Kursi
            detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(0).get("passengers").get(passanger).get("seatNumber").asText(), plusJakarta));

        }

        container2.addCell(new Cell().add(detailPenumpangTable).setBorder(Border.NO_BORDER));

        container2.addCell(stylePdfService.smallSpaceInColumn());
        container2.addCell(new Cell()
                .add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                .setBorder(Border.NO_BORDER)
                .setPaddings(-10, 0, 0, 0)
                .setFontColor(new DeviceRgb(199, 199, 199))
                .setBold()
                .setMaxHeight(20)
        );
        document.add(container2);

        Table contrainer3 = new Table(new float[]{525});
        String termCondition = messageSource.getMessage("invoice.term-condition-voucher", null, LocaleContextHolder.getLocale());
        contrainer3.addCell(new Cell().add(termCondition));


        document.add(stylePdfService.footer(plusJakarta, pdfDocument, imageMail, phoneImg));


        document.close();
        // export to pdf
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os;
        try {
            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
