package com.cheuks.bin.original;

import com.cheuks.bin.original.common.util.conver.JsonMapper;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Plugin(name = "LogstashLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class LogstashLayout extends AbstractStringLayout {

    protected static transient SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private PatternLayout patternLayout;
    protected Map<String, String> additionalLogAttributes;
    protected volatile String additionalLogAttribute;
    static final String CONTENT_TYPE = "application/json";
    protected String headerPattern;
    protected String footerPattern;

    JsonMapper json = JsonMapper.newInstance(true);

    @Override
    public String toSerializable(LogEvent event) {
        return messageConverHandle(event.getLoggerName(), patternLayout.toSerializable(event), event.getLevel().name(), DATE_FORMAT.format(new Date(event.getTimeMillis())), additionalLogAttributes);
    }

    @SuppressWarnings("unchecked")
    private String messageConverHandle(String triggerClass, String message, String level, String time, Map<String, String> additionalLogAttributes) {
        if (null == additionalLogAttribute && null != additionalLogAttributes) {
            try {
                additionalLogAttribute = json.writer(null, null, true, true, false, additionalLogAttributes);
                if (null != additionalLogAttribute && additionalLogAttribute.length() > 0) {
                    additionalLogAttribute = additionalLogAttribute.substring(1, additionalLogAttribute.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StringBuilder result = new StringBuilder("{");
        if (!StringUtil.isBlank(additionalLogAttribute)) {
            result.append(additionalLogAttribute).append(",");
        }
        result.append("\"time\":\"").append(time).append("\"").append(",");
        result.append("\"level\":\"").append(level).append("\"").append(",");
        result.append("\"trigger_class\":\"").append(triggerClass).append("\"").append(",");
        result.append("\"message\":\"").append(headerPattern).append(message).append(footerPattern).append("\"");
        result.append("}\r\n");
        return result.toString();
    }

    @SuppressWarnings("deprecation")
    public LogstashLayout(Configuration config, RegexReplacement replace, String eventPattern, PatternSelector patternSelector, Charset charset, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, String headerPattern, String footerPattern, Map<String, String> additionalLogAttributes) {
        super(config, charset, PatternLayout.createSerializer(config, replace, headerPattern, null, patternSelector, alwaysWriteExceptions, noConsoleNoAnsi), PatternLayout.createSerializer(config, replace, footerPattern, null, patternSelector, alwaysWriteExceptions, noConsoleNoAnsi));
        this.patternLayout = PatternLayout.newBuilder().withPattern(eventPattern).withPatternSelector(patternSelector).withConfiguration(config).withRegexReplacement(replace).withCharset(charset).withAlwaysWriteExceptions(alwaysWriteExceptions).withNoConsoleNoAnsi(noConsoleNoAnsi).withHeader(headerPattern).withFooter(footerPattern).build();
        this.additionalLogAttributes = additionalLogAttributes;
        this.headerPattern = null == headerPattern ? "" : headerPattern;
        this.footerPattern = null == footerPattern ? "" : footerPattern;
    }

    @PluginFactory
    public static LogstashLayout createLayout(@PluginAttribute(value = "pattern", defaultString = PatternLayout.DEFAULT_CONVERSION_PATTERN) final String pattern, @PluginElement("PatternSelector") final PatternSelector patternSelector, @PluginConfiguration final Configuration config, @PluginElement("Replace") final RegexReplacement replace, @PluginAttribute(value = "charset") final Charset charset,
                                              @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) final boolean alwaysWriteExceptions, @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) final boolean noConsoleNoAnsi, @PluginAttribute("header") final String headerPattern, @PluginAttribute("footer") final String footerPattern, @PluginElement("pairs") KeyValuePair[] pairs) {
        final Map<String, String> additionalLogAttributes = new HashMap<String, String>();
        if (pairs != null && pairs.length > 0) {
            for (final KeyValuePair pair : pairs) {
                final String key = pair.getKey();
                if (key == null) {
                    LOGGER.error("A null key is not valid in MapFilter");
                    continue;
                }
                final String value = pair.getValue();
                if (value == null) {
                    LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
                    continue;
                }
                if (additionalLogAttributes.containsKey(key)) {
                    LOGGER.error("Duplicate entry for key: {} is forbidden!", key);
                }
                additionalLogAttributes.put(key, value);
            }
        }
        return new LogstashLayout(config, replace, pattern, patternSelector, charset, alwaysWriteExceptions, noConsoleNoAnsi, headerPattern, footerPattern, additionalLogAttributes);
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE + "; charset=" + this.getCharset();
    }

    @Override
    public byte[] getFooter() {
        return null;
    }

    @Override
    public byte[] getHeader() {
        return null;
    }

}
