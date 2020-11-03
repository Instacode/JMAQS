package com.magenic.jmaqs.selenium;

import com.deque.html.axecore.results.Node;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.ResultType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

public class HTMLReport {

  private HTMLReport() {
  }

  public static void createAxeHtmlReport(WebDriver webDriver, String destination)
      throws IOException, ParseException {
    AxeBuilder axeBuilder = new AxeBuilder();
    createAxeHtmlReport(webDriver, axeBuilder.analyze(webDriver), destination);
  }

  public static void createAxeHtmlReport(WebDriver webDriver, WebElement context,
      String destination) throws IOException, ParseException {
    AxeBuilder axeBuilder = new AxeBuilder();
    createAxeHtmlReport(webDriver, axeBuilder.analyze(webDriver, context), destination);
  }

  public static void createAxeHtmlReport(SearchContext context, Results results, String destination)
      throws IOException, ParseException {
    // Get the unwrapped element if we are using a wrapped element
    context = (context instanceof WrapsElement)
        ? ((WrapsElement) context).getWrappedElement() : context;

    HashSet<String> selectors = new HashSet<>();
    int violationCount = getCount(results.getViolations(), selectors);
    int incompleteCount = getCount(results.getIncomplete(), selectors);
    int passCount = getCount(results.getPasses(), selectors);
    int inapplicableCount = getCount(results.getInapplicable(), selectors);

    String stringBuilder = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">" + "<head>"
        + "<meta charset=\"utf-8\"><title>Accessibility Check</title><style></style>"
        + "</head>" + "<body></body>" + "</html>";
    Document doc = Jsoup.parse(stringBuilder);

    Element body = doc.body();
    String content = ".fullImage{"
        + System.lineSeparator()
        + "content: url('" + getDataImageString(context)
        + "};border: 1px solid black;margin-left:1em;"
        + "} .fullImage:hover {transform:scale(2.75);transform-origin: top left;} p {}"
        + ".wrap .wrapTwo .wrapThree{margin:2px;max-width:70vw;}"
        + ".wrapOne {margin-left:1em;overflow-wrap:anywhere;}"
        + ".wrapTwo {margin-left:2em;overflow-wrap:anywhere;}"
        + ".wrapThree {margin-left:3em;overflow-wrap:anywhere;}"
        + ".emOne {margin-left:1em;overflow-wrap:anywhere;}"
        + ".emTwo {margin-left:2em;overflow-wrap:anywhere;}"
        + ".emThree {margin-left:3em;overflow-wrap:anywhere;}"
        + ".majorSection{border: 1px solid black;}" + ".findings{border-top:1px solid black;}"
        + ".htmlTable{border-top:double lightgray;width:100%;display:table;}";
    doc.select("style").append(content);

    Element element = new Element("h1");
    element.text("Accessibility Check");
    body.appendChild(element);

    element = new Element("h3");
    element.text("Context");
    body.appendChild(element);

    element = new Element("div");
    element.attributes().put("class", "emOne");
    element.attributes().put("id", "reportContext");
    element.text("Url: " + results.getUrl());
    element.appendChild(new Element("br"));
    element.appendText("Orientation: "+ results.getTestEnvironment().getOrientationType());
    element.appendChild(new Element("br"));
    element.appendText("Size: " + results.getTestEnvironment().getwindowWidth() + " x  "
        + results.getTestEnvironment().getWindowHeight());
    element.appendChild(new Element("br"));
    element.appendText("Time: " + getDateFormat(results.getTimestamp()));
    element.appendChild(new Element("br"));
    element.appendText("User agent: " + results.getTestEnvironment().getUserAgent());
    element.appendChild(new Element("br"));
    element.appendText("Using: " + results.getTestEngine().getName() + " ("
         + results.getTestEngine().getVersion() + ")");
    body.appendChild(element);

    element = new Element("h3");
    element.appendText("Counts:");
    body.appendChild(element);

    element = new Element("div");
    element.attr("class", "emOne");

    element.text(" Violation: " + violationCount);
    element.appendChild(new Element("br"));
    element.appendText(" Incomplete: " + incompleteCount);
    element.appendChild(new Element("br"));
    element.appendText(" Pass: " + passCount);
    element.appendChild(new Element("br"));
    element.appendText(" Inapplicable: " + inapplicableCount);
    body.appendChild(element);

    element = new Element("h3");
    element.appendText("Image:");
    body.appendChild(element);

    element = new Element("img");
    element.attributes().put("class", "fullImage");
    element.attributes().put("width", "33%");
    element.attributes().put("height", "auto");
    body.appendChild(element);

    if (results.isErrored()) {
      element = new Element("h2");
      element.appendText("SCAN ERRORS:");
      body.appendChild(element);

      Element error = new Element("div");
      error.attributes().put("id", "ErrorMessage");
      error.appendText(StringEscapeUtils.escapeHtml4(results.getErrorMessage()));
      body.appendChild(error);
    }

    body.appendChild(new Element("br"));
    body.appendChild(new Element("br"));

    Element area = new Element("div");

    if (violationCount > 0) {
      area.appendChild(new Element("br"));
      area.appendChild(getReadableAxeResults(results.getViolations(), ResultType.Violations.name()));
    }

    if (incompleteCount > 0) {
      area.appendChild(new Element("br"));
      area.appendChild(getReadableAxeResults(results.getIncomplete(), ResultType.Incomplete.name()));
    }

    if (passCount > 0) {
      area.appendChild(new Element("br"));
      area.appendChild(getReadableAxeResults(results.getPasses(), ResultType.Passes.name()));
    }

    if (inapplicableCount > 0) {
      area.appendChild(new Element("br"));
      area.appendChild(getReadableAxeResults(results.getInapplicable(), ResultType.Inapplicable.name()));
    }

    body.appendChild(area);
    FileUtils.writeStringToFile(new File(destination), doc.outerHtml(), StandardCharsets.UTF_8);
  }



  private static Element getReadableAxeResults(List<Rule> results, String type) {
    Element section = new Element("div");
    section.attr("class", "majorSection");
    section.attr("id", type + "Section");

    Element childEl = new Element("h2");
    childEl.text(type);
    section.appendChild(childEl);

    int loops = 1;

    for (Rule element : results) {
      childEl = new Element("div");
      childEl.attr("class", "findings");
      childEl.appendText(loops++ + ": " + StringEscapeUtils.escapeHtml4(element.getHelp()));
      section.appendChild(childEl);

      Element childEl2 = new Element("div");
      childEl2.attr("class", "emTwo");
      childEl2.text("Description: "+ StringEscapeUtils.escapeHtml4(element.getDescription()));
      childEl2.appendChild(new Element("br"));
      childEl2.appendText("Help: " + StringEscapeUtils.escapeHtml4(element.getHelp()));
      childEl2.appendChild(new Element("br"));
      childEl2.appendText("Help URL: ");

      Element link = new Element("a");
      link.attr("href", element.getHelpUrl());
      link.text(element.getHelpUrl());

      childEl2.appendChild(link);
      childEl2.appendChild(new Element("br"));

      if (!element.getImpact().isEmpty()) {
        childEl2.appendText("Impact: " + StringEscapeUtils.escapeHtml4(element.getImpact()));
        childEl2.appendChild(new Element("br"));
      }

      childEl2.appendText("Tags: ").append(StringEscapeUtils.escapeHtml4(
          String.join(", ", element.getTags())));
      childEl2.appendChild(new Element("br"));

      if (!element.getNodes().isEmpty()) {
        childEl2.appendText("Element(s):");
        childEl2.appendChild(new Element("br"));
      }

      childEl.appendChild(childEl2);

      for (Node item : element.getNodes()) {
        Element elementNodes = new Element("div");
        elementNodes.attr("class", "htmlTable");
        childEl.appendChild(elementNodes);

        Element htmlAndSelectorWrapper = new Element("div");
        htmlAndSelectorWrapper.attr("class", "emThree");
        htmlAndSelectorWrapper.text("Html:");
        htmlAndSelectorWrapper.appendChild(new Element("br"));
        elementNodes.appendChild(htmlAndSelectorWrapper);

        Element htmlAndSelector = new Element("p");
        htmlAndSelector.attr("class", "wrapOne");
        htmlAndSelector.html(item.getHtml());
        htmlAndSelector.text(item.getHtml());
        htmlAndSelectorWrapper.appendChild(htmlAndSelector);
        htmlAndSelectorWrapper.appendText("Selector(s):");

        htmlAndSelector = new Element("p");
        htmlAndSelector.attr("class", "wrapTwo");

        for (Object target : Collections.singletonList(item.getTarget())) {
          String targetString = target.toString().replace("[", "").replace("]", "");
          htmlAndSelector.text(targetString);
          htmlAndSelector.html(StringEscapeUtils.escapeHtml4(targetString));
        }

        htmlAndSelectorWrapper.appendChild(htmlAndSelector);
      }
    }

    return section;
  }

  private static int getCount(List<Rule> results, HashSet<String> uniqueList) {
    int count = 0;
    for (Rule item : results) {
      for (Node node : item.getNodes()) {
        for (Object target : Collections.singletonList(node.getTarget())) {
          count++;
          uniqueList.add(target.toString());
        }
      }

      // Still add one if no targets are included
      if (item.getNodes().isEmpty()) {
        count++;
      }
    }
    return count;
  }

  private static String getDataImageString(SearchContext context) {
    TakesScreenshot newScreen = (TakesScreenshot) context;
    return "data:image/png;base64," + newScreen.getScreenshotAs(OutputType.BASE64) + "');";
  }

  private static String getDateFormat(String timestamp) throws ParseException {
    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timestamp);
    return new SimpleDateFormat("dd-MMM-yy HH:mm:ss Z").format(date);
  }
}