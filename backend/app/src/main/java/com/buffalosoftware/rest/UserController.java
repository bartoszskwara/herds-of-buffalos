package com.buffalosoftware.rest;

import com.buffalosoftware.api.user.IUserService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.building.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("")
    public ResponseEntity getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BaseDtoList<>(users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserById(@NotNull @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping("/current")
    public ResponseEntity getCurrentLoggedUser() {
        //TODO temporary implementation
        return ResponseEntity.ok(userService.findRandomUserWithMultipleCities());
    }
}