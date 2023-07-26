package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.EmailVerificationToken;
import song.devlog1.entity.User;
import song.devlog1.exception.alreadyexist.AlreadyExpiredEmailVerificationToken;
import song.devlog1.exception.alreadyexist.AlreadyVerifiedEmailVerificationTokenException;
import song.devlog1.exception.invalid.InvalidTokenException;
import song.devlog1.exception.notfound.EmailVerificationTokenNotFoundException;
import song.devlog1.repository.EmailVerificationJpaRepository;
import song.devlog1.repository.UserJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationJpaRepository emailVerificationRepository;
    private final UserJpaRepository userRepository;

    @Transactional
    public String createEmailVerificationToken(String email) {

        isAlreadyVerified(email);

        Optional<EmailVerificationToken> findToken = emailVerificationRepository.findByEmail(email);
        if (findToken.isPresent()) {
            EmailVerificationToken token = findToken.get();
            token.setToken(getUUIDToken());
            return token.getToken();
        }

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(email);
        emailVerificationToken.setToken(getUUIDToken());
        emailVerificationToken.setVerified(false);
        EmailVerificationToken saveToken = emailVerificationRepository.save(emailVerificationToken);

        return saveToken.getToken();
    }

    @Transactional
    public Long verifyEmailVerificationToken(String email, String token) {
        Optional<EmailVerificationToken> findToken = emailVerificationRepository.findByEmail(email);
        if (findToken.isEmpty()) {
            throw new EmailVerificationTokenNotFoundException("토큰을 찾을 수 없습니다.");
        }

        EmailVerificationToken emailVerificationToken = findToken.get();
        if (emailVerificationToken.isVerified()) {
            throw new AlreadyExpiredEmailVerificationToken("이미 만료된 토큰입니다.");
        }

        if (!token.equals(emailVerificationToken.getToken())) {
            throw new InvalidTokenException("토큰값이 일치하지 않습니다");
        }

        emailVerificationToken.setVerified(true);
        EmailVerificationToken saveToken = emailVerificationRepository.save(emailVerificationToken);
        return saveToken.getId();
    }

    private void isAlreadyVerified(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new AlreadyVerifiedEmailVerificationTokenException("이미 인증된 이메일입니다.");
        }
    }

    private static String getUUIDToken() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
