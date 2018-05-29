package com.landowner.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.landowner.listener.server.InitService;

@Component
public class ServerListener implements ServletContextListener {

	private InitService initService;
	

	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		initService = applicationContext.getBean(InitService.class);
		initService.start();
		welcome();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		initService.close();
	}

   private static void welcome() {
        System.out.println();
        System.out.println();
        System.out.println("*       *       *\t* * * *\t\t*    \t     ***\t");
        System.out.println(" *     * *     * \t*      \t\t*    \t   **   \t");
        System.out.println("  *   *   *   *  \t* * * *\t\t*    \t *      \t");
        System.out.println("   * *     * *   \t*      \t\t*    \t   **   \t");
        System.out.println("    *       *    \t* * * *\t\t* * *\t     ***\t");
        System.out.println();
        System.out.println();
    }
}
