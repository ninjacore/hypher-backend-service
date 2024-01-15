package io.hypher.backendservice.platformdata.controller;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.dto.GildedProfilePage;
import io.hypher.backendservice.platformdata.model.Profile;
import io.hypher.backendservice.platformdata.service.ProfileService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class ProfilePageController {

    // @Autowired
    // ProfileService profileService;

    // @GetMapping("/profilePage/{handle}")
    // public ProfilePage getMethodName(@RequestParam String param) {

    //     return null;
    // }

}
