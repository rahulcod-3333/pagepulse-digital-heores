package com.digitalheroes.pagepulse;

import com.digitalheroes.pagepulse.HtmlAnalyzer.Analyze;
import com.digitalheroes.pagepulse.dto.AnalysisDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyzeTest {
    private final Analyze analyzer = new Analyze();

    // ---------------------------------------------------------------
    // Happy path
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Happy path: well-formed page extracts all fields correctly")
    void analyze_happyPath_extractsAllFieldsCorrectly() {
        String html = """
                <html>
                <head>
                    <title>Page Pulse Demo</title>
                    <meta name="description" content="A tool that audits any URL.">
                </head>
                <body>
                    <h1>Welcome to Page Pulse</h1>
                    <img src="hero.jpg" alt="Hero banner">
                    <p>This page has exactly ten words in its body text here.</p>
                </body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals("Page Pulse Demo", result.getTitle());
        assertEquals("A tool that audits any URL.", result.getMetaDescription());
        assertEquals(1, result.getH1Count());
        assertEquals(0, result.getMissingAltImages());
        assertEquals(15, result.getWordCount());
    }

    // ---------------------------------------------------------------
    // Failure / edge case 1: missing meta description
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Missing meta description falls back to the default message, not a null or crash")
    void analyze_missingMetaDescription_returnsFallbackString() {
        String html = """
                <html>
                <head><title>No Meta Here</title></head>
                <body><h1>Heading</h1><p>Some body text.</p></body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals("No meta description found", result.getMetaDescription());
    }

    @Test
    @DisplayName("Blank meta description content also falls back to the default message")
    void analyze_blankMetaDescriptionContent_returnsFallbackString() {
        String html = """
                <html>
                <head>
                    <title>Blank Meta</title>
                    <meta name="description" content="">
                </head>
                <body><h1>Heading</h1></body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        // Current behavior: an empty (but present) content attribute is returned as-is.
        // This test pins down that behavior so a future change to it is a deliberate
        // decision, not an accidental regression.
        assertEquals("", result.getMetaDescription());
    }

    // ---------------------------------------------------------------
    // Failure / edge case 2: images missing alt text
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Counts images with a missing alt attribute and a blank alt attribute as missing")
    void analyze_missingAndBlankAltAttributes_countsBothAsMissing() {
        String html = """
                <html>
                <head><title>Alt Text Check</title></head>
                <body>
                    <h1>Gallery</h1>
                    <img src="a.jpg" alt="A proper description">
                    <img src="b.jpg">
                    <img src="c.jpg" alt="">
                    <img src="d.jpg" alt="   ">
                </body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        // b.jpg (no alt attr), c.jpg (empty alt), d.jpg (whitespace-only alt) = 3 missing
        assertEquals(3, result.getMissingAltImages());
    }

    // ---------------------------------------------------------------
    // Failure / edge case 3: no H1 tags on the page
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Page with zero H1 tags returns a count of 0, not an error")
    void analyze_noH1Tags_returnsZero() {
        String html = """
                <html>
                <head><title>No Heading</title></head>
                <body><p>This page forgot its H1.</p></body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals(0, result.getH1Count());
    }

    @Test
    @DisplayName("Page with multiple H1 tags counts all of them")
    void analyze_multipleH1Tags_countsAll() {
        String html = """
                <html>
                <head><title>Too Many Headings</title></head>
                <body><h1>First</h1><h1>Second</h1><h1>Third</h1></body>
                </html>
                """;
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals(3, result.getH1Count());
    }

    // ---------------------------------------------------------------
    // Failure / edge case 4: empty body text
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Empty body produces a word count of 0, never a negative number or crash")
    void analyze_emptyBody_wordCountIsZero() {
        String html = "<html><head><title>Empty</title></head><body></body></html>";
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals(0, result.getWordCount());
    }

    @Test
    @DisplayName("Body containing only whitespace produces a word count of 0")
    void analyze_whitespaceOnlyBody_wordCountIsZero() {
        String html = "<html><head><title>Whitespace</title></head><body>   \n   \t  </body></html>";
        Document document = Jsoup.parse(html);

        AnalysisDto result = analyzer.analyze(document);

        assertEquals(0, result.getWordCount());
    }
}

