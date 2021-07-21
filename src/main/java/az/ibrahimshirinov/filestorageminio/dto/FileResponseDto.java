package az.ibrahimshirinov.filestorageminio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {

    private Long objectId;
    private Long productId;
    private String objectUrl;
}
