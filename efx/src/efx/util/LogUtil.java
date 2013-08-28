package efx.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class LogUtil {

    private final static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    private static boolean inited = false;
    
    public static void initErrorLogStream() {
    	if(!inited){
    		PrintStream syserr = System.err;
            try {
                Log4jErrorPrintStream errStream = new Log4jErrorPrintStream(System.err);
                System.setErr(errStream);
            } catch (Exception e) {
                System.setErr(syserr);
            }
            inited = true;
    	}
        
    }

    public static String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private static class Log4jErrorPrintStream extends PrintStream {

        Log4jErrorPrintStream(OutputStream out) {
            super(out, true); 
        }

        public void println(Object obj) {
            if (obj instanceof Throwable) {              
                if (logger != null) {
                	logger.error(LogUtil.toString((Throwable) obj));
                }
            }
        }
    }
}
