package com.moxuan.payment.pojo.mapper;


import com.moxuan.payment.pojo.vo.PaymentInfoVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PaymentInfoBeanMapper {

    PaymentInfoVO combination( Integer amount,
                               String merchantOrderId,
                               String merchantUserId,
                               String qrCodeUrl);


}
