package org.example.atm.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.enums.TransactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private Long senderId;
    private Long receiverId;
    private Double amount;
    private TransactionType transactionType;
}