package com.example.git.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GithubServiceTest {
    private GithubService githubServiceJava;
    Path directory = Paths.get("C:\\Users\\jpalma7\\Documents\\git");
    String remoteUrl ="https://github.com/jpalmat/androidRemember.git";

    @BeforeEach
    void init() {
        githubServiceJava = new GithubService();
    }

    @Test
    void  whenNoBranch_shouldReturnMaster() {
        ReflectionTestUtils.setField(githubServiceJava, "url", "https://api.github.com/repos/jpalmat/androidRemember/branches");
        String expected = "master";
        String actual = githubServiceJava.getBranchedFromGithub();
        assertTrue(actual.contains(expected));
    }

    @Test
    void createdBranch1_shouldReturnBranch1() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(githubServiceJava, "url", "https://api.github.com/repos/jpalmat/androidRemember/branches");
        createBranch("BR1");

        String expected = "BR1";
        String actual = githubServiceJava.getBranchedFromGithub();
        assertTrue(actual.contains(expected));

        deleteBranch("BR1");

    }

    @Test
    void created2Branches_shouldReturnBoth() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(githubServiceJava, "url", "https://api.github.com/repos/jpalmat/androidRemember/branches");
        createBranch("BR1");
        createBranch("BR2");

        String expectedBr1 = "BR1";
        String expectedBr2 = "BR2";

        String actual = githubServiceJava.getBranchedFromGithub();
        assertTrue(actual.contains(expectedBr1) && actual.contains(expectedBr2));


        deleteBranch("BR1");
        deleteBranch("BR2");

    }

    private void deleteBranch(String branchName) throws IOException, InterruptedException {
        runCommand("git", "checkout","master");
        runCommand("git", "push","origin","--delete",branchName);
    }

    private void createBranch(String branchName) throws IOException, InterruptedException {
        //gitInit();
        gitNewBranch(branchName);
        addNewFile("Testfile.txt","text");
        gitAddCommit();
        gitPush(branchName);
    }

    private void gitAddRemote() throws IOException, InterruptedException {
        runCommand("git", "remote","add","origin",remoteUrl);
    }

    private void gitAddCommit() throws IOException, InterruptedException {
        runCommand("git", "commit","-am","test commit");
    }

    public void gitInit() throws IOException, InterruptedException {
        runCommand("git", "init");
    }
    public void gitPush(String brName) throws IOException, InterruptedException {
        runCommand("git", "push","-u","origin",brName);
    }
    public void gitNewBranch(String brName) throws IOException, InterruptedException {
        runCommand("git", "checkout","-b",brName);
    }
    public void runCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder()
                .command(command)
                .directory(this.directory.toFile());
        Process p = pb.start();
        printCommand(p);
    }

    private void printCommand(Process p) {
        String commandResult = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        System.out.println(commandResult);
    }

    public void addNewFile(String fileName, String text) throws IOException {
        ProcessBuilder pb = new ProcessBuilder()
                .command("cmd.exe","echo",text,">",fileName);
        Process p = pb.start();
        //printCommand(p);
    }
}