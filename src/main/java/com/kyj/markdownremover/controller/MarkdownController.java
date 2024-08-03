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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
public class MarkdownController {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownremoverApplication.class);

    public static void main(String[] args) {

    }

    @PostMapping("/remove")

    public static String removeMarkdown(Model model, @RequestParam("markdownText") String markdownText) {

        logger.info("---- markdownText 텍스트 ----");
        logger.info(markdownText);
        logger.info("---- markdownText 텍스트 ---- 끝");
        String[] lines = markdownText.split("\n");


        StringBuilder plainText = new StringBuilder();
        int count = 0;
        LinkedHashMap countedMap = new LinkedHashMap();
        HashMap checkMap  = new HashMap<>();
        for (String line : lines) {
            boolean checked = false;
            checkMap = removeMarkdownFromLine(line);
            Set<String> keys = checkMap.keySet();
            String tmpString = null;

            for (String key : keys) {
                tmpString = key;
            }

            count++;

            plainText.append(tmpString).append("\n");

            countedMap.put(checkMap, count);
        }
        count = 0;
        logger.info("---- 플레인 텍스트 ----");
        logger.info(String.valueOf(plainText));
        logger.info("---- 플레인 텍스트 ---- 끝");



        model.addAttribute("markdownText", markdownText);
        model.addAttribute("plainText", plainText);
        model.addAttribute("countedMap", countedMap);


        return "markdown/markdownView";
    }

    private static HashMap<String, Boolean> removeMarkdownFromLine(String line) {
        HashMap map =  new HashMap<String, Boolean>();
        // Remove headers
        if (line.startsWith("#")) {
            line = line.replaceAll("#", "").trim();
            map.put(line, true);
            return map;
        }

        // Remove bold and italic
        line = line.replaceAll("\\*\\*|__", "").trim();
        line = line.replaceAll("\\*|_", "").trim();

        // Remove blockquotes
        if (line.startsWith(">")) {
            map.put(line.substring(1).trim(), true);
            return map;
        }

        // Remove ordered list numbers
        if (line.matches("^\\d+\\.\\s.*")) {
            map.put(line.substring(line.indexOf('.') + 1).trim(), true);
            return map;
        }

        // Remove code block markers
        if (line.contains("```")) {
            map.put(line.replaceAll("```", "").trim(), true);
            return  map;
        }

        // Remove images and links
        if (line.matches("!\\[.*\\]\\(.*\\)") || line.matches("\\[.*\\]\\(.*\\)")) {
            map.put(line.replaceAll("\\[.*\\]\\(.*\\)", "").trim(), true);
            return map;
        }

        if (line.contains("~~")) {
            map.put(line.replaceAll("~~", "").trim(), true);
            return map;
        }

        if (line.contains("- ") && !line.contains("|")) {
            map.put(line.replaceAll("~~", "").trim(), true);
            return map;
        }

        if (line.contains("---") && !line.contains("|")) {
            map.put(line.replaceAll("---", "").trim(), true);
            return map;
        }

        try {

            map = removeTableFormat.removeTableMethod(line);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}

