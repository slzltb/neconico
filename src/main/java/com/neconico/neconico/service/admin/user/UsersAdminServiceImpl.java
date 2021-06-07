package com.neconico.neconico.service.admin.user;

import com.neconico.neconico.dto.admin.user.UserListDto;
import com.neconico.neconico.mapper.admin.user.UsersAdminMapper;
import com.neconico.neconico.paging.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersAdminServiceImpl implements UsersAdminService {

    private final UsersAdminMapper usersAdminMapper;

    @Override
    public long countUserList(String authority) { return usersAdminMapper.countUserList(authority); }

    @Override
    public List<UserListDto> selectUserList(Criteria cri) { return usersAdminMapper.selectUserList(setCriteria(cri)); }

    @Override
    public List<UserListDto> selectAdminList(Criteria cri) { return usersAdminMapper.selectAdminList(setCriteria(cri)); }

    @Override
    public Map<String, Long> selectUserListByAge() { return usersAdminMapper.selectUserListByAge(); }

    @Override
    public Map<String, Long> selectUserListByGender() { return usersAdminMapper.selectUserListByGender(); }

    @Override
    public Map<String, Long> selectUserListByLocation() {
        return usersAdminMapper.selectUserListByLocation();
    }

    @Override
    public Map<String, Long> selectUserListByRegistered() {
        return usersAdminMapper.selectUserListByRegistered();
    }

    //페이징 기준 설정
    private Criteria setCriteria(Criteria cri){
        cri.setSortingColumn("USERID");
        cri.setRequestOrder("ASC");
        cri.setContentPerPage(10);

        if(cri.getCurrentPage()<=0)
            cri.setCurrentPage(1);

        return cri;
    }
}
