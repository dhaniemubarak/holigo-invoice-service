package id.holigo.services.holigoinvoiceservice;

import id.holigo.services.holigoinvoiceservice.services.PdfAirlineServiceImpl;
import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@EnableFeignClients
@SpringBootApplication
public class HoligoInvoiceServiceApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(HoligoInvoiceServiceApplication.class, args);
	}


}
