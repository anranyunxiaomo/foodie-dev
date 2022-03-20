package com.moxuan.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.bo.AddressBO;
import com.moxuan.bo.AddressUpdateBO;
import com.moxuan.mapper.UserAddressMapper;
import com.moxuan.pojo.UserAddress;
import com.moxuan.pojo.mapper.AddressBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserAddressService extends ServiceImpl<UserAddressMapper, UserAddress> {

    @Resource
    private AddressBeanMapper addressBeanMapper;
    @Autowired
    private Sid sid;


    /**
     * 用户新增地址
     */
    public BaseResp add(AddressBO addressBO) {
        List<UserAddress> list = this.lambdaQuery().eq(UserAddress::getUserId, addressBO.getUserId()).list();
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer isDefault = 0;
        if (CollUtil.isEmpty(list)) {
            isDefault = 1;
        }
        UserAddress userAddress = addressBeanMapper.addToAddress(addressBO);
        userAddress.setId(sid.nextShort());
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        this.save(userAddress);
        return ResultUtil.ok();
    }

    /**
     * 用户修改地址
     */
    public BaseResp renew(AddressUpdateBO addressUpdateBO) {
        UserAddress userAddress = addressBeanMapper.updateToAddress(addressUpdateBO);
        userAddress.setUpdatedTime(new Date());
        this.updateById(userAddress);
        return ResultUtil.ok();
    }

    /**
     * 用户删除地址
     */
    public BaseResp delete(String userId, String addressId) {
        if (StrUtil.isBlankIfStr(userId) || StrUtil.isBlankIfStr(addressId)) {
            return ResultUtil.error("");
        }
        this.remove(Wrappers.<UserAddress>lambdaUpdate()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getId, addressId));
        UserAddress userAddress = this.lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1)
                .one();
        if (ObjectUtil.isNull(userAddress)) {
            List<UserAddress> list = this.lambdaQuery().eq(UserAddress::getUserId, userId).list();
            userAddress = list.get(0);
            userAddress.setIsDefault(1);
            this.updateById(userAddress);
        }
        return ResultUtil.ok();
    }

    /**
     * 用户设置默认地址
     */
    public BaseResp setDefalut(String userId, String addressId) {
        if (StrUtil.isBlankIfStr(userId) || StrUtil.isBlankIfStr(addressId)) {
            return ResultUtil.error("");
        }
        UserAddress userAddress = new UserAddress();
        userAddress.setIsDefault(0);
        userAddress.setUpdatedTime(new Date());
        this.update(userAddress, Wrappers.<UserAddress>lambdaUpdate()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1));
        userAddress.setIsDefault(1);
        userAddress.setUpdatedTime(new Date());
        this.update(userAddress, Wrappers.<UserAddress>lambdaUpdate()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId));
        return ResultUtil.ok();
    }
}

