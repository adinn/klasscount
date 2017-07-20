/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * @authors Andrew Dinn
 */

package org.jboss.klasscount;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * main class for agent which lists loaded classes
 */
public class AgentMain
{
    private static PrintStream out;
    private static String format;
    private static String[] packagePrefixes = {
            "java.",
            "javax.",
            "sun.",
            "com.sun.",
            "com.oracle.net",
            "com.oracle.webservices",
            "com.oracle.xmlns",
    };

    public static void premain(String args, Instrumentation inst)
            throws Exception
    {
        out = System.out;
        format = "%8d: %-58s %1s %-8d\n";
        int count = 0;
        int internal = 0;

        processOptions(args);

        Class[] klasses = inst.getAllLoadedClasses();
        for (int i = 0; i < klasses.length; i++) {
            Class<?> klass = klasses[i];
            count++;
            String name = klasses[i].getName();
            String tag = "-";
            boolean isInternal = isInternal(klass);
            if (isInternal) {
                internal++;
                tag = "+";
            }
            out.format(format, count, name, tag, internal);
        }
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception
    {
        premain(args, inst);
    }

    private static boolean isInternal(Class<?> klass)
    {
        while (klass.isArray()) {
            klass = klass.getComponentType();
        }

        if (klass.isPrimitive()) {
            return true;
        }
        
        if (klass.getClassLoader() == null) {
            return true;
        }

        Package pk = klass.getPackage();
        if (pk == null) {
            return false;
        }
        String packageName = pk.getName();

        if (packageName == null ||packageName.length() == 0) {
            return false;
        } else {
            for (int i = 0; i < packagePrefixes.length; i++) {
                if (packageName.startsWith(packagePrefixes[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void processOptions(String options)
    {
    }
}
