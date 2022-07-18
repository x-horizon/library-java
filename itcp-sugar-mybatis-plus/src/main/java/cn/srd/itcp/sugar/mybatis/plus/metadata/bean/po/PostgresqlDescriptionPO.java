package cn.srd.itcp.sugar.mybatis.plus.metadata.bean.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "pg_description", autoResultMap = true)
public class PostgresqlDescriptionPO {

    @TableField(value = "objoid")
    private Long objoid;

    @TableField(value = "objsubid")
    private Long objsubid;

    @TableField(value = "description")
    private String description;

}
