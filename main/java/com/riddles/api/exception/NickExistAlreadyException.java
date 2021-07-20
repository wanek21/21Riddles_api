package com.riddles.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This name is taken")
public class NickExistAlreadyException extends RuntimeException {
}
