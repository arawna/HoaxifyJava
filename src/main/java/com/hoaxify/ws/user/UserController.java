package com.hoaxify.ws.user;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shered.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/users")
    public GenericResponse createUser(@Valid @RequestBody User user) {
        userService.save(user);
        return new GenericResponse("User Created");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception) {
        ApiError error = new ApiError(400,"Validation Error","/api/1.0/users");
        Map<String,String> validationErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationErrors.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        error.setValidationErrors(validationErrors);
        return error;
    }
}
