package org.patuf.gradle.buildinfo;

import org.ajoberstar.grgit.Grgit;
import org.gradle.api.Project;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class GitBuildinfoProvider implements BuildinfoProvider {
    private static final Logger logger = LoggerFactory.getLogger(GitBuildinfoProvider)
	private Grgit grgit
	private final Project project
	
	public GitBuildinfoProvider(Project project) {
		this.project = project
	}
	
	@Override
	public boolean init() {
		try {
			grgit = Grgit.open(currentDir: project.rootProject.rootDir)
		} catch (Exception ex) {
			logger.warn('GitBuildinfoProvider failed to initialize', ex)
		}
	}
	
	@Override
	public String getName() {
		return 'git';
	}

	@Override
	public Map<String, String> getBuildinfo() {
		Map<String, String> buildInfo = [:];
		
		buildInfo['BRANCH'] = grgit.branch.current.fullName
		buildInfo['REMOTEBRANCH'] = grgit.branch.current.trackingBranch.fullName
		buildInfo['DIRTY'] = !grgit.status().clean
		buildInfo['HEAD'] = grgit.head().id
		
		return buildInfo
	}
}
