package com.example.peaceful_land.Controller;

import com.example.peaceful_land.Service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/payments")
@Controller
public class VnPayController {
    @Autowired
    private VnPayService vnPayService;


    @GetMapping("")
    public String home(){
        return "index";
    }


    @PostMapping("/vnpay")
    public ResponseEntity<Map<String, String>> submidOrder(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            @RequestParam("ticketId") Long ticketId,
            HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + "4200";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, ticketId);

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

        // Tách userId từ vnp_TxnRef (giả sử vnp_TxnRef có dạng "userId-transactionId")
        String userId = vnp_TxnRef.split("-")[0];

        // Xử lý theo kết quả trả về của cổng thanh toán
        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công, trả về ResponseEntity với thông báo thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thanh toán thành công!");
            response.put("userId", userId);
            response.put("orderId", orderInfo);
            response.put("totalPrice", totalPrice);
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