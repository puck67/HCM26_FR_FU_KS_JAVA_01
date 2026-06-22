package com.example.asm6.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public String parse(String markdown) {
        if (markdown == null) {
            return "";
        }
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
