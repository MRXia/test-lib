package com.mrxia.testlib.config.advice;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.mrxia.common.result.RestResult;
import com.mrxia.common.result.ResultType;
import com.mrxia.testlib.constant.SessionKey;
import com.mrxia.testlib.domain.User;

/**
 * 公共的Controller增强
 *
 * @author xiazijian
 */
@RestControllerAdvice
public class CommonControllerAdvice {

    @ModelAttribute
    public User loginUser(HttpSession session) {
        return (User) session.getAttribute(SessionKey.USER);
    }

    /**
     * 添加全局异常处理流程，根据需要设置需要处理的异常
     *
     * @param exception 方法参数校验异常
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object methodArgumentNotValidHandler(MethodArgumentNotValidException exception, HttpServletRequest request) {

        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        if (isAjax(request)) {
            return RestResult.error(ResultType.CUSTOMER_ERROR,
                    exception.getBindingResult()
                            .getFieldErrors()
                            .stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(",")));
        }
        return getErrorModelAndView(exception, request);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(Exception exception, HttpServletRequest request) {

        return getErrorModelAndView(exception, request);
    }

    private boolean isAjax(HttpServletRequest request) {
        String contentTypeHeader = request.getContentType();
        String acceptHeader = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        return (contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
    }

    private ModelAndView getErrorModelAndView(Exception exception, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("msg", exception.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("stackTrace", exception.getStackTrace());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
