package org.example.aifile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PDFController {
    @GetMapping
    public String formPage() {
        return "pdf/form";
    }
}
