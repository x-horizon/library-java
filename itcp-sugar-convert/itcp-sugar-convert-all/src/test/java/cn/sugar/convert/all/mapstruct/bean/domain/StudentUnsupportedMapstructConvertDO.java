package cn.sugar.convert.all.mapstruct.bean.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class StudentUnsupportedMapstructConvertDO implements Serializable {

    private static final long serialVersionUID = -4236383322015236485L;

    private Integer id;
    private String name;
    private Integer age;
    private String sex;

}