package com.video.vip.basics.mybatis;

import lombok.NonNull;
import org.apache.ibatis.annotations.Select;

/**
 * mybatis操作数据库父级
 * @author wxn
 */
public interface MyBatisBaseDAO {

    /**
     * 条件及版本号查询
     * @param tableName 表名称
     * @param strWhere 查询条件
     * @return
     */
    @Select("select version from ${param1} ${param2} limit 1")
    Integer getVersionByWhereAndVersion(@NonNull String tableName
            , @NonNull String strWhere);

}
