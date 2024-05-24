package net.musecom.boot_board.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

import net.musecom.boot_board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    /* 조회수 증가 
     * update bboard set hits=hits+1 where id=?
    */
    @Modifying
    @Query(value = "update BoardEntity b set b.hits=b.hits+1 where b.id=:id")
    void updateHits(@Param("id") Long id); //구현만되어있는상태 -> service에서 실행

    @NonNull List<BoardEntity> findAll(@NonNull Sort srot);
}
