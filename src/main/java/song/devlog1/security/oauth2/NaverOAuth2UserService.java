package song.devlog1.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import song.devlog1.entity.User;
import song.devlog1.entity.role.RoleName;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.security.oauth2.userinfo.UserInfo;
import song.devlog1.security.oauth2.userinfo.NaverUserInfo;

import java.util.Map;
import java.util.Optional;

@Slf4j
//@Service
//@RequiredArgsConstructor
//public class NaverOAuth2UserService extends DefaultOAuth2UserService {
public class NaverOAuth2UserService {

//    private final UserJpaRepository userRepository;

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        UserInfo userInfo = null;
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//
//        if ("naver".equals(registrationId)) {
//            userInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
//        } else {
//            log.info("OAuth 로그인 실패");
//        }
//
//        String provider = userInfo.getProvider();
//        String providerId = userInfo.getProviderId();
//        String username = provider + "_" + providerId;
//
//        String sex = userInfo.getSex();
//        String role = RoleName.ROLE_USER.name();
//        log.info("{}", oAuth2User.getAttributes());
//
//        User user = null;
//        Optional<User> findUser = userRepository.findByUsername(username);
//        if (findUser.isEmpty()) {
////            user=
//        }
//
//        return null;
//    }
}
