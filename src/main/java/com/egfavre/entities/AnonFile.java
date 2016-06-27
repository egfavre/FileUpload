package com.egfavre.entities;

import javax.persistence.*;

/**
 * Created by user on 6/27/16.
 */
@Entity
@Table(name="files")
public class AnonFile {
    @GeneratedValue
    @Id
    int id;


    @Column(nullable = false)
    String originalFilename;

    @Column(nullable = false)
    String realFilename;

    @Column(nullable = false)
    Boolean perm;

    public AnonFile() {
    }

    public AnonFile(String originalFilename, String realFilename, Boolean perm) {
        this.originalFilename = originalFilename;
        this.realFilename = realFilename;
        this.perm = perm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getRealFilename() {
        return realFilename;
    }

    public void setRealFilename(String realFilename) {
        this.realFilename = realFilename;
    }

    public Boolean getPerm() {
        return perm;
    }

    public void setPermananet(Boolean perm) {
        this.perm = perm;
    }
}
