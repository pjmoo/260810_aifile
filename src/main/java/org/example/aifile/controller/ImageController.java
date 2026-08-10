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
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    @GetMapping
    public String formPage() {
        return "image/form";
    }

    @PostMapping
    public String uploadImage(@RequestParam MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        System.out.println("file = " + file);
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());
        redirectAttributes.addFlashAttribute("msg", "파일 업로드 완료");
        return "redirect:/image";
    }
}
