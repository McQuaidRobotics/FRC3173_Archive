package org.usfirst.frc3173.IgKnighters2014.utilities;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author wolf
 */
public class Profiler {
    private static class Timing {
        public long m_totaltime, m_count, m_starttime, m_min = Long.MAX_VALUE, m_max = 0;
        public boolean m_started = false;
        public String m_name;
        public Timing(String name) {
            m_totaltime = 0;
            m_count = 0;
            m_name = name;
        }
        public void addTiming (long time) {
            m_totaltime += time;
            m_count++;
            if (time < m_min) m_min = time;
            if (time > m_max) m_max = time;
        }
        
        public void start() {
            //System.out.println("Timing starts: "+m_name);
            if (m_started) {
                System.out.println("Timing of '"+getName()+"' already started!");
            } else {
                m_started = true;
                m_starttime = System.currentTimeMillis();
            }
        }
        public void stop() {
            //System.out.println("Timing stops: "+m_name);
            if (m_started) {
                m_started = false;
                addTiming(System.currentTimeMillis() - m_starttime);
            } else {
                System.out.println("Timing of '"+getName()+"' not started yet!");
            }
        }
        
        public String getName() {
            return m_name;
        }
    }
    
    protected static Hashtable timings = new Hashtable();
    
    public static void reset() {
        timings.clear();
        System.out.println("Reset profiler");
    }
    
    public static void start(String name) {
        Timing t = getTiming(name);
        t.start();
        timings.put(name, t);
    }
    
    public static void stop(String name) {
        Timing t = getTiming(name);
        t.stop();
        timings.put(name, t);
    }
    
    public static void timing(String name, long time /* no see */) {
        System.out.println("t "+name+"\t"+time);
        Timing t = getTiming(name);
        t.addTiming(time);
        timings.put(name, t);
    }
    
    private static Timing getTiming(String name) {
        Timing t = (Timing)(timings.get(name));
        if (t == null) {
            t = new Timing(name);
            System.out.println("New timing: "+name);
        }
        return t;
    }
    
    public static void printTimings() {
        System.out.println("Total\tCount\tAvg\tMin\tMax\tName");
        for (Enumeration e = timings.elements(); e.hasMoreElements() ;) {
            Timing t = (Timing) e.nextElement();
            System.out.print(t.m_totaltime+"\t"+t.m_count+"\t");
            if (t.m_count == 0) {
                System.out.print("--");
            } else {
                System.out.print(t.m_totaltime/t.m_count);
            }
            System.out.print("\t"+t.m_min+"\t"+t.m_max);
            System.out.println("\t"+t.getName());
        }
    }
}
