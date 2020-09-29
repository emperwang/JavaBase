package com.wk.convert;

import com.wk.entity.UserBO;
import com.wk.entity.UserDao;
import com.wk.entity.UserDetailBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserDao convert(UserBO userBO);

    @Mappings({
            @Mapping(source = "id", target = "userId")
    })
    UserDetailBO convertDetail(UserDao userDao);
}
