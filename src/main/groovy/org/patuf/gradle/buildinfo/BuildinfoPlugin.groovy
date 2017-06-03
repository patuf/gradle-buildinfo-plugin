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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

class BuildinfoPlugin implements Plugin<Project> {

    public static final String BUILDINFO_EXTENSION_NAME = 'buildinfo'

    private static final LOGGER = LoggerFactory.getLogger(BuildinfoPlugin.class)

    @Override
    void apply(Project project) {
        LOGGER.debug("Registering extension '$BUILDINFO_EXTENSION_NAME'")
        def buildinfoExtension = project.extensions.create(BUILDINFO_EXTENSION_NAME, BuildinfoExtension, project)
		buildinfoExtension.buildinfoProvider(new GitBuildinfoProvider(project))
    }
}