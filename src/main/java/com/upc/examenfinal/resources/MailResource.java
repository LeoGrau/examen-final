package com.upc.examenfinal.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailResource {
    public String body;
    public String from;
    public String sendTo;
    public String Subject;
}
