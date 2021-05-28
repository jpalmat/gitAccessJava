package com.example.git.controller;

import com.example.git.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

    @Autowired
    private GithubService githubService;

    @GetMapping("/branches")
    public String getBranches(){
        return githubService.getBranchedFromGithub();
    }
}