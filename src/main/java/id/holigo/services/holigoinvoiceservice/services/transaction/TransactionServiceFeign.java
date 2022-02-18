package id.holigo.services.holigoinvoiceservice.services.transaction;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceFeign implements TransactionService {

    private final TransactionServiceFeignClient transactionServiceFeignClient;

    @Override
    public TransactionDto getTransactionDetail(UUID id) {
        log.info("Calling transaction service with id : -> {}", id);

        ResponseEntity<TransactionDto> responseEntity = transactionServiceFeignClient.getTransactionDetail(id);

        log.info("responseEntity body -> {}", responseEntity.getBody());

        return responseEntity.getBody();
    }

}
