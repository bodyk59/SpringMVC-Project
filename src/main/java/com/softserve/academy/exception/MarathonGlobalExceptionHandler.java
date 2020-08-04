package com.softserve.academy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MarathonGlobalExceptionHandler {
    private static final Logger MARATHON_EXCEPTIONS_HANDLER_LOGGER =
            LoggerFactory.getLogger(MarathonGlobalExceptionHandler.class);

    private ModelAndView modelAndViewCreator(String exceptionMessage, int code) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exceptionMessage", exceptionMessage);
        modelAndView.addObject("code", code);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler({MarathonInnerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView innerExceptionsHandler(HttpServletRequest request, Exception exception) {
        MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage())
        );
        return modelAndViewCreator("Page not found! Please try again later.", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView systemMarathonExceptionsHandler(HttpServletRequest request, Exception exception) {
        MARATHON_EXCEPTIONS_HANDLER_LOGGER.error(
                String.format("URL: %s | Status code: %d | Caused: %s",
                        request.getRequestURL(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getMessage())
        );
        return modelAndViewCreator("Something went wrong! Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
