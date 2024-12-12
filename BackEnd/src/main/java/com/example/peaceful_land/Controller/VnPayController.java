package com.example.peaceful_land.Controller;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.PaymentMethod;
import com.example.peaceful_land.Entity.Transaction;
import com.example.peaceful_land.Exception.UserNotFoundException;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PaymentMethodRepository;
import com.example.peaceful_land.Repository.TransactionRepository;
import com.example.peaceful_land.Service.payment.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/payments")
@Controller
public class VnPayController {
    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;


    @GetMapping("")
    public String home(){
        return "index";
    }


    @PostMapping("/vnpay")
    public ResponseEntity<Map<String, String>> submidOrder(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            @RequestParam("userId") Long userId,
            HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + "4200";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, userId);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", vnpayUrl);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/afterPayed")
    public ResponseEntity<Object> afterPayment(HttpServletRequest request) throws Exception {
        // Lấy kết quả thanh toán từ dịch vụ (có thể trả về trạng thái giao dịch)
        int paymentStatus = vnPayService.orderReturn(request);


        // Lấy các tham số trả về từ cổng thanh toán (VNPAY)
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_BankTranNo = request.getParameter("vnp_BankTranNo");
        String vnp_CardType = request.getParameter("vnp_CardType");

        // Tách userId từ vnp_TxnRef (giả sử vnp_TxnRef có dạng "userId-transactionId")
        String userId = vnp_TxnRef.split("-")[0];
        totalPrice = totalPrice.substring(0,totalPrice.length()-2);


        // Lấy đối tượng người dùng
        Account account = accountRepository.findById(Long.parseLong(userId))
                .orElseThrow(UserNotFoundException::new);

        // Lưu thông tin giao dịch vào cơ sở dữ liệu
        Transaction trans = Transaction.builder()
                .id(Long.parseLong(transactionId))
                .account(account)
                .money(Long.parseLong(totalPrice))
                .transactionDescription(orderInfo)
                .transactionInformation("BankCode: " + vnp_BankCode + ", BankTranNo: " + vnp_BankTranNo + ", CardType: " + vnp_CardType)
                .status((byte) paymentStatus)
                .build();
        trans = transactionRepository.save(trans);
        // Lưu thời gian thanh toán
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime paymentLocalDateTime = LocalDateTime.parse(paymentTime, formatter);
        trans.setDateBegin(paymentLocalDateTime);
        transactionRepository.save(trans);

        // Thêm phương thức thanh toán nếu chưa tồn tại
        if (!paymentMethodRepository.existsByAccountEqualsAndNameEqualsAndAccountNumberEqualsAndHideEquals(
                account, vnp_BankCode, account.getPhone(), false)) {
            PaymentMethod paymentMethod = PaymentMethod.builder()
                    .account(account)
                    .name(vnp_BankCode)
                    .accountNumber(account.getPhone())
                    .isWallet(true)
                    .build();
            paymentMethodRepository.save(paymentMethod);
        }

        // Xử lý theo kết quả trả về của cổng thanh toán
        if ("00".equals(vnp_ResponseCode)) {

            // Thêm tiền vào tài khoản người dùng nếu thanh toán thành công
            account.setAccountBalance(account.getAccountBalance() + Long.parseLong(totalPrice));
            accountRepository.save(account);

            // Thanh toán thành công, trả về ResponseEntity với thông báo thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thanh toán thành công!");
            response.put("userId", userId);
            response.put("orderId", orderInfo);
            response.put("totalPrice",  totalPrice);
            response.put("paymentTime", paymentTime);
            response.put("transactionId", transactionId);

            // Trả về mã trạng thái 200 OK và dữ liệu JSON
            return ResponseEntity.ok(response);
        } else {
            // Thanh toán thất bại, ném ngoại lệ và trả về lỗi với mã 400
            throw new RuntimeException("Thanh toán thất bại với mã phản hồi: " + vnp_ResponseCode);
        }
    }

}