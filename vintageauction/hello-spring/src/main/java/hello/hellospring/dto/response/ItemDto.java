package hello.hellospring.dto.response;

import lombok.Data;

@Data
public class ItemDto {
    private String itemName;
    private String itemPrice;
    private String itemCategory;
    private String storeFileName;
}
