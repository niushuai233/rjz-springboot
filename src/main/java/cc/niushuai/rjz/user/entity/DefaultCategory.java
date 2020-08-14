package cc.niushuai.rjz.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ns
 * @date 2020/8/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultCategory {
    private String recordType;
    private String iconClassName;
    private String categoryName;
}
