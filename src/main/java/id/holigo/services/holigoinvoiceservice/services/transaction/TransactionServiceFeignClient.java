package id.holigo.services.holigoinvoiceservice.services.transaction;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

@FeignClient(name = "holigo-transaction-service")
public interface TransactionServiceFeignClient {

    String TRANSACTION_DETAIL_BY_ID = "/api/v1/transactions/{id}";

    @RequestMapping(method = RequestMethod.GET, value = TRANSACTION_DETAIL_BY_ID)
    ResponseEntity<TransactionDto> getTransactionDetail(@PathVariable UUID id);
}
