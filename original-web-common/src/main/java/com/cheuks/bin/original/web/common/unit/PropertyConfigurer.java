package com.cheuks.bin.original.web.common.unit;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.cheuks.bin.original.common.util.ConfigManager;

/***
 * 
 * @Title: original-web-common
 * @Description: 配置文件管理器
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月4日 下午4:26:52
 *
 */
public final class PropertyConfigurer extends PropertyPlaceholderConfigurer {

    private ConfigManager configManager;

    final String configManagerBeanName = "configManager";

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

        if (null == configManager) {
            synchronized (this) {
                beanFactory = new DefaultListableBeanFactory(beanFactoryToProcess);
                if (beanFactory.containsBean(configManagerBeanName)) {
                    this.configManager = beanFactory.getBean(ConfigManager.class);
                } else {
                    try {
                        this.configManager = beanFactory.getBean(ConfigManager.class);
                    } catch (Exception e) {
                        RootBeanDefinition bean = new RootBeanDefinition(ConfigManager.class);
                        beanFactory.registerBeanDefinition("configManager", bean);
                        this.configManager = beanFactory.getBean(ConfigManager.class);
                    }

                }
            }
        }
        configManager.add(props);
        super.processProperties(beanFactoryToProcess, props);
    }

}
