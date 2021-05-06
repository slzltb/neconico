package com.neconico.neconico.service.store;

import com.neconico.neconico.dto.store.StoreInfoDto;
import com.neconico.neconico.mapper.store.StoreInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreInfoService {

    private final StoreInfoMapper storeInfoMapper;

    public StoreInfoDto findStoreInfo(Long userId){
        return storeInfoMapper.selectStoreInfoByUser(userId);
    }

    public void createStoreInfo(StoreInfoDto storeInfoDto) {
        storeInfoMapper.insertStoreInfo(storeInfoDto);
    }

    public void updateStoreInfo(StoreInfoDto storeInfoDto){
        if(storeInfoDto.getStoreName()!=null){
            if(storeInfoMapper.selectStoreInfoByName(storeInfoDto.getStoreName())) {
                throw new IllegalArgumentException("Exist same storename");
            }
        }
        storeInfoMapper.updateStoreInfo(storeInfoDto);
    }


}