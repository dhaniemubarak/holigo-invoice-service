package id.holigo.services.holigoinvoiceservice.services.transaction;

import java.util.UUID;

import id.holigo.services.holigoinvoiceservice.web.model.TransactionDto;

public interface TransactionService {
    TransactionDto getTransactionDetail(UUID id);
}
