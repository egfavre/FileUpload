package com.egfavre.controllers;

import com.egfavre.entities.AnonFile;
import com.egfavre.services.AnonFileRepository;
import com.egfavre.utils.PasswordStorage;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
    public String upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, PasswordStorage.CannotPerformOperationException {
        int count = (int) files.count();
        if (count < 10) {

            File dir = new File("public/files");
            dir.mkdirs();

            File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);

            FileOutputStream fos = new FileOutputStream(uploadedFile);
            fos.write(file.getBytes());

            String permanent = request.getParameter("perm");
            Boolean perm = false;
            if (permanent != null){
                perm = true;
            }
            String comment = request.getParameter("comment");

            String password = request.getParameter("password");

            AnonFile anonFile = new AnonFile();
            if (password != null){
                anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), perm, comment, PasswordStorage.createHash(password));

            }
            else {
                anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), perm, comment);}
            files.save(anonFile);
        } else {
            Iterable<AnonFile> fileList = files.findByPerm(false);
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

