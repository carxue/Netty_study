package org.netty.discard.server;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	SecurityManager sm = System.getSecurityManager();
        if (sm != null)
            sm.checkPermission(new RuntimePermission("selectorProvider"));
    	String cn = System.getProperty("java.nio.channels.spi.SelectorProvider");
    	Integer num = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    	String className = StringUtil.simpleClassName(App.class);
        System.out.println(cn+":"+num+":"+className);
    }
}
