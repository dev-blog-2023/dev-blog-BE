package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.ResetPasswordToken;
import song.devlog1.exception.notfound.ResetPasswordTokenNotFoundException;
import song.devlog1.repository.ResetPasswordTokenJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordTokenService {

    private final ResetPasswordTokenJpaRepository resetPasswordRepository;

    @Transactional
    public void saveResetPasswordToken(String username) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(username);
        resetPasswordToken.setToken(UUID.randomUUID().toString().substring(0, 5));

        ResetPasswordToken saveToken = resetPasswordRepository.save(resetPasswordToken);
    }

    @Transactional
    public String verifyResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = verifyToken(token);

        return resetPasswordToken.getUsername();
    }

    private ResetPasswordToken verifyToken(String token) {
        Optional<ResetPasswordToken> findToken = resetPasswordRepository.findByToken(token);
        if (findToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException("토큰을 찾을 수 없습니다.");
        }
        return findToken.get();
    }
}
