package hello.hellospring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private int curPage;
    private int totalPage;
    private T vintageBoardList;
}
