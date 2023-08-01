package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.dto.BoardDto;
import song.devlog1.dto.BoardPageDto;
import song.devlog1.dto.EditBoardDto;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.entity.Board;
import song.devlog1.entity.User;
import song.devlog1.exception.invalid.InvalidAuthorizedException;
import song.devlog1.exception.notfound.BoardNotFoundException;
import song.devlog1.exception.notfound.UserNotFoundException;
import song.devlog1.repository.BoardJpaRepository;
import song.devlog1.repository.UserJpaRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardJpaRepository boardRepository;
    private final UserJpaRepository userRepository;

    @Transactional
    public Long saveBoard(SaveBoardDto saveBoardDto, Long userId) {
        User findUser = getUserById(userId);

        Board board = saveBoardDto.toEntity();
        board.setWriter(findUser);

        Board saveBoard = boardRepository.save(board);

        return saveBoard.getId();
    }

    @Transactional
    public BoardDto findById(Long boardId) {
        Board findBoard = getBoardById(boardId);

        BoardDto boardDto = new BoardDto(findBoard);

        return boardDto;
    }

    @Transactional
    public Page<BoardPageDto> findAll(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);

        Page<BoardPageDto> boardPageDtoPage = boardPage.map(BoardPageDto::new);
        return boardPageDtoPage;
    }

    @Transactional
    public Long editBoard(Long userId, Long boardId, EditBoardDto editBoardDto) {
        Board findBoard = getBoardById(boardId);

        if (!findBoard.getWriter().getId().equals(userId)) {
            throw new InvalidAuthorizedException("권한이 없습니다.");
        }

        if (!findBoard.getTitle().equals(editBoardDto.getTitle())) {
            findBoard.setTitle(editBoardDto.getTitle());
        }
        if (!findBoard.getContent().equals(editBoardDto.getContent())) {
            findBoard.setContent(editBoardDto.getContent());
        }

        Board saveBoard = boardRepository.save(findBoard);

        return saveBoard.getId();
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        Board findBoard = getBoardById(boardId);
        if (!findBoard.getWriter().getId().equals(userId)) {
            throw new InvalidAuthorizedException("권한이 없습니다.");
        }

        boardRepository.delete(findBoard);
    }

    private User getUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get();
    }

    private Board getBoardById(Long boardId) {
        Optional<Board> findBoard = boardRepository.findById(boardId);
        if (findBoard.isEmpty()) {
            throw new BoardNotFoundException("게시글을 찾을 수 없습니다.");
        }
        return findBoard.get();
    }
}
