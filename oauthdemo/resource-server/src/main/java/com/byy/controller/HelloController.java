package com.byy.controller;


import com.byy.model.entities.AttrEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
public class HelloController {

    @PreAuthorize("hasAuthority('read')")
    @GetMapping("/hello")
    public Object getUser(Authentication authentication, HttpServletRequest request) {
        AttrEntity attrEntity = new AttrEntity();
        attrEntity.setAttrName("dsad");

        return attrEntity;
    }
}
