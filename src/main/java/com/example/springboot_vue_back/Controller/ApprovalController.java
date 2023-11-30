package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.domain.Needhelp;
import com.example.springboot_vue_back.req.UserQueryReq;
import com.example.springboot_vue_back.resp.ApprovalResp;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.resp.UserQueryResp;
import com.example.springboot_vue_back.service.NeedhelpService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/29 16:43
 **/
@RestController
@RequestMapping("/approval")
public class ApprovalController {
    @Resource
    private NeedhelpService needhelpService;

    @GetMapping("/list")
    public ComminResp list() {
        ComminResp<List<ApprovalResp>> objectComminResp = new ComminResp<>();
       List<ApprovalResp> list = needhelpService.selectall();
        objectComminResp.setContent(list);
        return objectComminResp;
    }
    @GetMapping("/select/{email}")
    public ComminResp select(@PathVariable String email) {
        System.out.println("email:----------------------------"+email);
        ComminResp<List<ApprovalResp>> objectComminResp = new ComminResp<>();
        List<ApprovalResp> list = needhelpService.select(email);
        objectComminResp.setContent(list);
        return objectComminResp;
    }
    @DeleteMapping("/delete/{id}")
    public ComminResp delete(@PathVariable long id) {
        ComminResp objectComminResp = new ComminResp<>();
        needhelpService.delete(id);
        return objectComminResp;
    }
    @GetMapping("/selectallOrderByOptionAscApproval/{option}")
    public ComminResp selectallOrderByOptionAscApproval(@PathVariable boolean option) {
        ComminResp<List<ApprovalResp>> objectComminResp = new ComminResp<>();
        List<ApprovalResp> list = needhelpService.selectallOrderByOptionAscApproval(option);
        objectComminResp.setContent(list);
        return objectComminResp;
    }
    @GetMapping("/approvaled/{id}")
    public ComminResp approvaled(@PathVariable long id) {

        ComminResp approvaled = needhelpService.approvaled(id);
        return approvaled;
    }
}
