    package net.musecom.boot_board.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.Getter;
    import lombok.Setter;
    import net.musecom.boot_board.dto.BoardDto;

    @Entity
    @Setter
    @Getter
    @Table(name="bboard")
    public class BoardEntity extends TimesEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length=20, nullable = false)
        private String bwriter;

        @Column(length=20, nullable = false)
        private String pass;

        @Column(length=255, nullable = false)
        private String title;

        @Column(length=1000, nullable = false)
        private String contents;

        @Column
        private int hits;

        public static BoardEntity toBoardEntity(BoardDto bDto) {
            BoardEntity bEntity = new BoardEntity();
            
            bEntity.setBwriter(bDto.getBwriter());
            bEntity.setPass(bDto.getPass());
            bEntity.setTitle(bDto.getTitle());
            bEntity.setContents(bDto.getContents());
            bEntity.setHits(0);

            return bEntity; 
        }

        public static BoardEntity toUpdateEntity(BoardDto bDto) {
            BoardEntity bEntity = new BoardEntity();

            bEntity.setId(bDto.getId());
            bEntity.setBwriter(bDto.getBwriter());
            bEntity.setPass(bDto.getPass());
            bEntity.setTitle(bDto.getTitle());
            bEntity.setContents(bDto.getContents());
            bEntity.setHits(bDto.getHits());

            return bEntity;  
        }
    }