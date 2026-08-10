package org.example.aifile.controller;

import lombok.RequiredArgsConstructor;
import org.example.aifile.service.PDFService;
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
    private final PDFService pDFService;

    @GetMapping
    public String formPage() {
        return "pdf/form";
    }

    @PostMapping
    public String uploadPDF(@RequestParam MultipartFile file,
                            RedirectAttributes redirectAttributes) {
//        System.out.println("file = " + file);
//        System.out.println(file.getContentType());
//        System.out.println(file.getOriginalFilename());
        int size = pDFService.uploadPDF(file);
        redirectAttributes.addFlashAttribute(
                "msg", "파일 업로드 완료 : %d개의 청크".formatted(size));
        return "redirect:/pdf";
    }

    @PostMapping("/rag")
    public String rag(@RequestParam String query,
                      RedirectAttributes redirectAttributes) {
        String answer = pDFService.ragChat(query);
        redirectAttributes.addFlashAttribute("answer", answer);
        return "redirect:/pdf";
    }
}
