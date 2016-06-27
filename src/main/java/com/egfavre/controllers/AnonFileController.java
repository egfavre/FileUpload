package com.egfavre.controllers;

import com.egfavre.entities.AnonFile;
import com.egfavre.services.AnonFileRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by user on 6/27/16.
 */
@Controller
public class AnonFileController {
    @Autowired
    AnonFileRepository files;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }

    @RequestMapping (path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file) throws IOException {
        int count = (int) files.count();
        if (count < 10) {

            File dir = new File("public/files");
            dir.mkdirs();

            File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);

            FileOutputStream fos = new FileOutputStream(uploadedFile);
            fos.write(file.getBytes());

            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName());
            files.save(anonFile);
        } else {
            Iterable<AnonFile> fileList = files.findAll();
            ArrayList<Integer> intList = new ArrayList<>();
            for (AnonFile f : fileList) {
                int index = f.getId();
                intList.add(index);
            }
            Collections.sort(intList);
            int least = intList.get(0);
            files.delete(least);
        }
        return "redirect:/";
    }
}

