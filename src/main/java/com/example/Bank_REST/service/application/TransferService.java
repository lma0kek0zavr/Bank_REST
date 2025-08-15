package com.example.Bank_REST.service.application;

import com.example.Bank_REST.dto.TransferDto;
import com.example.Bank_REST.util.request.TransferRequest;

public interface TransferService {
    TransferDto transfer(TransferRequest transferRequest);
}
