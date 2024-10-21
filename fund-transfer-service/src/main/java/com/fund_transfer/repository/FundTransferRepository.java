package com.fund_transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fund_transfer.model.entity.FundTransfer;

import java.util.List;
import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {

    /**
     * Tìm giao dịch chuyển tiền theo tham chiếu giao dịch.
     *
     * @param referenceId ID tham chiếu giao dịch
     * @return Một đối tượng giao dịch chuyển tiền tùy chọn
     */
    Optional<FundTransfer> findFundTransferByTransactionReference(String referenceId);

    /**
     * Lấy danh sách các đối tượng FundTransfer dựa trên ID tài khoản gửi.
     *
     * @param accountId ID của tài khoản gửi.
     * @return Danh sách các đối tượng FundTransfer.
     */
    List<FundTransfer> findFundTransferByFromAccount(String accountId);
}
