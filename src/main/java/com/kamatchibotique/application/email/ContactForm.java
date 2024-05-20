package com.kamatchibotique.application.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ContactForm {
    @NotEmpty
    private String name;
    @NotEmpty
    private String subject;
    @Email
    private String email;
    @NotEmpty
    private String comment;

    public ContactForm() {
    }

    public ContactForm(String name, String subject, String email, String comment) {
        this.name = name;
        this.subject = subject;
        this.email = email;
        this.comment = comment;
    }

    public @NotEmpty String getName() {
        return name;
    }

    public void setName(@NotEmpty String name) {
        this.name = name;
    }

    public @NotEmpty String getSubject() {
        return subject;
    }

    public void setSubject(@NotEmpty String subject) {
        this.subject = subject;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @NotEmpty String getComment() {
        return comment;
    }

    public void setComment(@NotEmpty String comment) {
        this.comment = comment;
    }
}
