package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.EmailVerificationToken;
import song.devlog1.entity.User;
import song.devlog1.exception.already.AlreadyExistsEmailException;
import song.devlog1.exception.already.AlreadyExpiredTokenException;
import song.devlog1.exception.already.AlreadyVerifiedTokenException;
import song.devlog1.exception.notfound.EmailVerificationTokenNotFoundException;
import song.devlog1.repository.EmailVerificationJpaRepository;
import song.devlog1.repository.UserJpaRepository;

import java.time.LocalDateTime;
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
        isAlreadyUsedEmail(email);

        Optional<EmailVerificationToken> findToken = emailVerificationRepository.findByEmail(email);
        if (findToken.isPresent()) {
            if (findToken.get().isVerified()) {
                throw new AlreadyVerifiedTokenException("이미 인증된 토큰입니다22.");
            }
            EmailVerificationToken token = findToken.get();
            token.setToken(getUUIDToken());
            EmailVerificationToken saveToken = emailVerificationRepository.save(token);
            return saveToken.getToken();
        }

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(email);
        emailVerificationToken.setToken(getUUIDToken());
        emailVerificationToken.setVerified(false);
        EmailVerificationToken saveToken = emailVerificationRepository.save(emailVerificationToken);

        return saveToken.getToken();
    }

    @Transactional
    public Long verifyEmailVerificationToken(String email, String token) {
        Optional<EmailVerificationToken> findToken = emailVerificationRepository.findByEmailAndToken(email, token);
        if (findToken.isEmpty()) {
            throw new EmailVerificationTokenNotFoundException("토큰을 찾을 수 없습니다.");
        }

        EmailVerificationToken emailVerificationToken = findToken.get();
        if (emailVerificationToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AlreadyExpiredTokenException("이미 만료된 토큰입니다.");
        }

        if (emailVerificationToken.isVerified()) {
            throw new AlreadyVerifiedTokenException("이미 인증된 이메일입니다.");
        }

        emailVerificationToken.setVerified(true);
        EmailVerificationToken saveToken = emailVerificationRepository.save(emailVerificationToken);
        return saveToken.getId();
    }

    @Transactional
    public void deleteEmailVerificationToken(Long tokenId) {
        Optional<EmailVerificationToken> findToken = emailVerificationRepository.findById(tokenId);
        if (findToken.isPresent()) {
            emailVerificationRepository.delete(findToken.get());
        }
    }

    private void isAlreadyUsedEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new AlreadyExistsEmailException("이미 사용중인 이메일입니다.");
        }
    }

    private static String getUUIDToken() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
