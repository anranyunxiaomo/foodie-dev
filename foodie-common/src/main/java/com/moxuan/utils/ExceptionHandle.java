package com.moxuan.utils;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.moxuan.utils.CodeConstant.*;

/**
 * 统一异常补获
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    public static TraceBaseResp newErrResponse(Exception e) {
        TraceBaseResp response = new TraceBaseResp();
        BaseResp.Trace trace = new BaseResp.Trace();
        response.setData(trace.getErrTrace());
        response.setTrace(trace);
        trace.setErrType(e.getClass().getName());
        trace.setStackTraceElements(filterStackTraceElement(e.getStackTrace()));
        trace.setErrTrace(e.getMessage());
        return response;
    }

    /**
     * 获取异常调用栈信息
     *
     * @param stackTraceElements 异常调用栈列表
     * @return 过滤后的调用栈
     */
    public static StackTraceElement filterStackTraceElement(StackTraceElement[] stackTraceElements) {
        if (ArrayUtils.isNotEmpty(stackTraceElements)) {
            return Arrays.stream(stackTraceElements)
                    .filter(e -> e.getClassName().contains("com.moxuan.*")).limit(1)
                    .toArray(StackTraceElement[]::new)[0];
        }
        return null;
    }

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public TraceBaseResp handleBusinessException(CustomException e) {
        log.warn("框架捕获到异常:[{}][{}]", e.getErrorCode(), e.getMessage());
        TraceBaseResp response = newErrResponse(e);
        response.setStatus(e.getErrorCode());
        response.setMsg(e.getMessage());
        response.setData(e.getBody());
        return response;
    }

    /**
     * Validation参数校验错误
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler({
            BindException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public TraceBaseResp handleValidationException(Exception e) {
        log.warn("框架捕获到参数校验异常:{}", e.getMessage());

        TraceBaseResp response = newErrResponse(e);

        if (e instanceof ConstraintViolationException) {
            response.setStatus(CodeConstant.ERR_METHOD_ARGUMENT_NOT_VALID);
            ConstraintViolationException ex = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> cvs = ex.getConstraintViolations();
            if (CollectionUtils.isEmpty(cvs)) {
                response.setMsg(CodeNameConstant.getMsg(response.getStatus()));
            } else {
                response.setMsg(cvs.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
            }
            response.getTrace().setSubMsg(cvs.stream().map(cv -> "[" + cv.getPropertyPath() + "]" + cv.getMessage()).collect(Collectors.joining(",")));
            return response;
        }
        List<FieldError> fieldErrors;
        if (e instanceof BindException) {
            response.setStatus(ERR_BIND);
            BindException ex = (BindException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        } else {
            // 参数无效，如JSON请求参数违反约束
            response.setStatus(ERR_METHOD_ARGUMENT_NOT_VALID);
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        }
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            response.setMsg(fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(",")));
            response.getTrace().setSubMsg(fieldErrors.stream().map(fieldError -> "[" + fieldError.getField() + "]" + fieldError.getDefaultMessage()).collect(Collectors.joining(",")));
        } else {
            response.setMsg(CodeNameConstant.getMsg(response.getStatus()));
        }
        return response;
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public TraceBaseResp handleServletException(Exception e) {
        log.error("框架捕获到系统异常:", e);

        TraceBaseResp response = newErrResponse(e);

        if (e instanceof NoHandlerFoundException) {
            // 找不到处理器异常
            response.setStatus(ERR_NO_HANDLER_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            // 请求方法不支持异常
            response.setStatus(ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED);
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            // 请求类型不支持异常
            response.setStatus(ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED);
        } else if (e instanceof MissingPathVariableException) {
            // 缺失路径变量异常
            response.setStatus(ERR_MISSING_PATH_VARIABLE);
        } else if (e instanceof TypeMismatchException) {
            // 缺失路径变量异常
            response.setStatus(ERR_TYPE_MISMATCH);
        } else if (e instanceof HttpMessageNotReadableException) {
            // HttpMessage不可读异常
            response.setStatus(ERR_HTTP_MESSAGE_NOT_READABLE);
        } else if (e instanceof HttpMessageNotWritableException) {
            // HttpMessage不可写异常
            response.setStatus(ERR_HTTP_MESSAGE_NOT_WRITABLE);
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            // 请求类型不接受异常
            response.setStatus(ERR_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE);
        } else if (e instanceof ServletRequestBindingException) {
            // Servlet请求绑定异常
            response.setStatus(ERR_REQUEST_BINDING);
        } else if (e instanceof MissingServletRequestPartException) {
            // 缺失Servlet请求异常
            response.setStatus(ERR_MISSING_SERVLET_REQUEST_PART);
        } else if (e instanceof AsyncRequestTimeoutException) {
            // 异步请求超时异常
            response.setStatus(ERR_ASYNC_REQUEST_TIMEOUT);
        }
        response.setMsg(CodeNameConstant.getMsg(response.getStatus()));
        return response;
    }

    /**
     * 其他常见异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler({
            NullPointerException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            ArithmeticException.class,
            ClassCastException.class,
            NegativeArraySizeException.class,
            ArrayIndexOutOfBoundsException.class,
            NoSuchMethodException.class,
            SQLException.class,
            IOException.class,
    })
    public TraceBaseResp handleCommonException(Exception e) {
        log.error("框架捕获到异常:", e);

        TraceBaseResp response = newErrResponse(e);
        if (e instanceof NullPointerException) {
            // 空指针异常
            response.setStatus(ERR_NULL_POINTER);
        } else if (e instanceof IllegalArgumentException) {
            // 非法参数异常
            response.setStatus(ERR_ILLEGAL_ARGUMENT);
        } else if (e instanceof IllegalStateException) {
            // 非法状态异常
            response.setStatus(ERR_ILLEGAL_STATE);
        } else if (e instanceof ArithmeticException) {
            // 计算异常
            response.setStatus(ERR_ARITHMETIC);
        } else if (e instanceof ClassCastException) {
            // 类型转换异常
            response.setStatus(ERR_CLASS_CAST);
        } else if (e instanceof NegativeArraySizeException) {
            // 集合负数异常
            response.setStatus(ERR_NEGATIVE_ARRAY_SIZE);
        } else if (e instanceof ArrayIndexOutOfBoundsException) {
            // 集合超出范围异常
            response.setStatus(ERR_ARRAY_INDEX_OUT_OF_BOUNDS);
        } else if (e instanceof NoSuchMethodException) {
            // 方法未找到异常
            response.setStatus(ERR_NO_SUCH_METHOD);
        } else if (e instanceof SQLException) {
            // SQL异常
            response.setStatus(ERR_SQL);
        } else if (e instanceof IOException) {
            // 读写异常
            response.setStatus(ERR_IO);
        }
        response.setMsg(CodeNameConstant.getMsg(response.getStatus()));
        return response;
    }

}

