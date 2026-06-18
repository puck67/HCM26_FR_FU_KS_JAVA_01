package com.fpt.lms.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    public String render(String markdown) {
        if (markdown == null) return "";
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
