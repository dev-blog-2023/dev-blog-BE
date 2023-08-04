package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.ResetPasswordToken;
import song.devlog1.exception.already.AlreadyExpiredTokenException;
import song.devlog1.exception.notfound.ResetPasswordTokenNotFoundException;
import song.devlog1.repository.ResetPasswordTokenJpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordTokenService {

    private final ResetPasswordTokenJpaRepository resetPasswordRepository;

    @Transactional
    public String saveResetPasswordToken(String username) {
        Optional<ResetPasswordToken> findToken = resetPasswordRepository.findByUsername(username);
        if (findToken.isPresent()) {
            ResetPasswordToken resetPasswordToken = findToken.get();
            resetPasswordToken.setToken(getUUIDToken());
            return resetPasswordToken.getToken();
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(username);
        resetPasswordToken.setToken(getUUIDToken());

        ResetPasswordToken saveToken = resetPasswordRepository.save(resetPasswordToken);

        return saveToken.getToken();
    }

    @Transactional
    public String verifyResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = getResetPasswordTokeByToken(token);
        if (resetPasswordToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            throw new AlreadyExpiredTokenException("이미 만료된 토큰입니다.");
        }

        return resetPasswordToken.getUsername();
    }

    private ResetPasswordToken getResetPasswordTokeByToken(String token) {
        Optional<ResetPasswordToken> findToken = resetPasswordRepository.findByToken(token);
        if (findToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException("토큰을 찾을 수 없습니다.");
        }
        return findToken.get();
    }

    private String getUUIDToken() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
