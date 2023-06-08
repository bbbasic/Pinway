package com.example.userservice.controllers;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.models.User;
import com.example.userservice.models.UserVisibilityType;
import com.example.userservice.services.UserService;
import com.example.userservice.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/api") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserService userService;


    @PostMapping(path = "/users") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity AddUser (@Valid @RequestBody User requestBody) {
        User user = userService.Create(requestBody);
        return ResponseEntity.status(201).body(user);

    }
    @GetMapping(path = "/users")
    public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers() {
        // This returns a JSON or XML with the users
        Iterable<User> notificationList = userService.List();
        return ResponseEntity.status(200).body(notificationList);
    }

    @GetMapping(path="/users/{id}")
    public @ResponseBody ResponseEntity GetDetails( @PathVariable("id") Integer id) {
        User user = userService.Details(id);
        return ResponseEntity.status(200).body(user);
    }
    @DeleteMapping(path = "/users/{id}")
    public @ResponseBody ResponseEntity Delete(@PathVariable("id") Integer id) {
        userService.Delete(id);
        return ResponseEntity.status(204).build();

    }

    @PutMapping("/users/{id}")
    public @ResponseBody ResponseEntity Update(@PathVariable("id") Integer id, @Valid @RequestBody User requestBody) {
        User updated = userService.Update(id, requestBody);
        return ResponseEntity.status(200).body(updated);

    }

    @GetMapping(path="/userVisibilityType")
    public @ResponseBody ResponseEntity GetAllUserVisibilityTypes() {
        Iterable<UserVisibilityType> userVisibilityTypes = userService.ListUserVisibilityTypes();
        return ResponseEntity.status(200).body(userVisibilityTypes);

    }

    // FOLLOWERS AND FOLLOWING

    @PostMapping(path="/users/{id}/followers")
    public @ResponseBody ResponseEntity AddFollower (@PathVariable("id") Integer id, @Valid @RequestBody Integer requestBody) {
        User user = userService.AddFollower(id, requestBody);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping(path="/users/{id}/followers")
    public @ResponseBody ResponseEntity getAllFollowersForUser (@PathVariable("id") Integer id) {
        List<UserDTO> userDTO = userService.GetAllFollowersForUser(id);
        return ResponseEntity.status(200).body(userDTO);
    }

    @PostMapping("/user/{username}/role/{name}")
    public ResponseEntity<User> addRoleToUser(@PathVariable String username, @PathVariable String name) {
        User user = userService.addRoleToUser(username, name);
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.status(201).body(newUser);
    }


}