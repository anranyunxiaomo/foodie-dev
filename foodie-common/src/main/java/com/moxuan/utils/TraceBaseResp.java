package com.moxuan.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class TraceBaseResp extends BaseResp {
    /**
     * 响应消息体(泛型)
     *
     * @ignore
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Trace trace;
}
