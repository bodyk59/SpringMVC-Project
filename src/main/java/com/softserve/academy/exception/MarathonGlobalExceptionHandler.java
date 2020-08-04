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

    @ExceptionHandler({MarathonInnerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Marathon Inner Exception occurred")
    public void entityNotFoundHandler(HttpServletRequest request, Exception exception) {
        MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage())
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,  reason = "Internal Server Exception occurred")
    public void internalServerErrorHandler(HttpServletRequest request, Exception exception) {
        MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getMessage())
        );
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestHandler(HttpServletRequest request, Exception exception) {
        MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage())
        );

        return new ModelAndView("error").addObject("message", exception.getMessage())
                .addObject("code", HttpStatus.BAD_REQUEST.value());
    }
}
