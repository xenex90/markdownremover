package com.kyj.markdownremover.tableRemover;

import com.kyj.markdownremover.MarkdownremoverApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;

public class removeTableFormat {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownremoverApplication.class);

    public static LinkedHashMap removeTableMethod(String text) {
        // 정규식을 사용하여 테이블 형식 제거
        text = text.replaceAll("\\|\\s*:?[-]+\\s*:?\\|\\s*:?[-]+\\s*:?\\|\\s*:?[-]+\\s*:?\\|", "").replace("\r","").replace("\t","");
        BufferedReader reader = new BufferedReader(new StringReader(text));
        StringBuilder result = new StringBuilder();
        String line;
        LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        try {
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                // 테이블 구분선이 아닌 라인 처리
                if (line.trim().startsWith("|")) {

                    // 앞뒤 '|'와 불필요한 공백 제거
                    line = line.trim().replaceAll("\\|\\s*", "").replaceAll("\\s*\\|$", "");
                    line = line.replaceAll("\\s*\\|\\s*", " ");

                }
                map.put(line,true);
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put(result.toString(), true);
        // 최종 결과 문자열 반환
        return map;
    }
}

