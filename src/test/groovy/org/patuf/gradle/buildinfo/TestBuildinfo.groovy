package org.patuf.gradle.buildinfo;

import static org.junit.Assert.*

import org.ajoberstar.grgit.Grgit
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.slf4j.LoggerFactory
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;

class TestBuildinfo extends GroovyTestCase {
	private Project project
	private Grgit grgit
	private File gitDir

	public void setUp() {
//		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//		root.setLevel(Level.DEBUG);
//		LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.DEBUG)
		
		gitDir = new File ("/tmp/buildinfotest")
		
		if (gitDir.exists())
			grgit = Grgit.open(gitDir)
		else
			grgit = Grgit.clone(dir: gitDir, uri: 'https://github.com/patuf/sandpit-gradle-plugin.git')
		project = ProjectBuilder.builder().withProjectDir(gitDir).build()
	}
	
	public void tearDown() {
		gitDir.deleteDir()
//		Files.delete(gitDir.toPath())
	}
	
	@Test
	public void test() {
		
		def rpd = project.rootProject.rootDir

		project.pluginManager.apply BuildinfoPlugin

		println project.buildinfo.retrieve()

//		final AbstractTask rlsTask = (AbstractTask) project.getTasks().getByName('release')
//		rlsTask.execute();
//		def rlsExt = new ReleaseExtension()
//		rlsExt.release = false
//		
//		def verMan = new VersionManager()
//		verMan.extension = rlsExt
//		
//		verMan.parseVersionInfo('0.0.4')
//		def verStr = verMan.toString() 
//		assertEquals("0.0.4-SNAPSHOT", verMan.toString())
	}
}
