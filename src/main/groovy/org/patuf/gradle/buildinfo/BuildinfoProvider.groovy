package org.patuf.gradle.buildinfo
import java.util.Map;

public interface BuildinfoProvider {

	String getName();

	Map<String, String> getBuildinfo();
	
	boolean init();
}
