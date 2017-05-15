package com.cheuks.bin.original.rmi;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class AnyThingTest {

	@Test
	public void t1() {
		BeanDefinition s = new RootBeanDefinition();
		s.setScope("prototype");
	}

}
