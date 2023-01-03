package id.holigo.services.holigoinvoiceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HoligoInvoiceServiceApplication {

	public static void main(String[] args){
		SpringApplication.run(HoligoInvoiceServiceApplication.class, args);
	}


}
