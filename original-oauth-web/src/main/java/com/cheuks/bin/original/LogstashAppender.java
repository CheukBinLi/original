package com.cheuks.bin.original;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Plugin(name = "LogstashAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class LogstashAppender extends SocketAppender {

    protected LogstashAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class LogstashAppenderBuilder extends SocketAppender.AbstractBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<LogstashAppender> {

        @PluginBuilderAttribute
        private String pattern = PatternLayout.DEFAULT_CONVERSION_PATTERN;
        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;
        @PluginConfiguration
        private Configuration config;
        @PluginElement("Replace")
        private RegexReplacement replace = RegexReplacement.createRegexReplacement(Pattern.compile("\\r\\n|\\n\\r|\\n|\\r|\\t"), new String(new char[]{32}));
        @PluginBuilderAttribute
        private Charset charset;
        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;
        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi = false;
        @PluginBuilderAttribute
        private String headerPattern;
        @PluginBuilderAttribute
        private String footerPattern;
        @PluginElement("pairs")
        private KeyValuePair[] pairs;

        @Override
        public LogstashAppender build() {
            boolean immediateFlush = isImmediateFlush();
            final boolean bufferedIo = isBufferedIo();
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
            final Layout<? extends Serializable> layout = new LogstashLayout(config, replace, pattern, patternSelector, charset, alwaysWriteExceptions, noConsoleNoAnsi, headerPattern, footerPattern, additionalLogAttributes);

            final String name = getName();
            if (name == null) {
                AbstractLifeCycle.LOGGER.error("No name provided for SocketAppender");
                return null;
            }

            final Protocol protocol = getProtocol();
            final Protocol actualProtocol = protocol != null ? protocol : Protocol.TCP;
            if (actualProtocol == Protocol.UDP) {
                immediateFlush = true;
            }

            final AbstractSocketManager manager = SocketAppender.createSocketManager(name, actualProtocol, getHost(), getPort(), getConnectTimeoutMillis(), getSslConfiguration(), getReconnectDelayMillis(), getImmediateFail(), layout, getBufferSize(), getSocketOptions());

            return new LogstashAppender(name, layout, getFilter(), manager, isIgnoreExceptions(), !bufferedIo || immediateFlush, getAdvertise() ? getConfiguration().getAdvertiser() : null);
        }

    }

    @PluginBuilderFactory
    public static LogstashAppenderBuilder build() {
        return new LogstashAppenderBuilder();
    }

}
