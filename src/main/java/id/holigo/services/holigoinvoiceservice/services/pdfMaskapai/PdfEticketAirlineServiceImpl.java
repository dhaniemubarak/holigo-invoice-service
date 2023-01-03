package id.holigo.services.holigoinvoiceservice.services.pdfMaskapai;

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
public class PdfEticketAirlineServiceImpl implements PdfEticketAirlineService {


    @Autowired
    private MessageSource messageSource;

    @Override
    public void airlineEticket(TransactionDto transactionDto, HttpServletResponse response, StylePdfService stylePdfService) throws IOException {
        ImageData imageDataLogo = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/logo_uAoxJeYaC.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663143887020");
        Image imageLogo = new Image(imageDataLogo).scaleAbsolute(168, 56);
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

        //      --> HEADER START
        String title = messageSource.getMessage("invoice.generic-title-ETicket", null, LocaleContextHolder.getLocale());
        String subTitle = messageSource.getMessage("invoice.subtitle-maskapai", null, LocaleContextHolder.getLocale());
        document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, title, subTitle));

        //      --> ID TRANSAKSI START
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
            document.add(stylePdfService.headerTitle(plusJakarta, imageLogo, title, subTitle));
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

        ImageData airImgData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/flight_r7fOpcE6h.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663213262777");
        Image airImg = new Image(airImgData).scaleAbsolute(94, 94);

        ImageData startPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/start-point_9MPllA5Gg.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578713");
        Image startPointImg = new Image(startPointData).scaleAbsolute(10, 40);
        ImageData endPointData = ImageDataFactory.create("https://ik.imagekit.io/holigo/invoice/compressed/end-point_iijnQ0Xsb.png?ik-sdk-version=javascript-1.4.3&updatedAt=1663582578673");
        Image endPointImg = new Image(endPointData).scaleAbsolute(10, 40);


        Table container1 = new Table(new float[]{155, 5, 290, 75});

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
        Date departureDate;
        helpTbl.addCell(new Cell().add(ticketHelpImg).setBorder(Border.NO_BORDER));

        Paragraph ticketPrg = new Paragraph();
        ticketPrg.setFixedLeading(10);
        String ticketStr = messageSource.getMessage("invoice.maskapai-ticket", null, LocaleContextHolder.getLocale());
        ticketPrg.add(ticketStr);
        helpTbl.addCell(stylePdfService.getInfo(ticketPrg, plusJakarta));
        helpTbl.addCell(new Cell().add(timeImg).setBorder(Border.NO_BORDER));
        Paragraph timePrg = new Paragraph();
        String timeStr = messageSource.getMessage("invoice.maskapai-time", null, LocaleContextHolder.getLocale());
        timePrg.add(timeStr).setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(timePrg, plusJakarta));
        helpTbl.addCell(new Cell().add(informationImg).setBorder(Border.NO_BORDER));
        String infoStr = messageSource.getMessage("invoice.maskapai-info", null, LocaleContextHolder.getLocale());
        infoPrg.add(infoStr).setFixedLeading(10);
        helpTbl.addCell(stylePdfService.getInfo(infoPrg, plusJakarta));
        container1.addCell(new Cell().add(helpTbl).setBorder(Border.NO_BORDER));

        //        --SPACE-- 1.5
        container1.addCell(new Cell().add(" ")
                .setBorderBottom(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));


        //        --DESTINATION-- 2
        Table maskapaiTabel = new Table(new float[]{135});
        Table parentDestination = new Table(new float[]{270});
        Table secondLayerparentDestination = new Table(new float[]{270});
        Table kotaArrivalTable = new Table(new float[]{120});
        Table originAirlanes = new Table(new float[]{120});
        Table dstDateArrival = new Table(new float[]{140});
        Table tableTest = new Table(new float[]{135});
        int itinerariesSize = transactionDto.getDetail().get("trips").get(page).get("itineraries").size();

        for (int itineraries = 0; itineraries < itinerariesSize; itineraries++) {
            destinationTabel = new Table(new float[]{130, 10, 130});
            dstDate = new Table(new float[]{140});
            try {
                departureDate = inputDate.parse(transactionDto.getDetail()
                        .get("trips").get(page).get("itineraries").get(itineraries)
                        .get("departureDate").asText());
                dstDate.addCell(stylePdfService.getDestination(outputDate.format(departureDate), plusJakarta, TextAlignment.RIGHT, true));
            } catch (ParseException e) {
                throw new RuntimeException("Date Format Error");
            }

            Date departureTime;
            try {
                departureTime = inputTime.parse(transactionDto.getDetail()
                        .get("trips").get(page).get("itineraries").get(itineraries)
                        .get("departureTime").asText());
                dstDate.addCell(stylePdfService.getDestination(outputTime.format(departureTime), plusJakarta, TextAlignment.RIGHT, true));
            } catch (ParseException e) {
                throw new RuntimeException("Date Format Error");
            }
            destinationTabel.addCell(new Cell().add(dstDate).setBorder(Border.NO_BORDER));
            destinationTabel.addCell(new Cell().add(startPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));

            originAirlanes = new Table(new float[]{120});
            String originCity = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("originAirport").get("city").asText() + " (" + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("originAirport").get("id").asText() + ")";
            originAirlanes.addCell(stylePdfService.getDestination(originCity, plusJakarta, TextAlignment.LEFT, true));
            String departureTerminal = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("originAirport").get("name").asText();
            originAirlanes.addCell(stylePdfService.getDestination(departureTerminal, plusJakarta, TextAlignment.LEFT, false));

            destinationTabel.addCell(new Cell().add(originAirlanes).setBorder(Border.NO_BORDER));

            destinationTabel.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            String timeFlight;
            String departureDateStr = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("departureDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("departureTime").asText();
            String arrivalDateStr = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("arrivalDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("arrivalTime").asText();
            SimpleDateFormat formatDateDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double diff;
            try {
                departureDate = formatDateDestination.parse(departureDateStr);
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
            dstDateArrival = new Table(new float[]{140});
            Date arrivalDate;
            Date arrivalTime;

            try {
                //
                arrivalDate = inputDate.parse(transactionDto.getDetail()
                        .get("trips").get(page).get("itineraries").get(itineraries)
                        .get("arrivalDate").asText());
                dstDateArrival.addCell(stylePdfService.getDestination(outputDate.format(arrivalDate), plusJakarta, TextAlignment.RIGHT, true));

                arrivalTime = inputTime.parse(transactionDto.getDetail()
                        .get("trips").get(page).get("itineraries").get(itineraries)
                        .get("arrivalTime").asText());
                dstDateArrival.addCell(stylePdfService.getDestination(outputTime.format(arrivalTime), plusJakarta, TextAlignment.RIGHT, true));
            } catch (ParseException e) {
                throw new RuntimeException("Date Format Error");
            }


            destinationTabel.addCell(new Cell().add(dstDateArrival).setBorder(Border.NO_BORDER));
            destinationTabel.addCell(new Cell().add(endPointImg.setRelativePosition(5, 0, 0, 0)).setBorder(Border.NO_BORDER));
            kotaArrivalTable = new Table(new float[]{120});
            String destinationCity = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("destinationAirport").get("city").asText() +
                    " (" + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("destinationAirport").get("id").asText() + ")";
            kotaArrivalTable.addCell(stylePdfService.getDestination(destinationCity, plusJakarta, TextAlignment.LEFT, true));

            String destinationNameTerminal = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("destinationAirport").get("name").asText();
            kotaArrivalTable.addCell(stylePdfService.getDestination(destinationNameTerminal, plusJakarta, TextAlignment.LEFT, false));
            destinationTabel.addCell(new Cell().add(kotaArrivalTable).setBorder(Border.NO_BORDER));

            parentDestination = new Table(new float[]{270});
            parentDestination.addCell(new Cell().add(destinationTabel)
                    .setBorder(Border.NO_BORDER)
            );
            if (itineraries == 0) {

            } else {
                parentDestination.addCell(stylePdfService.smallSpaceInColumn());
            }
            String kodebooking = messageSource.getMessage("invoice.kereta-kodeBooking", null, LocaleContextHolder.getLocale());
            String bookingCodeOut = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("pnr").asText();

            if (itineraries == 0 && itinerariesSize > 1) {
                parentDestination.addCell(new Cell().add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                        .setBorder(Border.NO_BORDER)
                        .setFontColor(new DeviceRgb(199, 199, 199))
                        .setBold()
                );
                double transitTime;
                try {
                    Date departureTrans = formatDateDestination.parse(transactionDto.getDetail().get("trips").get(page).get("itineraries").get(1).get("departureDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(1).get("departureTime").asText());
                    Date arrivalDateTrans = formatDateDestination.parse(transactionDto.getDetail().get("trips").get(page).get("itineraries").get(0).get("arrivalDate").asText() + " " + transactionDto.getDetail().get("trips").get(page).get("itineraries").get(0).get("arrivalTime").asText());
                    transitTime =  departureTrans.getTime() - arrivalDateTrans.getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(" date error ");
                }
                double diffMinuteTransit = transitTime / (60 * 1000);
                long durationFlightHourTrans = 0;
                double durationFlightMinuteTrans;
                while (diffMinuteTransit >= 60) {
                    durationFlightHourTrans = durationFlightHourTrans + 1;
                    diffMinuteTransit = diffMinuteTransit - 60;
                }
                durationFlightMinuteTrans = diffMinuteTransit;
                String timeFlightTransit = durationFlightHourTrans + "jam" + durationFlightMinuteTrans + "menit";

                parentDestination.addCell(stylePdfService.getSyaratKetentuan("Transit : " + timeFlightTransit, plusJakarta)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(8)
                        .setRelativePosition(0, 0, 10, 0));

                parentDestination.addCell(new Cell().add("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                        .setBorder(Border.NO_BORDER)
                        .setFontColor(new DeviceRgb(199, 199, 199))
                        .setBold()
                );
            }
            // end Destination

            //        --MASKAPAI RIGHT CORNER-- 3
            maskapaiTabel = new Table(new float[]{135});

            ImageData maskapaiImgData = ImageDataFactory.create(transactionDto.getDetail().get("trips").get(page).get("imageUrl").asText());
            Image maskapaiImg;
            if (itinerariesSize == 1) {
                maskapaiImg = new Image(maskapaiImgData).scaleAbsolute(maskapaiImgData.getWidth(), maskapaiImgData.getHeight());
            } else {
                maskapaiImg = new Image(maskapaiImgData).scaleAbsolute(maskapaiImgData.getWidth() / 2, maskapaiImgData.getHeight() / 2);
            }

            // Space in Transit
            if (itineraries == 0) {
                maskapaiTabel.addCell(new Cell().add(maskapaiImg).setRelativePosition(35, 0, 0, 0).setBorder(Border.NO_BORDER));
            } else {
                maskapaiTabel.addCell(new Cell().add(stylePdfService.getSpacePara(45)).setBorder(Border.NO_BORDER));
                maskapaiTabel.addCell(new Cell().add(maskapaiImg).setBorder(Border.NO_BORDER).setRelativePosition(35, 0, 0, 0));
            }
            // airlane merk
            String airlaneName = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("airlinesName").asText();
            maskapaiTabel.addCell(stylePdfService.eticketInfo(airlaneName, plusJakarta));

            String airlaneNumber = transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("flightNumber").asText();
            maskapaiTabel.addCell(stylePdfService.eticketInfo(airlaneNumber, plusJakarta));

            String[] airlaneClass = {transactionDto.getDetail().get("trips").get(page).get("seatClass").asText(), transactionDto.getDetail().get("trips").get(page).get("itineraries").get(itineraries).get("subclass").asText()};
            String trainClassInfo = "";
            if (airlaneClass[0].equalsIgnoreCase("E")) {
                String economi = messageSource.getMessage("invoice.maskapai-economi", null, LocaleContextHolder.getLocale());
                trainClassInfo = airlaneClass[0] + " " + economi;
            } else if (airlaneClass[0].equalsIgnoreCase("B")) {
                String bisnis = messageSource.getMessage("invoice.maskapai-bisnis", null, LocaleContextHolder.getLocale());
                trainClassInfo = airlaneClass[0] + " " + bisnis;
            } else if (airlaneClass[0].equalsIgnoreCase("F")) {
                String first = messageSource.getMessage("invoice.maskapai-first", null, LocaleContextHolder.getLocale());
                trainClassInfo = airlaneClass[0] + " " + first;
            }
            maskapaiTabel.addCell(stylePdfService.eticketInfo(trainClassInfo, plusJakarta));


            if (itineraries == itinerariesSize - 1) {
                // last itineraries
                if (itinerariesSize == 1) {
                    maskapaiTabel.addCell(new Cell().add(kodebooking).setFont(plusJakarta)
                            .setFontSize(9)
                            .setFontColor(new DeviceRgb(97, 97, 97))
                            .setRelativePosition(5, 0, 0, 0)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.RIGHT)
                    );
                    maskapaiTabel.addCell(new Cell().add(bookingCodeOut).setFont(plusJakarta)
                            .setFontSize(20)
                            .setFontColor(new DeviceRgb(0, 189, 23))
                            .setRelativePosition(5, 0, 0, 10)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setBold());
                } else {
                    maskapaiTabel.addCell(new Cell().add(kodebooking).setFont(plusJakarta)
                            .setFontSize(9)
                            .setFontColor(new DeviceRgb(97, 97, 97))
                            .setRelativePosition(5, 0, 0, 0)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.RIGHT)
                    );
                    maskapaiTabel.addCell(new Cell().add(bookingCodeOut).setFont(plusJakarta)
                            .setFontSize(15)
                            .setFontColor(new DeviceRgb(0, 189, 23))
                            .setRelativePosition(5, 0, 0, 10)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setBold());
                }
                maskapaiTabel.addCell(new Cell().add(airImg).setBorder(Border.NO_BORDER));
            } else {
                maskapaiTabel.addCell(new Cell().add(kodebooking).setFont(plusJakarta)
                        .setFontSize(9)
                        .setFontColor(new DeviceRgb(97, 97, 97))
                        .setRelativePosition(5, 0, 0, 5)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(Border.NO_BORDER)
                );
                maskapaiTabel.addCell(new Cell().add(bookingCodeOut).setFont(plusJakarta)
                        .setFontSize(15)
                        .setFontColor(new DeviceRgb(0, 189, 23))
                        .setRelativePosition(5, 0, 0, 12)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBold());
            }
            secondLayerparentDestination.addCell(new Cell().add(parentDestination).setBorder(Border.NO_BORDER));

            tableTest.addCell(new Cell().add(maskapaiTabel).setBorder(Border.NO_BORDER));

        }
        container1.addCell(new Cell().add(secondLayerparentDestination).setBorder(Border.NO_BORDER));
        container1.addCell(new Cell().add(tableTest).setBorder(Border.NO_BORDER));

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
        Table detailPenumpangTable = new Table(new float[]{100, col, col, col});
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
//            String nomorKursiOutput = transactionDto.getDetail().get("trips").get(page).get("passengers").get(passanger).get("seatNumber").asText();
            String nomorKursiOutput = "-";
            if (nomorKursiOutput.isEmpty() || nomorKursiOutput.isBlank() || nomorKursiOutput == null) {
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

        Paragraph ketentuanPrg = new Paragraph();

        String term = messageSource.getMessage("invoice.maskapai-condition", null, LocaleContextHolder.getLocale());
        String term1 = messageSource.getMessage("invoice.term-condition-airlane1", null, LocaleContextHolder.getLocale());
        String link = " https://holigo.co.id/newspenerbangan";
        String term2 = messageSource.getMessage("invoice.term-condition-airlane2", null, LocaleContextHolder.getLocale());
        String term3 = messageSource.getMessage("invoice.term-condition-airlane3", null, LocaleContextHolder.getLocale());
        String term4 = messageSource.getMessage("invoice.term-condition-airlane4", null, LocaleContextHolder.getLocale());
        String term5 = messageSource.getMessage("invoice.term-condition-airlane5", null, LocaleContextHolder.getLocale());

        ketentuanPrg.setFixedLeading(11);
        ketentuanPrg.add(term1);
        ketentuanPrg.add(link);
        ketentuanPrg.add(term2);
        ketentuanPrg.add(term3);
        ketentuanPrg.add(term4);
        ketentuanPrg.add(term5);

        contrainer3.addCell(stylePdfService.getHeaderTextCell(term, plusJakarta).setFontSize(7).setBorder(Border.NO_BORDER));
        contrainer3.addCell(stylePdfService.getSyaratKetentuanPrg(ketentuanPrg, plusJakarta).setBorder(Border.NO_BORDER));

        return contrainer3;
    }
}
