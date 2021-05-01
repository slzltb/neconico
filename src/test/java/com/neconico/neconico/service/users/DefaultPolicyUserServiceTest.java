package com.neconico.neconico.service.users;

import com.neconico.neconico.dto.users.UserFindAccountIdDto;
import com.neconico.neconico.dto.users.UserInfoDto;
import com.neconico.neconico.dto.users.UserJoinDto;
import com.neconico.neconico.dto.users.UserReturnAccountIdDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefaultPolicyUserServiceTest {

    @Autowired
    private UserService userService;

    private List<UserJoinDto> userJoinDtos = new ArrayList<>();

    @BeforeEach
    void insertUserJoinDtos() {
        for(int i=1; i<=10; i++) {
            String gender = "M";

            if(i%2 == 0){
                gender = "F";
            }

            UserJoinDto userJoinDto = UserJoinDto.builder()
                    .accountId("user" + i)
                    .accountPw("1234")
                    .accountName("user" + i)
                    .gender(gender)
                    .brithdate("920521")
                    .email("user" + i + "@gmail.com")
                    .phoneNumber("010-1111-1111")
                    .address("서울시")
                    .zipNo("0158" + i)
                    .infoAgreement("check")
                    .createDate(LocalDateTime.of(2021, 04, 29, 04, 43, i))
                    .modifiedDate(LocalDateTime.of(2021, 04, 29, 04, 43, i))
                    .authority("ROLE_USER")
                    .build();

            userJoinDtos.add(userJoinDto);
        }

        for(UserJoinDto userJoinDto : userJoinDtos) {
            userService.joinUser(userJoinDto);
        }
    }

    @Test
    @DisplayName("회원가입시 회원정보를 DB에 저장")
    void insert_join_user_info_in_database() {
        //given
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .accountId("user11")
                .accountPw("1234")
                .accountName("user11")
                .gender("M")
                .brithdate("880125")
                .email("user11" + "@gmail.com")
                .phoneNumber("010-1111-1111")
                .address("서울시")
                .zipNo("015611")
                .infoAgreement("check")
                .createDate(LocalDateTime.of(2021, 04, 29, 04, 43))
                .modifiedDate(LocalDateTime.of(2021, 04, 29, 04, 43))
                .authority("ROLE_USER")
                .build();

        //when
        Long user_id = userService.joinUser(userJoinDto);


        //then
        assertThat(user_id).isNotNull();
    }

    @ParameterizedTest(name = "{index} -> 유저아이디가 {0}일때")
    @ValueSource(
            strings = {"user1", "user2", "user3", "user4", "user5",
                    "user6", "user7", "user8", "user9", "user10"})
    @DisplayName("회원의 아이디로 해당 유저정보를 DB에서 가져온다")
    void select_user_by_account_id_in_database(String accountId) {

        UserInfoDto userInfoDto = userService.findUserByAccountId(accountId);

        //해당 회원이
        assertThat(userInfoDto).extracting("accountId").isEqualTo(accountId);
    }

    @Test
    @DisplayName("모든회원 정보를 DB에서 가져온다")
    void get_user_information_in_database() {
        List<UserInfoDto> userInfoDtoList = userService.findAll();

        assertThat(userInfoDtoList.size()).isEqualTo(10);
    }

    @ParameterizedTest(name = "{index} -> 유저이름이 {0}이고, 이메일이 {1}일때")
    @DisplayName("회원 아이디 찾기 시 해당 회원이 존재하는지 확인")
    @CsvSource({"'user1', 'user1@gmail.com'", "'user2', 'user2@gmail.com'", "'user3', 'user3@gmail.com'",
            "'user4', 'user4@gmail.com'", "'user5', 'user5@gmail.com'", "'user6', 'user6@gmail.com'",
            "'user7', 'user7@gmail.com'", "'user8', 'user8@gmail.com'", "'user9', 'user9@gmail.com'",
            "'user10', 'user10@gmail.com'"})
    void when_searching_for_a_account_id_check_whether_the_member_exists(String accountName, String email) {

        UserFindAccountIdDto userFindAccountIdDto = new UserFindAccountIdDto();

        userFindAccountIdDto.setAccountName(accountName);
        userFindAccountIdDto.setEmail(email);

        UserReturnAccountIdDto userReturnAccountIdDto = userService
                .findAccountIdByNameAndEmail(userFindAccountIdDto);

        assertThat(userReturnAccountIdDto).isNotNull();

    }

    @ParameterizedTest(name = "{index} -> 유저아이디가 {0}일때")
    @DisplayName("회원 탈퇴시 회원권한을 ROLE_DROP으로 변경")
    @ValueSource(
            strings = {"user1", "user2", "user3", "user4", "user5",
                    "user6", "user7", "user8", "user9", "user10"})
    void update_authority_when_user_withdrawal(String accountId) {

        userService.changeDropUserAuthority(accountId);
        UserInfoDto userInfoDto = userService.findUserByAccountId(accountId);

        assertThat(userInfoDto)
                .extracting("authority").isEqualTo("ROLE_DROP");
    }

    @Test
    @DisplayName("회원 정보 변경 시 DB에 반영")
    void update_user_info_in_database() {

        UserJoinDto userJoinDto = userJoinDtos.get(0);

        //변경된 회원정보
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setAccountId(userJoinDto.getAccountId());
        userInfoDto.setPhoneNumber("010-2325-3535");
        userInfoDto.setEmail("user11111@gamil.com");
        userInfoDto.setZipNo("51562");
        userInfoDto.setAddress("부산시");
        userInfoDto.setModifiedDate(LocalDateTime.of(2021, 04, 30, 03, 13, 20));

        userService.changeUserInfo(userInfoDto);

        UserInfoDto findUserInfo = userService.findUserByAccountId(userInfoDto.getAccountId());


        assertThat(findUserInfo).extracting(
                "phoneNumber",
                "email",
                "zipNo",
                "address",
                "modifiedDate")
                .contains(
                        userInfoDto.getPhoneNumber(),
                        userInfoDto.getEmail(),
                        userInfoDto.getZipNo(),
                        userInfoDto.getAddress(),
                        userInfoDto.getModifiedDate()
                );
    }
}