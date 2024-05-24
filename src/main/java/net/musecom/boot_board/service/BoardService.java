package net.musecom.boot_board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.musecom.boot_board.dto.BoardDto;
import net.musecom.boot_board.entity.BoardEntity;
import net.musecom.boot_board.repository.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void write(BoardDto bDto) {
        BoardEntity bEntity = BoardEntity.toBoardEntity(bDto);
        boardRepository.save(bEntity);
    }

    public List<BoardDto> findAll(){
       //column에 있는 모든 내용을 가져옴 
       List<BoardEntity> bEntities = 
                 boardRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
       
       //BoardDto 타입의 ArrayList를 만듬
       List<BoardDto> bDtos = new ArrayList<>();  
       
       //위에서 만든 ArrayList 박스에 column에서 가져온 내용을 하나하나 채움
       for(BoardEntity bEntity : bEntities) {
           bDtos.add(BoardDto.toBoardDto(bEntity));
       }    
       return bDtos;
    }

    /*게시글 조회수 증가 */
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    /* 게시물보기 */
    public BoardDto findById(Long id) {
       // Optional 값이 있을 수도 있고 없을 수도 있는 타입
       Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
       if(optionalBoardEntity.isPresent()){

          BoardEntity boardEntity = optionalBoardEntity.get();
          BoardDto boardDto = BoardDto.toBoardDto(boardEntity);
          return boardDto;
          
       }else{
         return null;
       }
    }

    /** 게시물 수정 */
    public BoardDto update(BoardDto bDto) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(bDto);
        boardRepository.save(boardEntity);
        return findById(bDto.getId());
    }

    /* 게시물 삭제 */
    public void delete(Long id){
        boardRepository.deleteById(id);
    }

    public Page<BoardDto> paging(Pageable pageable){
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5; //한 페이지에 보여줄 글의 갯 수
        // select * from bbs limit 0==>page, 10==>pageLimit;
        
        Page<BoardEntity> bEntities = 
                    boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));
       
          System.out.println("1. 전체 글 수 : " + bEntities.getTotalElements());
          System.out.println("2. db에 요청한 페이지 번호 : " + bEntities.getNumber());
          System.out.println("3. 요청 페이지의 해당 글 : " + bEntities.getContent());
          System.out.println("4. 전체 페이지 수 : " + bEntities.getTotalPages());
          System.out.println("5. 한 페이지에 보여지는 글 수 : " + bEntities.getSize());
          System.out.println("6. 이전 페이지 : " + bEntities.hasPrevious());
          System.out.println("7. 다음 페이지 : " + bEntities.hasNext());
          System.out.println("8. 첫 페이지 : " + bEntities.isFirst());
          System.out.println("9. 마지막 페이지 : " + bEntities.isLast());

         // 목록에서 보여줄 내용 id, bwriter, hits, title, createdTime
         Page<BoardDto> boardDtos = bEntities.map( board -> new BoardDto(board.getId(), board.getBwriter(),board.getHits(),board.getTitle(),board.getCreatedTime())); 
        return boardDtos;
    }

}