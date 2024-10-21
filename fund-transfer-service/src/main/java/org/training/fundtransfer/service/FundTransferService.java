package org.training.fundtransfer.service;

import org.training.fundtransfer.model.dto.FundTransferDto;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;

import java.util.List;

public interface FundTransferService {

    /**
     * Chuyển tiền từ một tài khoản sang tài khoản khác.
     *
     * @param fundTransferRequest Đối tượng yêu cầu chứa thông tin chi tiết về việc chuyển tiền.
     * @return Đối tượng phản hồi chứa kết quả của việc chuyển tiền.
     */
    FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest);

    /**
     * Lấy thông tin chi tiết của giao dịch chuyển tiền dựa trên ID tham chiếu đã cho.
     *
     * @param referenceId ID tham chiếu của giao dịch.
     * @return Thông tin giao dịch dưới dạng đối tượng FundTransferDto.
     */
    FundTransferDto getTransferDetailsFromReferenceId(String referenceId);

    /**
     * Lấy tất cả các giao dịch chuyển tiền liên quan đến ID tài khoản đã cho.
     *
     * @param accountId ID của tài khoản
     * @return Danh sách các đối tượng FundTransferDto đại diện cho các giao dịch chuyển tiền
     */
    List<FundTransferDto> getAllTransfersByAccountId(String accountId);
}
