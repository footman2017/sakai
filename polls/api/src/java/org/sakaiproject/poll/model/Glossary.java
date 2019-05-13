/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.sakaiproject.component.cover.ServerConfigurationService;

/**
 *
 * @author Asus
 */
public class Glossary {
    private static final String ISO8601_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ISO8601_DATE_FORMAT_STRING);
    private long id;
    private String term;
    private String description;
    private String category;
    
    public Glossary() {
        this.term = "";
        this.description = "";
        this.category = "";
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
        .append(this.term)
        .append(this.description)
        .append(this.category)
        .toString();
    }
}
