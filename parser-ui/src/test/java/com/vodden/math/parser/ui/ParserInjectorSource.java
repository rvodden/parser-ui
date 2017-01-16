package com.vodden.math.parser.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.grapher.graphviz.GraphvizGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

public class ParserInjectorSource implements InjectorSource {

	@Override
	public Injector getInjector() {
		GuiceDebug.enable();
		Injector injector = Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, new ParserModule());
		try {
			GuiceGrapher.graph("/Users/voddenr/guice-graph.dot", injector);
		} catch (IOException e) {
			e.printStackTrace();
			assert(false);
		}
		return injector;
	}
	
	public static class GuiceGrapher {
		  private static void graph(String filename, Injector demoInjector) throws IOException {
		    PrintWriter out = new PrintWriter(new File(filename), "UTF-8");

		    Injector injector = Guice.createInjector(new GraphvizModule());
		    GraphvizGrapher grapher = injector.getInstance(GraphvizGrapher.class);
		    grapher.setOut(out);
		    grapher.setRankdir("TB");
		    grapher.graph(demoInjector);
		  }
	}
	
	public static class GuiceDebug {
	    private static final Handler HANDLER;
	    static {
	        HANDLER = new StreamHandler(System.out, new Formatter() {
	            public String format(LogRecord record) {
	                return String.format("%s: [Guice] %s%n",
	                                  record.getLevel().getName(),
	                                  record.getMessage());
	            }
	        });
	        HANDLER.setLevel(Level.ALL);
	        enable();
	    }

	    private GuiceDebug() {}

	    public static Logger getLogger() {
	        return Logger.getLogger("com.google.inject");
	    }

	    public static void enable() {
	        Logger guiceLogger = getLogger();
	        guiceLogger.addHandler(GuiceDebug.HANDLER);
	        guiceLogger.setLevel(Level.ALL);
	    }

	    public static void disable() {
	        Logger guiceLogger = getLogger();
	        guiceLogger.setLevel(Level.OFF);
	        guiceLogger.removeHandler(GuiceDebug.HANDLER);
	    }
	}

}
