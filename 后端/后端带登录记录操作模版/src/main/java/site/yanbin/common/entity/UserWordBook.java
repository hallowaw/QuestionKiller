package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWordBook {
//缺一个permission的字段
    private Long userId;    // 用户ID
    private Long wordBookId; // 词书ID
}
