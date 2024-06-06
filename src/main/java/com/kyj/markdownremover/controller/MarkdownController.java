package com.kyj.markdownremover.controller;


import com.kyj.markdownremover.MarkdownremoverApplication;
import com.kyj.markdownremover.tableRemover.removeTableFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.regex.Pattern;

@Controller
public class MarkdownController {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownremoverApplication.class);

    public static void main(String[] args) {

    }

    @PostMapping("/remove")
    public static String removeMarkdown(@RequestParam("markdownText") String markdownText) {
        logger.info("---- markdownText 텍스트 ----");
        logger.info(markdownText);
        logger.info("---- markdownText 텍스트 ---- 끝");
        String[] lines = markdownText.split("\n");


        StringBuilder plainText = new StringBuilder();

        for (String line : lines) {
            plainText.append(removeMarkdownFromLine(line)).append("\n");
        }
        logger.info("---- 플레인 텍스트 ----");
        logger.info(String.valueOf(plainText));
        logger.info("---- 플레인 텍스트 ---- 끝");

        return "/index";
    }

    private static String removeMarkdownFromLine(String line) {
        // Remove headers
        if (line.startsWith("#")) {
            return line.replaceAll("#", "").trim();
        }

        // Remove bold and italic
        line = line.replaceAll("\\*\\*|__", "");
        line = line.replaceAll("\\*|_", "");

        // Remove blockquotes
        if (line.startsWith(">")) {
            return line.substring(1).trim();
        }

        // Remove ordered list numbers
        if (line.matches("^\\d+\\.\\s.*")) {
            return line.substring(line.indexOf('.') + 1).trim();
        }

        // Remove code block markers
        if (line.contains("```")) {
            return line.replaceAll("```", "");
        }

        // Remove images and links
        if (line.matches("!\\[.*\\]\\(.*\\)") || line.matches("\\[.*\\]\\(.*\\)")) {
            return line.replaceAll("\\[.*\\]\\(.*\\)", "");
        }

        if (line.contains("~~")) {
            return line.replaceAll("~~", "");
        }

        if (line.contains("- ") && !line.contains("|")) {
            return line.replaceAll("- ", "");
        }

        if (line.contains("---") && !line.contains("|")) {
            return line.replaceAll("---", "");
        }

        try {
//            logger.info(line);
            line = removeTableFormat.removeTableMethod(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (line.contains("|")) {
//            while (line.contains("|")) {
//                line = line.replace("|", "").replace("-", "");
//            }
//        }

        return line;
    }
}

