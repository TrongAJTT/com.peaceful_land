package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.*;
import com.example.peaceful_land.Exception.RequestNotFoundException;
import com.example.peaceful_land.Repository.*;
import com.example.peaceful_land.Service.repos.IEmailService;
import com.example.peaceful_land.Service.repos.IUserRequestService;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service @RequiredArgsConstructor
public class UserRequestService implements IUserRequestService {

    private final PropertyRepository propertyRepository;
    private final RequestWithdrawRepository requestWithdrawRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PostRepository postRepository;
    private final RequestPostRepository requestPostRepository;
    private final UserInterestRepository userInterestRepository;
    private final IEmailService emailService;
    private final AccountRepository accountRepository;
    private final PurchaseRepository purchaseRepository;
    private final RequestReportRepository requestReportRepository;

    @Override
    public List<PostApprovalResponse> getPostRequestBaseOn(String requestState) {
        List<RequestPost> postRequests = switch(requestState){
            case VariableUtils.REQUEST_STATE_ALL -> requestPostRepository.findAllByOrderByIdDesc();
            case VariableUtils.REQUEST_STATE_PENDING -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(null);
            case VariableUtils.REQUEST_STATE_APPROVED -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(true);
            case VariableUtils.REQUEST_STATE_REJECTED -> requestPostRepository.findAllByApprovedEqualsOrderByExpirationDesc(false);
            default -> throw new IllegalStateException("Trạng thái yêu cầu không hợp lệ");
        };
        return postRequests.stream()
                .map(RequestPost::toPostApprovalResponse)
                .toList();
    }

    @Override
    public ResponseUserPostReqView getPostRequestById(Long id) {
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        return postRequest.toResponseUserPostReqView();
    }

    @Override
    public void approvePostRequest(Long id) {
        // Lấy ra yêu cầu
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        // Bài rao đã được duyệt
        if (postRequest.getApproved() != null){
            throw new IllegalArgumentException("Yêu cầu đã được xử lý");
        }
        // Thay đổi trạng thái yêu cầu và lưu vào database
        postRequest.setApproved(true);
        requestPostRepository.save(postRequest);
        // Thay đổi trạng thái bài rao và lưu vào database nếu có
        Post approvedPost = postRequest.getPost();
        if (approvedPost.getHide()){
            approvedPost.setHide(false);
            postRepository.save(approvedPost);
            // Thay đổi trạng thái bất động sản và lưu vào database
            Property property = approvedPost.getProperty();
            property.setHide(false);
            propertyRepository.save(property);
        }
        // Gửi email thông báo cho người đăng bài
        new Thread(() ->
            emailService.sendPostApprovedEmailToOwner(
                    approvedPost.getProperty().getUser().getEmail(),
                    approvedPost.getId(),
                    approvedPost.getDateBegin()
            )
        ).start();
        // Gửi email đến người quan tâm
        List<UserInterest> listUserInterested = userInterestRepository.findByPropertyEqualsAndNotificationEquals(approvedPost.getProperty(), true);
        if (!listUserInterested.isEmpty()){
            new Thread(() -> {
                for(UserInterest userInterest : listUserInterested) {
                    emailService.sendPostApprovedEmailToWhoInterested(
                            userInterest.getUser().getEmail(),
                            approvedPost.getId(),
                            userInterest.getDateBegin()
                    );
                }
            }).start();
        }
    }

    @Override
    public void rejectPostRequestFromId(Long id, String denyMessage) {
        // Lấy ra yêu cầu
        RequestPost postRequest = requestPostRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        rejectPostRequest(postRequest, denyMessage);
    }

    private void rejectPostRequest(RequestPost request, String denyMessage) {
        // Thay đổi trạng thái yêu cầu và lưu vào database
        request.setApproved(false);
        request.setDenyMessage(denyMessage);
        requestPostRepository.save(request);
        // Thay đổi trạng thái bài rao và lưu vào database nếu có
        Post approvedPost = request.getPost();
        if (!request.getHide()){
            approvedPost.setHide(true);
            postRepository.save(approvedPost);
            // Thay đổi trạng thái bất động sản và lưu vào database
            Property property = approvedPost.getProperty();
            property.setHide(true);
            propertyRepository.save(property);
        }
        // Gửi email thông báo cho người đăng bài
        new Thread(() ->
                emailService.sendPostRejectedEmailToOwner(
                        approvedPost.getProperty().getUser().getEmail(),
                        approvedPost.getId(),
                        approvedPost.getDateBegin(),
                        denyMessage
                )
        ).start();
    }

    @Override
    public List<ResponseWithdrawRequest> getWithdrawRequestBaseOn(String requestState) {
        List<RequestWithdraw> withdrawRequests = switch(requestState){
            case VariableUtils.REQUEST_STATE_ALL -> requestWithdrawRepository.findAllByOrderByDateBeginDesc();
            case VariableUtils.REQUEST_STATE_PENDING -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_PENDING);
            case VariableUtils.REQUEST_STATE_APPROVED -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_APPROVED);
            case VariableUtils.REQUEST_STATE_REJECTED -> requestWithdrawRepository.findAllByStatusEqualsOrderByDateBeginDesc(RequestWithdraw.STATUS_REJECTED);
            default -> throw new IllegalStateException("Trạng thái yêu cầu không hợp lệ");
        };
        return withdrawRequests.stream()
                .map(RequestWithdraw::toResponseWithdrawRequest)
                .toList();
    }

    @Override
    public void approveOrRejectWithdrawRequest(Long id, Boolean isApprove, String denyMessageIfFalse) {
        // Lấy ra yêu cầu
        RequestWithdraw withdrawRequest = requestWithdrawRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        if (withdrawRequest.getStatus() != RequestWithdraw.STATUS_PENDING){
            throw new IllegalArgumentException("Yêu cầu đã được xử lý");
        }
        // Thay đổi trạng thái yêu cầu và số dư tài khoản (nếu có) và lưu vào database
        Account account = withdrawRequest.getAccount();
        if (isApprove){
            withdrawRequest.setStatus(RequestWithdraw.STATUS_APPROVED);
            // Trừ tiền và lưu vào database
            account.setAccountBalance(account.getAccountBalance() - withdrawRequest.getAmount() - 3000);
            accountRepository.save(account);
            // Lưu thanh toán
            purchaseRepository.save(
                    Purchase.builder()
                    .user(account)
                    .amount(withdrawRequest.getAmount())
                    .action(VariableUtils.PURCHASE_ACTION_WITHDRAW)
                    .build()
            );
        }
        else {
            withdrawRequest.setStatus(RequestWithdraw.STATUS_REJECTED);
            withdrawRequest.setResultMessage(denyMessageIfFalse);
        }
        requestWithdrawRepository.save(withdrawRequest);
        // Gửi email thông báo cho người yêu cầu
        new Thread(() ->
            emailService.sendWithdrawResponse(
                    account.getEmail(),
                    withdrawRequest.getId(),
                    withdrawRequest.getDateBegin(),
                    withdrawRequest.getAmount(),
                    withdrawRequest.getPayment(),
                    isApprove, isApprove ? null : denyMessageIfFalse)
        ).start();
    }

    public void SYSTEM_scanForExpiredPostRequests() {
        new Thread(() -> {
            List<RequestPost> listRequestPost = requestPostRepository.findAllByExpirationBeforeAndApproved(LocalDate.now(), null);
            for (RequestPost requestPost : listRequestPost) {
                rejectPostRequest(requestPost, "Hết hạn yêu cầu nhưng quản trị viên vẫn chưa xử lý");
            }
            String message = ">>>\n" + VariableUtils.getServerStatPrefix() + ( listRequestPost.isEmpty() ?
                    "No post request expired" :
                    "Automatically rejected these post request " + Arrays.toString(listRequestPost.stream()
                            .map(p -> p.getPost().getId()).toArray()) + " due to expiration"
            ) + "\n<<<";
            System.out.println(message);
        }).start();
    }

    @Override
    public List<ResponseReport> getReportRequestBaseOn(String requestState) {
        List<RequestReport> requestReports = switch(requestState){
            case VariableUtils.REQUEST_STATE_ALL -> requestReportRepository.findAllByOrderByIdDesc();
            case VariableUtils.REQUEST_STATE_PENDING -> requestReportRepository.findAllByHideEqualsOrderByIdDesc(false);
            case VariableUtils.REQUEST_STATE_HANDLED -> requestReportRepository.findAllByHideEqualsOrderByIdDesc(true);
            default -> throw new IllegalStateException("Trạng thái yêu cầu không hợp lệ");
        };
        return requestReports.stream()
                .map(RequestReport::toLiteResponseReport)
                .toList();
    }

    @Override
    public ResponseReport getReportDetailInfoId(Long id) {
        RequestReport requestReport = requestReportRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        return requestReport.toResponseReport();
    }

    @Override
    public void handleReport(Long id, String replyMessage) {
        // Lấy ra yêu cầu
        RequestReport requestReport = requestReportRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        if (requestReport.getHide()){
            throw new IllegalArgumentException("Yêu cầu đã được xử lý");
        }
        // Thay đổi trạng thái yêu cầu và lưu vào database
        requestReport.setHide(true);
        requestReportRepository.save(requestReport);
        // Gửi email thông báo cho người yêu cầu nếu có
        if (replyMessage != null && !replyMessage.isEmpty()){
            new Thread(() ->
                emailService.sendRequestHandledEmail(requestReport, replyMessage)
            ).start();
        }
    }
}
