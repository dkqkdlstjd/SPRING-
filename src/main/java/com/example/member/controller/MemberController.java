package com.example.member.controller;


import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        System.out.println("memberDTO = " + memberDTO);
        System.out.println("MemberController.save");
        memberService.save(memberDTO);
        return "login";
    }

    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult!=null){
            //login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        }else{
            //login 실패
            return "login";
        }
    }

    @GetMapping("/member/")
    public String findALL(Model model){
        List<MemberDTO> memberDIOList = memberService.findAll();
        //어떤 html로 가져살 데이터가 있다면 model사용
        model.addAttribute("memberList",memberDIOList);
        return "list";
    }

    //@PathVariable 경로상에 있는 값을 가져온다
    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDTO memberDTO=memberService.findById(id);
        model.addAttribute("member",memberDTO);
        System.out.println("id = " + id + ", model = " + model);
        return "detail";
    }
    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO=memberService.updateForm(myEmail);
        model.addAttribute("updateMember",memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/"+ memberDTO.getId();
    }
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id){
        memberService.deleteById(id);
        return "redirect:/member/";

    }
    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }
    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail){
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailChcek(memberEmail);
        if(checkResult!=null){
            return "ok";
        }else{
            return "no";
        }
    }
 }

