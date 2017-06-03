/**
 * Copyright 2017 Nikolay Nikolov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patuf.gradle.buildinfo

import org.ajoberstar.grgit.Grgit
import org.gradle.api.Project
import org.slf4j.LoggerFactory

class BuildinfoExtension extends LinkedHashMap<String, String> {

	private static final LOGGER = LoggerFactory.getLogger(BuildinfoExtension.class)
	
    private final Project project

	/**
	 * Instance of grgit, can be used in local repo
	 */
	Grgit grgit;
	private final Map<String, BuildinfoProvider> biProviders = [:]

    BuildinfoExtension(Project project) {
        this.project = project
		grgit = Grgit.open(currentDir: project.rootProject.rootDir)
    }

	public Map<String, String> retrieve() {
		if (this.isEmpty()) {
			Map<String, String> buildInfo = [:]
			biProviders.each {
//				String bipName, BuildinfoProvider biProvider -> LOGGER.warn("$bipName kulturec ${biProvider.getBuildinfo().toString()}")
				String bipName, BuildinfoProvider biProvider -> biProvider.getBuildinfo().each {
					key, value -> this["${biProvider.name}_$key"] = value
				}
			}
		}
		
		return this
	}
	
	void buildinfoProvider(BuildinfoProvider biProvider) {
		if (biProvider.init()) {
			biProviders[biProvider.name] = biProvider
			LOGGER.info("$biProvider.name initialized successfully")
		} else
			LOGGER.warn("$biProvider.name failed to initialize. Skipping")
	}
}