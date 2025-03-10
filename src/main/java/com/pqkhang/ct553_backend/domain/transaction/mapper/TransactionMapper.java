package com.pqkhang.ct553_backend.domain.transaction.mapper;

import com.pqkhang.ct553_backend.domain.transaction.dto.TransactionDTO;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toTransactionDTO(Transaction transaction);

    Transaction toTransaction(TransactionDTO transactionDTO);
//    void updateTransactionFromDTO(@MappingTarget Transaction transaction, TransactionDTO transactionDTO);
}
