package com.neconico.neconico.mapper.users;

import com.neconico.neconico.dto.users.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserInfoDto> selectUserAll();

    UserInfoDto selectUserByAccountId(String accountId);

    SessionUser selectSessionUserInfoByAccountId(String accountId);

    UserAccountIdDto selectUserByNameAndEmail(UserFindAccountIdDto userFindAccountIdDto);

    UserAccountIdDto selectUserByAccountIdAndPhoneNumAndEmail(UserFindAccountPwDto userFindAccountPwDto);

    void insertUser(UserJoinDto userJoinDto);

    void updateUserAuthority(String accountId);

    void updateUserInfo(UserInfoDto userInfoDto);

}
