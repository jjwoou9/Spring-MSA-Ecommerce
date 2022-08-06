package com.amp.userservice.config.oauth;

import com.amp.userservice.config.auth.PrincipalDetails;
import com.amp.userservice.config.provider.FaceBookUserInfo;
import com.amp.userservice.config.provider.GoogleUserInfo;
import com.amp.userservice.config.provider.NaverUserInfo;
import com.amp.userservice.config.provider.OAuth2UserInfo;
import com.amp.userservice.jpa.UserRepository;
import com.amp.userservice.model.User;
import com.amp.userservice.model.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    UserRepository userRepository;

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code리턴 (OAuth-Client라이브러리) -> Access Token요청 (이게 userRequest 정보)
        //userRequest 정보 - > 회원프로필 laodUser()를 통해 구글로부터  회원 프로필 받음.

        //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

        // code를 통해 구성한 정보
        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);
        System.out.println("getAttribute : " + oAuth2User.getAttributes());

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청~~");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청~~");
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청~~");
            //네이버  카카오 같은 한국 포털서비스는 구글/트위터/페북 같은 해외랑 다르게 yml로 별도로 provicer를 설정해줘야한다.
            //그리고 설정한 user-name-attribute 키값으로 getAttribute에서 get으로 받아와야한다.
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎ");
        }

        //System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
        //System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
        Optional<User> userOptional =
                userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = User.builder() //builder annotation
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .role(Role.ROLE_USER)
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
