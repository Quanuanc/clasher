package org.example.clasher.controller;

import org.example.clasher.pojo.Result;
import org.example.clasher.task.FileUpdateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@RestController
public class UpdateController {
    private static final Logger log = LoggerFactory.getLogger(UpdateController.class);
    private final FileUpdateTask fileUpdateTask;

    public UpdateController(FileUpdateTask fileUpdateTask) {
        this.fileUpdateTask = fileUpdateTask;
    }

    @GetMapping("/update")
    public Result update() {
        Result result = new Result();
        try {
            fileUpdateTask.updateFile();
            result.setSuccess(true);
        } catch (URISyntaxException | IOException | ExecutionException | InterruptedException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return result;
    }
}
