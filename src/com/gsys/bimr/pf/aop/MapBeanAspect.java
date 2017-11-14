package com.gsys.bimr.pf.aop;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;

public class MapBeanAspect {

	private static final Logger LOGGER = Logger.getLogger(MapBeanAspect.class.getName());
	private static final Integer DELAY_IN_MINUTES = 15;

	@After("com.gsys.bimr.pf.MapBean.generateRequest())")
	public void callScheduler(JoinPoint joinPoint) {

		long delay = 1000 * 60L * DELAY_IN_MINUTES;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// just wait until the delay time passed
				// AOP it's used, so it is not necessary to
				// make any call from here
			}
		}, 0, delay);
		LOGGER.info("before" + joinPoint.getSignature().getName());
	}
}
