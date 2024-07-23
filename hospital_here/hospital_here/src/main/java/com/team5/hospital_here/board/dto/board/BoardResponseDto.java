package com.team5.hospital_here.board.dto.board;

import com.team5.hospital_here.board.domain.Board;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String name;

}
