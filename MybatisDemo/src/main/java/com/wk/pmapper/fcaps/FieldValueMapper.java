package com.wk.pmapper.fcaps;

import com.wk.entity.fcaps.FieldValue;
import com.wk.entity.fcaps.HisField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2020/10/20 15:24
 * @Description
 */
public interface FieldValueMapper {

    List<Map<String,Object>> getField(@Param("field") String field);

    List<FieldValue> getField2(List<String> list);

    List<HisField> getHisa();
}
