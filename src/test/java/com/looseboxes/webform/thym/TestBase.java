package com.looseboxes.webform.thym;

import java.util.Arrays;

/**
 * @author hp
 */
public class TestBase {
    
    public static final boolean DEBUG = true;
    public static final boolean LOG_STACKTRACE = DEBUG;
    
    public String heading(String s) {
        
        final int max = 77;
        final StringBuilder b = new StringBuilder(max);
        final int n = max - s.length() - 2; // 2 additional space chars
        final char ch = 'x';

        this.append(n, ch, b);
        b.append(' ').append(s).append(' ');
        this.append(n, ch, b);
        
        return b.toString();
    }
    
    private void append(int n, char ch, StringBuilder b) {
        if(n > 2) {
            final int half = n / 2;
            final char [] arr = new char[half];
            Arrays.fill(arr, 0, half, ch);
            b.append(arr);
        }
    }
    
    public void warn(Object msg){
        log(msg);
    }
    
    public void info(Object msg){
        log(msg);
    }
    
    public void debug(Object msg){
        if(DEBUG) {
            log(msg);
        }
    }

    public void trace(Object msg){
//        System.out.println(this.getClass().getSimpleName() + " " + msg);
    }
    
    public void log(Object msg){
        System.out.println(this.getClass().getSimpleName() + " " + msg);
    }
}
