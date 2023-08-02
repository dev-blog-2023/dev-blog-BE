package song.devlog1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.Board;
import song.devlog1.entity.Comment;
import song.devlog1.entity.Role;
import song.devlog1.entity.User;
import song.devlog1.entity.role.RoleName;
import song.devlog1.repository.BoardJpaRepository;
import song.devlog1.repository.CommentJpaRepository;
import song.devlog1.repository.RoleJpaRepository;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.service.UserRoleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void doInit() {
        initService.init1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final PasswordEncoder passwordEncoder;
        private final UserJpaRepository userRepository;
        private final RoleJpaRepository roleRepository;
        private final BoardJpaRepository boardRepository;
        private final CommentJpaRepository commentRepository;
        private final UserRoleService userRoleService;

        public void init1() {
            User userA = new User();
            userA.setUsername("a");
            userA.setPassword(passwordEncoder.encode("a"));
            userA.setName("a");
            userA.setEmail("dkclasltmf22@naver.com");
            userA.setEnabled(true);
            userA.setAccountNonExpired(true);
            userA.setAccountNonLocked(true);
            userA.setCredentialsNonExpired(true);
            User saveUserA = userRepository.save(userA);

            Role roleUser = new Role(RoleName.ROLE_USER);
            Role saveRoleUser = roleRepository.save(roleUser);

            Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
            Role saveRoleAdmin = roleRepository.save(roleAdmin);

            userRoleService.grantRole(saveUserA.getId(), roleUser.getRoleName());
            userRoleService.grantRole(saveUserA.getId(), roleAdmin.getRoleName());

            User userB = new User();
            userB.setUsername("b");
            userB.setPassword(passwordEncoder.encode("b"));
            userB.setName("b");
            userB.setEmail("");
            userB.setEnabled(true);
            userB.setAccountNonExpired(true);
            userB.setAccountNonLocked(true);
            userB.setCredentialsNonExpired(true);
            User saveUserB = userRepository.save(userB);

            userRoleService.grantRole(saveUserB.getId(), roleUser.getRoleName());


            for (int i = 0; i < 20; i++) {
                Board board = new Board();
                board.setTitle("Title " + i);
                board.setContent("Content " + i);
                board.setWriter(userA);
                boardRepository.save(board);
            }

            Board findBoard = boardRepository.findById(1L).get();

            for (int i = 0; i < 3; i++) {
                Comment comment = new Comment();
                comment.setBoard(findBoard);
                comment.setWriter(userA);
                comment.setContent("Comment " + i);
                Comment saveComment = commentRepository.save(comment);
                if (i == 1) {
                    for (int j = 0; j < 2; j++) {
                        Comment reply = new Comment();
                        reply.setBoard(findBoard);
                        reply.setWriter(userA);
                        reply.setParent(saveComment);
                        reply.setContent("Reply " + j);
                        commentRepository.save(reply);
                    }
                }
            }
        }
    }
}
