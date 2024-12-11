package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.RejectPostRequest;
import com.example.peaceful_land.Repository.RequestPostRepository;
import com.example.peaceful_land.Service.IUserRequestService;
import com.example.peaceful_land.Utils.VariableUtils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user-requests")
public class UserRequestController {

    private final RequestPostRepository requestPostRepository;
    private final IUserRequestService userRequestService;
    private final Gson gson;

    @PostMapping("/posts")
    public ResponseEntity<?> getAllPostRequests(@RequestParam String type) {
        return ResponseEntity.ok(userRequestService.getPostRequestBaseOn(type));
    }

    @PostMapping("/post/{id}")
    public ResponseEntity<?> getPostRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(userRequestService.getPostRequestById(id));
    }

    @PostMapping("/post/{id}/action")
    public ResponseEntity<?> doActionToPostRequest(@PathVariable Long id, @RequestParam String type, @RequestBody(required = false) RejectPostRequest request) {
        if (type.equals(VariableUtils.REQUEST_STATE_APPROVED)) {
            userRequestService.approvePostRequest(id);
            return ResponseEntity.ok(gson.toJson("Duyệt bài rao thành công"));
        } else if (type.equals(VariableUtils.REQUEST_STATE_REJECTED)) {
            if (request == null || request.getDenyMessage() == null || request.getDenyMessage().isEmpty()) {
                return ResponseEntity.badRequest().body(gson.toJson("Lý do từ chối không được để trống"));
            }
            userRequestService.rejectPostRequestFromId(id, request.getDenyMessage());
            return ResponseEntity.ok(gson.toJson("Từ chối bài rao thành công"));
        } else {
            return ResponseEntity.badRequest().body(gson.toJson("Không hợp lệ"));
        }
    }

    @PostMapping("/withdraws")
    public ResponseEntity<?> getAllWithdrawsRequests(@RequestParam String type) {
        return ResponseEntity.ok(userRequestService.getWithdrawRequestBaseOn(type));
    }

    @PostMapping("/withdraw/{id}/action")
    public ResponseEntity<?> doActionToWithdrawRequest(@PathVariable Long id, @RequestParam String type, @RequestBody(required = false) RejectPostRequest request) {
        if (type.equals(VariableUtils.REQUEST_STATE_APPROVED)) {
            userRequestService.approveOrRejectWithdrawRequest(id, true, null);
            return ResponseEntity.ok(gson.toJson("Duyệt yêu cầu rút tiền thành công"));
        } else if (type.equals(VariableUtils.REQUEST_STATE_REJECTED)) {
            if (request == null || request.getDenyMessage() == null || request.getDenyMessage().isEmpty()) {
                return ResponseEntity.badRequest().body(gson.toJson("Lý do từ chối không được để trống"));
            }
            userRequestService.approveOrRejectWithdrawRequest(id, false, request.getDenyMessage());
            return ResponseEntity.ok(gson.toJson("Từ chối yêu cầu rút tiền thành công"));
        } else {
            return ResponseEntity.badRequest().body(gson.toJson("Hành động không hợp lệ"));
        }
    }


}