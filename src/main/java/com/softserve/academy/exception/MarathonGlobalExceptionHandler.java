package com.softserve.academy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MarathonGlobalExceptionHandler {
    private static final Logger MARATHON_EXCEPTIONS_HANDLER_LOGGER =
            LoggerFactory.getLogger(MarathonGlobalExceptionHandler.class);

    private void errorLogger(HttpServletRequest request, int statusCode, Exception exception) {
        MarathonGlobalExceptionHandler.MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        statusCode,
                        exception.getMessage())
        );
    }

    @ExceptionHandler({MarathonInnerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Marathon Inner Exception occurred")
    public void entityNotFoundHandler(HttpServletRequest request, Exception exception) {
        errorLogger(request, HttpStatus.NOT_FOUND.value(), exception);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,  reason = "Internal Server Exception occurred")
    public void internalServerErrorHandler(HttpServletRequest request, Exception exception) {
        errorLogger(request, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestHandler(HttpServletRequest request, Exception exception) {
        errorLogger(request, HttpStatus.BAD_REQUEST.value(), exception);

        return new ModelAndView("error").addObject("message", exception.getMessage())
                .addObject("code", HttpStatus.BAD_REQUEST.value());
    }
}
