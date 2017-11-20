package main.java.pf.aop;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MapBeanAspect {

	private static final Logger LOGGER = Logger.getLogger(MapBeanAspect.class.getName());
	private static final Integer DELAY_IN_MINUTES = 15;

	@Before("execution(** com.gsys.bimr.pf.Map.generateRequest(..))")
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
		LOGGER.info("after" + joinPoint.getSignature().getName());
	}
}
