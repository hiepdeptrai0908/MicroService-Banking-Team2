package org.training.fundtransfer.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.training.fundtransfer.exception.AccountUpdateException;
import org.training.fundtransfer.exception.GlobalErrorCode;
import org.training.fundtransfer.exception.InsufficientBalance;
import org.training.fundtransfer.exception.ResourceNotFound;
import org.training.fundtransfer.external.AccountService;
import org.training.fundtransfer.external.TransactionService;
import org.training.fundtransfer.model.mapper.FundTransferMapper;
import org.training.fundtransfer.model.TransactionStatus;
import org.training.fundtransfer.model.TransferType;
import org.training.fundtransfer.model.dto.Account;
import org.training.fundtransfer.model.dto.FundTransferDto;
import org.training.fundtransfer.model.dto.Transaction;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;
import org.training.fundtransfer.model.entity.FundTransfer;
import org.training.fundtransfer.repository.FundTransferRepository;
import org.training.fundtransfer.service.FundTransferService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundTransferServiceImpl implements FundTransferService {

    private final AccountService accountService;
    private final FundTransferRepository fundTransferRepository;
    private final TransactionService transactionService;

    @Value("${spring.application.ok}")
    private String ok;

    private final FundTransferMapper fundTransferMapper = new FundTransferMapper();

    /**
     * Chuyển tiền từ một tài khoản sang tài khoản khác.
     *
     * @param fundTransferRequest Đối tượng yêu cầu chứa thông tin chi tiết về việc chuyển tiền.
     * @return Đối tượng phản hồi cho biết trạng thái của việc chuyển tiền.
     * @throws ResourceNotFound Nếu tài khoản yêu cầu không được tìm thấy trên máy chủ.
     * @throws AccountUpdateException Nếu trạng thái tài khoản là đang chờ xử lý hoặc không hoạt động.
     * @throws InsufficientBalance Nếu số dư cần thiết để chuyển không đủ.
     */
    @Override
    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) {

        Account fromAccount;

        ResponseEntity<Account> response = accountService.readByAccountNumber(fundTransferRequest.getFromAccount());
        if(Objects.isNull(response.getBody())){
            log.error("Tài khoản yêu cầu "+fundTransferRequest.getFromAccount()+" không được tìm thấy trên máy chủ");
            throw new ResourceNotFound("Tài khoản yêu cầu không được tìm thấy trên máy chủ", GlobalErrorCode.NOT_FOUND);
        }
        fromAccount = response.getBody();
        if (!fromAccount.getAccountStatus().equals("ACTIVE")) {
            log.error("Trạng thái tài khoản là đang chờ xử lý hoặc không hoạt động, vui lòng cập nhật trạng thái tài khoản");
            throw new AccountUpdateException("Trạng thái tài khoản là: đang chờ xử lý", GlobalErrorCode.NOT_ACCEPTABLE);
        }
        if (fromAccount.getAvailableBalance().compareTo(fundTransferRequest.getAmount()) < 0) {
            log.error("Số tiền cần chuyển không đủ");
            throw new InsufficientBalance("Số tiền yêu cầu không có sẵn", GlobalErrorCode.NOT_ACCEPTABLE);
        }
        Account toAccount;
        response = accountService.readByAccountNumber(fundTransferRequest.getToAccount());
        if(Objects.isNull(response.getBody())) {
            log.error("Tài khoản yêu cầu "+fundTransferRequest.getToAccount()+" không được tìm thấy trên máy chủ");
            throw new ResourceNotFound("Tài khoản yêu cầu không được tìm thấy trên máy chủ", GlobalErrorCode.NOT_FOUND);
        }
        toAccount = response.getBody();
        String transactionId = internalTransfer(fromAccount, toAccount, fundTransferRequest.getAmount());
        FundTransfer fundTransfer = FundTransfer.builder()
                .transferType(TransferType.INTERNAL)
                .amount(fundTransferRequest.getAmount())
                .fromAccount(fromAccount.getAccountNumber())
                .transactionReference(transactionId)
                .status(TransactionStatus.SUCCESS)
                .toAccount(toAccount.getAccountNumber()).build();

        fundTransferRepository.save(fundTransfer);
        return FundTransferResponse.builder()
                .transactionId(transactionId)
                .message("Chuyển tiền thành công").build();
    }

    /**
     * Chuyển tiền từ một tài khoản sang tài khoản khác trong hệ thống.
     *
     * @param fromAccount Tài khoản chuyển tiền.
     * @param toAccount Tài khoản nhận tiền.
     * @param amount Số tiền chuyển.
     * @return Số tham chiếu giao dịch.
     */
    private String internalTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {

        fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().subtract(amount));
        accountService.updateAccount(fromAccount.getAccountNumber(), fromAccount);

        toAccount.setAvailableBalance(toAccount.getAvailableBalance().add(amount));
        accountService.updateAccount(toAccount.getAccountNumber(), toAccount);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .accountId(fromAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount.negate())
                        .description("Chuyển tiền nội bộ từ "+fromAccount.getAccountNumber()+" đến "+toAccount.getAccountNumber())
                        .build(),
                Transaction.builder()
                        .accountId(toAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount)
                        .description("Chuyển tiền nội bộ nhận từ: "+fromAccount.getAccountNumber()).build());

        String transactionReference = UUID.randomUUID().toString();
        transactionService.makeInternalTransactions(transactions, transactionReference);
        return transactionReference;
    }

    /**
     * Lấy thông tin chi tiết của giao dịch chuyển tiền dựa trên ID tham chiếu đã cho.
     *
     * @param referenceId ID tham chiếu của giao dịch chuyển tiền.
     * @return Đối tượng FundTransferDto chứa thông tin chi tiết của giao dịch chuyển tiền.
     * @throws ResourceNotFound Nếu giao dịch chuyển tiền không được tìm thấy.
     */
    @Override
    public FundTransferDto getTransferDetailsFromReferenceId(String referenceId) {

        return fundTransferRepository.findFundTransferByTransactionReference(referenceId)
                .map(fundTransferMapper::convertToDto)
                .orElseThrow(() -> new ResourceNotFound("Giao dịch chuyển tiền không được tìm thấy", GlobalErrorCode.NOT_FOUND));
    }

    /**
     * Lấy danh sách các giao dịch chuyển tiền liên quan đến ID tài khoản đã cho.
     *
     * @param accountId ID của tài khoản
     * @return Danh sách các đối tượng FundTransferDto
     */
    @Override
    public List<FundTransferDto> getAllTransfersByAccountId(String accountId) {

        return fundTransferMapper.convertToDtoList(fundTransferRepository.findFundTransferByFromAccount(accountId));
    }
}
