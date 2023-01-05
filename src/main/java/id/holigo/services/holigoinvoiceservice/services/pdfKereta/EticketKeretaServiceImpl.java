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
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import id.holigo.services.holigoinvoiceservice.services.style.StylePdfService;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.krysalis.barcode4j.impl.pdf417.PDF417;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
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

        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(84, 28);
        ImageData imageDataEmail = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/mail-huge_hktWHMzK0.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143472868");
        Image imageMail = new Image(imageDataEmail).scaleAbsolute(9, 8);
        ImageData phoneData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/phone-huge_MSWlXRVSC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143417375");
        Image phoneImg = new Image(phoneData).scaleAbsolute(9, 8);

        int page = 0;

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
        //        Size Table Declarartion

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

        // - - - - - HEADER  - - - - -
        ImageData imageDataAtaTour = null;
        ImageData imageDataKai = null;

        try {
            imageDataAtaTour = ImageDataFactory.create("https://ik.imagekit.io/holigo/transportasi/ATA_TOUR_logo_2oxKzullM.png?ik-sdk-version=javascript-1.4.3&updatedAt=1672655066539");
            imageDataKai = ImageDataFactory.create("https://ik.imagekit.io/holigo/transportasi/logo-kai-main_SyEqhgYKx.png?ik-sdk-version=javascript-1.4.3&updatedAt=1672655146525");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image imageAtaTour = new Image(imageDataAtaTour).scaleAbsolute(90, 90);
        Image imageKai = new Image(imageDataKai).scaleAbsolute(50, 50);

        document.add(stylePdfService.headerTrain(imageAtaTour, imageKai, imageLogo));

        String title = messageSource.getMessage("invoice.generic-title-bukti-pembayaran", null, LocaleContextHolder.getLocale());
        String subTitle = messageSource.getMessage("invoice.subtitle-kereta", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.headerTitleNoImage(plusJakarta, title, subTitle));

        //--> ID TRANSAKSI
        String transactionId = messageSource.getMessage("invoice.id-transaksi", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.transaksiId(transactionId, plusJakarta, transactionDto));
        document.add(stylePdfService.oneLine(pdfDocument));
        document.add(space);

        // CONTAINER 1
        document.add(container1(transactionDto, stylePdfService, plusJakarta, messageSource, page));
        // END CONTAINER 1
        // CONTAINER 2
        document.add(container2(transactionDto, stylePdfService, plusJakarta, messageSource, page));
        // END CONTAINER 2
        // CONTAINER 3
        document.add(container3(transactionDto, stylePdfService, plusJakarta, messageSource, page));
        // END CONTAINER 3


        document.add(stylePdfService.footer(plusJakarta, pdfDocument, imageMail, phoneImg));

        // page 2
        int trips = transactionDto.getDetail().get("trips").size();
        if (trips == 2) {
            page = 1;

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(stylePdfService.headerTrain(imageAtaTour, imageKai, imageLogo));
            document.add(stylePdfService.headerTitleNoImage(plusJakarta, title, subTitle));
            document.add(stylePdfService.transaksiId(transactionId, plusJakarta, transactionDto));
            document.add(stylePdfService.oneLine(pdfDocument));
            document.add(space);
            document.add(container1(transactionDto, stylePdfService, plusJakarta, messageSource, page));
            document.add(container2(transactionDto, stylePdfService, plusJakarta, messageSource, page));
            document.add(container3(transactionDto, stylePdfService, plusJakarta, messageSource, page));
            document.add(stylePdfService.footer(plusJakarta, pdfDocument, imageMail, phoneImg));
        }


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

    private static Table container1(TransactionDto transactionDto, StylePdfService stylePdfService, PdfFont plusJakarta, MessageSource messageSource, int page) throws MalformedURLException {

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

        ImageData maskapaiImgData = ImageDataFactory.create(transactionDto.getDetail().get("trips").get(page).get("imageUrl").asText());
        Image maskapaiImg = new Image(maskapaiImgData).scaleAbsolute(100, 100);
        Table container1 = new Table(new float[]{155, 5, 265, 100});

        // Formating Date
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy");
        // Formating Time
        DateFormat inputTime = new SimpleDateFormat("HH:mm:ss");
        DateFormat outputTime = new SimpleDateFormat("HH:mm");
        //        --HELP/Intruction-- 1

        //time flight
        Table destinationTabel = new Table(new float[]{130, 10, 130});
        Paragraph infoPrg = new Paragraph();
        Table dstDate = new Table(new float[]{140});

        Table helpTbl = new Table(new float[]{45, 105});
        Date originDate;
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
        try {
            originDate = inputDate.parse(transactionDto.getDetail()
                    .get("trips").get(page)
                    .get("departureDate").asText());
            dstDate.addCell(stylePdfService.getDestination(outputDate.format(originDate), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }

        Date originTime;
        try {
            originTime = inputTime.parse(transactionDto.getDetail()
                    .get("trips").get(page)
                    .get("departureTime").asText());
            dstDate.addCell(stylePdfService.getDestination(outputTime.format(originTime), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }
        destinationTabel.addCell(new Cell().add(dstDate).setBorder(Border.NO_BORDER));
        destinationTabel.addCell(new Cell().add(startPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));

        Table kotaDepartureTable = new Table(new float[]{120});
        String departureCity = transactionDto.getDetail().get("trips").get(page).get("originStation").get("city").asText() + " (" + transactionDto.getDetail().get("trips").get(page).get("originStation").get("id").asText() + ")";
        kotaDepartureTable.addCell(stylePdfService.getDestination(departureCity, plusJakarta, TextAlignment.LEFT, true));
        String departureTerminal = transactionDto.getDetail().get("trips").get(page).get("originStation").get("name").asText();
        kotaDepartureTable.addCell(stylePdfService.getDestination(departureTerminal, plusJakarta, TextAlignment.LEFT, false));
        destinationTabel.addCell(new Cell().add(kotaDepartureTable).setBorder(Border.NO_BORDER));

        destinationTabel.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        String timeFlight;
        String departureDateStr = transactionDto.getDetail().get("trips").get(page).get("departureDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("departureTime").asText();
        String arrivalDateStr = transactionDto.getDetail().get("trips").get(page).get("arrivalDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("arrivalTime").asText();
        SimpleDateFormat formatDateDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double diff;
        try {
            Date departureDate = formatDateDestination.parse(departureDateStr);
            Date arrivalDate = formatDateDestination.parse(arrivalDateStr);
            diff = arrivalDate.getTime() - departureDate.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(" date error ");
        }
        double diffMinute = diff / (60 * 1000);
        long durationFlightHour = 0;
        double durationFlightMinute;
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
                    .get("trips").get(page)
                    .get("arrivalDate").asText());
            dstDateArrival.addCell(stylePdfService.getDestination(outputDate.format(arrivalDate), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }

        Date arrivalTime;
        try {
            arrivalTime = inputTime.parse(transactionDto.getDetail()
                    .get("trips").get(page)
                    .get("arrivalTime").asText());
            dstDateArrival.addCell(stylePdfService.getDestination(outputTime.format(arrivalTime), plusJakarta, TextAlignment.RIGHT, true));
        } catch (ParseException e) {
            throw new RuntimeException("Date Format Error");
        }

        destinationTabel.addCell(new Cell().add(dstDateArrival).setBorder(Border.NO_BORDER));
        destinationTabel.addCell(new Cell().add(endPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));
        Table kotaArrivalTable = new Table(new float[]{120});
        String arrivalCity = transactionDto.getDetail().get("trips").get(page).get("destinationStation").get("city").asText() +
                " (" + transactionDto.getDetail().get("trips").get(page).get("destinationStation").get("id").asText() + ")";
        kotaArrivalTable.addCell(stylePdfService.getDestination(arrivalCity, plusJakarta, TextAlignment.LEFT, true));
        String arrivalTerminal = transactionDto.getDetail().get("trips").get(page).get("destinationStation").get("name").asText();
        kotaArrivalTable.addCell(stylePdfService.getDestination(arrivalTerminal, plusJakarta, TextAlignment.LEFT, false));
        destinationTabel.addCell(new Cell().add(kotaArrivalTable).setBorder(Border.NO_BORDER));

        Table parentDestination = new Table(new float[]{270});
        parentDestination.addCell(new Cell().add(destinationTabel)
                .setBorder(Border.NO_BORDER)
        );
        parentDestination.addCell(stylePdfService.smallSpaceInColumn());
        parentDestination.addCell(stylePdfService.smallSpaceInColumn());
        parentDestination.addCell(stylePdfService.smallSpaceInColumn());
        String kodebooking = messageSource.getMessage("invoice.kereta-kodeBooking", null, LocaleContextHolder.getLocale());
        String bookingCodeOut = transactionDto.getDetail().get("trips").get(page).get("bookCode").asText();

        // BARCODE
        try {
            //Barcode4j
            PDF417 barcodeGenerator = new PDF417();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            barcodeGenerator.generateBarcode(canvas, bookingCodeOut);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(canvas.getBufferedImage(), "jpg", baos);
            byte[] bytes = baos.toByteArray();

            ImageData imageData = ImageDataFactory.create(bytes);
            Image image = new Image(imageData).scaleAbsolute(100, 40);
            parentDestination.addCell(new Cell().add(image).setBorder(Border.NO_BORDER).setRelativePosition(7, 0, 0, 0));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        parentDestination.addCell(new Cell().add(kodebooking).setFont(plusJakarta)
                .setFontSize(9)
                .setFontColor(new DeviceRgb(97, 97, 97))
                .setRelativePosition(5, 10, 0, 0)
                .setBorder(Border.NO_BORDER)
        );
        parentDestination.addCell(new Cell().add(bookingCodeOut).setFont(plusJakarta)
                .setFontSize(20)
                .setFontColor(new DeviceRgb(0, 189, 23))
                .setRelativePosition(5, 0, 0, 0)
                .setBorder(Border.NO_BORDER)
                .setBold());

        container1.addCell(new Cell().add(parentDestination).setBorder(Border.NO_BORDER)); // end Destination

        //        --TRAIN-- 3
        Table maskapaiTabel = new Table(new float[]{135});


        maskapaiTabel.addCell(new Cell().add(maskapaiImg).setBorder(Border.NO_BORDER));
        String trainName = transactionDto.getDetail().get("trips").get(page).get("trainName").asText();
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainName, plusJakarta));

        String trainNumber = transactionDto.getDetail().get("trips").get(page).get("trainNumber").asText();
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainNumber, plusJakarta));

        String[] trainClass = {transactionDto.getDetail().get("trips").get(page).get("trainClass").asText(), transactionDto.getDetail().get("trips").get(page).get("trainSubClass").asText()};
        String trainClassInfo = trainClass[0] + "-subclass " + trainClass[1];
        maskapaiTabel.addCell(stylePdfService.eticketInfo(trainClassInfo, plusJakarta));
        maskapaiTabel.addCell(new Cell().add(" ").setFontSize(20).setHeight(20).setBorder(Border.NO_BORDER));

        maskapaiTabel.addCell(new Cell().add(trainImg).setBorder(Border.NO_BORDER));
        container1.addCell(new Cell().add(maskapaiTabel).setBorder(Border.NO_BORDER));

        return container1;
    }

    private static Table container2(TransactionDto transactionDto, StylePdfService stylePdfService, PdfFont plusJakarta, MessageSource messageSource, int page) {
        float col = 200f;
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
        Table detailPenumpangTable = new Table(new float[]{60, 260, 180, col});
        detailPenumpangTable.setMargins(0, 0, 0, 10);


        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell("No.", plusJakarta));
        String namaPenumpang = messageSource.getMessage("invoice.generic-namaPenumpang", null, LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(namaPenumpang, plusJakarta));
        String nomorIdentitas = messageSource.getMessage("invoice.generic-nomorIdentitas", null, LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(nomorIdentitas, plusJakarta));


        String nomorKursi = messageSource.getMessage("invoice.generic-nomorKursi", null, LocaleContextHolder.getLocale());
        detailPenumpangTable.addCell(stylePdfService.getHeaderTextCell(nomorKursi, plusJakarta));


        int countPenumpang = transactionDto.getDetail().get("trips").get(page).get("passengers").size();
        int count = 1;
        for (int passanger = 0; countPenumpang > passanger; passanger++) {
            //no.
            detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(count + "", plusJakarta));
            count = count + 1;
            //nama penumpang
            try {
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(transactionDto.getDetail().get("trips").get(page).get("passengers").get(passanger).get("passenger").get("name").asText(), plusJakarta));
            } catch (NullPointerException e) {
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput("-", plusJakarta));
            }
            //nomor identitas || passport
            try {
                //nomor identitas
                String nomorIdentitasOutput = transactionDto.getDetail().get("trips").get(page).get("passengers").get(passanger).get("passenger").get("identityCard").get("idCardNumber").asText();
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(nomorIdentitasOutput, plusJakarta));
            } catch (NullPointerException e) {
                //passport
                String passportOutput = transactionDto.getDetail().get("trips").get(page).get("passengers").get(passanger).get("passenger").get("passport").get("passportNumber").asText();
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(passportOutput, plusJakarta));
            }

            //nomor Kursi
            String nomorKursiOutput = transactionDto.getDetail().get("trips").get(page).get("passengers").get(passanger).get("seatNumber").asText();
            if (nomorKursiOutput.isEmpty() || nomorKursiOutput.isBlank() || nomorKursiOutput.equalsIgnoreCase("null")) {
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput("-", plusJakarta));
            } else {
                detailPenumpangTable.addCell(stylePdfService.getDetailPenumpangOutput(nomorKursiOutput, plusJakarta));
            }


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
        return container2;
    }

    private static Table container3(TransactionDto transactionDto, StylePdfService stylePdfService, PdfFont plusJakarta, MessageSource messageSource, int page) {
        Table contrainer3 = new Table(new float[]{525});
//        String termCondition = messageSource.getMessage("invoice.term-condition-voucher", null, LocaleContextHolder.getLocale());
//        contrainer3.addCell(stylePdfService.getSyaratKetentuan(termCondition,plusJakarta).setBorder(Border.NO_BORDER));
        String term = messageSource.getMessage("invoice.term-condition-train", null, LocaleContextHolder.getLocale());
        String term1 = messageSource.getMessage("invoice.term-condition-train1", null, LocaleContextHolder.getLocale());
        String term2 = messageSource.getMessage("invoice.term-condition-train2", null, LocaleContextHolder.getLocale());
        String term3 = messageSource.getMessage("invoice.term-condition-train3", null, LocaleContextHolder.getLocale());
        String term4 = messageSource.getMessage("invoice.term-condition-train4", null, LocaleContextHolder.getLocale());
        String term5 = messageSource.getMessage("invoice.term-condition-train5", null, LocaleContextHolder.getLocale());
        String term6 = messageSource.getMessage("invoice.term-condition-train6", null, LocaleContextHolder.getLocale());
        String term7 = messageSource.getMessage("invoice.term-condition-train7", null, LocaleContextHolder.getLocale());
        String termEnd = messageSource.getMessage("invoice.term-condition-trainEnd", null, LocaleContextHolder.getLocale());
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term1, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term2, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term3, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term4, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term5, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term6, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(term7, plusJakarta).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuan(termEnd, plusJakarta).setBorder(Border.NO_BORDER));

        return contrainer3;
    }
}
