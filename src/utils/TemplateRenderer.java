package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class TemplateRenderer {
    private final Configuration cfg;

    public TemplateRenderer() throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/templates/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
    }

    public void render(String templateName, Map<String, Object> dataModel, OutputStream outputStream)
            throws TemplateException, IOException {
        Template template = cfg.getTemplate(templateName);
        try (Writer out = new OutputStreamWriter(outputStream)) {
            template.process(dataModel, out);
        }
    }
}