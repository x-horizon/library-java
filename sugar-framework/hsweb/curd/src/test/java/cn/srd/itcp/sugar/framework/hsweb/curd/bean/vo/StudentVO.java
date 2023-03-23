package cn.srd.itcp.sugar.framework.hsweb.curd.bean.vo;

import cn.hutool.core.util.RandomUtil;
import cn.srd.itcp.sugar.framework.hsweb.curd.bean.domain.StudentDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
public class StudentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8117094738912289865L;
    private Integer id;
    private String name;
    private Integer age;
    private StudentDO.Sex sex;

    public static StudentVO newVO() {
        return StudentVO.builder()
                .id(RandomUtil.randomInt(99))
                .name("name" + RandomUtil.randomNumbers(2))
                .age(RandomUtil.randomInt(15, 20))
                .build();
    }

    public static List<StudentVO> newVOs() {
        return new ArrayList<>() {{
            add(newVO());
            add(newVO());
            add(newVO());
        }};
    }

}