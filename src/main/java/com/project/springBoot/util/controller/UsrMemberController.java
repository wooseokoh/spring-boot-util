package com.project.springBoot.util.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.springBoot.util.dto.Member;
import com.project.springBoot.util.dto.ResultData;
import com.project.springBoot.util.dto.api.KapiKakaoCom__v2_user_me__ResponseBody;
import com.project.springBoot.util.service.KakaoRestLoginService;
import com.project.springBoot.util.service.MemberService;

@Controller
public class UsrMemberController extends BaseController {
	@Value("${custom.kakaoRest.apiKey}")
	private String kakaoRestApiKey;

	@Autowired
	private MemberService memberService;

	@Autowired
	private KakaoRestLoginService kakaoRestLoginService;

	@GetMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@GetMapping("/usr/member/goKakaoLoginPage")
	public String goKakaoLoginPage() {

		String url = kakaoRestLoginService.getKakaoLoginPageUrl();

		return "redirect:" + url;
	}

	@GetMapping("/usr/member/doLoginByKakoRest")
	public String doLoginByKakoRest(HttpServletRequest req, HttpSession session, String code) {
		KapiKakaoCom__v2_user_me__ResponseBody kakaoUser = kakaoRestLoginService.getKakaoUserByAuthorizeCode(code);

		Member member = memberService.getMemberByOnLoginProviderMemberId("kakaoRest", kakaoUser.id);

		ResultData rd = null;

		if (member != null) {
			rd = memberService.updateMember(member, kakaoUser);
		} else {
			rd = memberService.join(kakaoUser);
		}

		int id = (int) rd.getBody().get("id");

		session.setAttribute("loginedMemberId", id);

		return msgAndReplace(req, "카카오톡 계정으로 로그인하였습니다.", "../home/main");
	}

	@GetMapping("/usr/member/doLogout")
	public String doLogout(HttpServletRequest req, HttpSession session) {
		int id = -1;
		if (session.getAttribute("loginedMemberId") != null) {
			id = (int) session.getAttribute("loginedMemberId");
			session.removeAttribute("loginedMemberId");
		}

		return msgAndReplace(req, "로그아웃 되었습니다.", "../home/main");
	}
}