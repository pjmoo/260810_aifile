package org.example.aifile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PDFController {
    @GetMapping
    public String formPage() {
        return "pdf/form";
    }

    @PostMapping
    public String uploadPDF(@RequestParam MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        System.out.println("file = " + file);
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());
        redirectAttributes.addFlashAttribute("msg", "파일 업로드 완료");
        return "redirect:/pdf";
    }
}
