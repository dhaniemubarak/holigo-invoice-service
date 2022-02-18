package id.holigo.services.holigoinvoiceservice.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    private static final String PDF_RESOURCES = "static/";

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public File generatePdf(TransactionDto transactionDto, String template) throws IOException, DocumentException {
        Context context = getContext(transactionDto);

        String html = loadAndFillTemplate(template, context);
        return renderPdf(html);
    }

    private Context getContext(TransactionDto transactionDto) {
        Context context = new Context();
        context.setVariable("transactionDto", transactionDto);
        context.setVariable("media", "print");
        return context;
    }

    private String loadAndFillTemplate(String template, Context context) {
        return templateEngine.process(template, context);
    }

    private File renderPdf(String html) throws IOException, DocumentException {
        File file = File.createTempFile("receipt", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        String resources = new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm();
        log.info("resources -> {}", resources);
        renderer.setDocumentFromString(html, resources);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

}
