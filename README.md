# Build info plugin for Gradle
## Why do you care?
How many hops does it take you to track down what is the currently deployed build on production? How about the version of the deployment configuration that actually deployed your solution? Or maybe even the version of the scripts/configuration that built your infrastructure? How about if a stage environment works off a branch or branches in several repositories?

Usually all of these operations need to go through the CI server and manually mapping a build number to a commit id in a source repository. In many cases deployments or infrastructure builds are not managed by the CI, which makes it even more complicated. For traditional deployments it may be fine, but for continuous deployment process this would be too much time and overhead.

This gradle plugin collects facts about your build/deploy during the build/deploy itself and stores the in a map that can be easily attached as meta information to an artifact to the build.

## Possible use cases
* Docker images - store the build info as environment or labels or file
* Swarm/Mesos deployments - store the build info as labels
* Generic artifacts that don't have meta information by standard, i.e. scripting modules
* Cloud environments that are generated from a script - store the build info in a file or as meta information (if the cloud provider supports it)

## What kinds of facts are collected?
It is a pluggable system that currently only has plugin for git-related facts:
* Commit version
* Current and remote branch names
* Whether the repository is dirty
See below for help on writing additional plugins

## How to use this plugin
Build script snippet for use in all Gradle versions:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "gradle.plugin.org.patuf.gradle:gradle-buildinfo-plugin:0.0.3"
      }
    }

    apply plugin: "org.patuf.gradle-buildinfo-plugin"

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

    plugins {
      id "org.patuf.gradle-buildinfo-plugin" version "0.0.3"
    }

### Usage

This plugin registers an extension that implements Map<String,String>. The map initializes lazy, when calling the extension method retrieve():

    task myTaskThatNeedsBuildinfo {
      doFirst {
        buildinfo.retrieve()
      }
    }
The retrieve() method traverses all buildinfo providers and calls their action method (getBuildInfo).

You can use the extension as a normal map:

    buildinfo['myCustomFact'] = 'myCustomValue'
    buildinfo.each { key, value ->
      println "$key = $value"
    }


To guarantee some uniqueness and traceability, the buildinfo map's keys are prefixed with the 'name' property of the BuildinfoProvider.

The keys provided by the GitBuildinfoProvider are:

    git_BRANCH
    git_REMOTEBRANCH
    git_HEAD
    git_DIRTY

### Git authentication
This plugin's GitBuildinfoProvider uses the excellent library [grgit](https://github.com/ajoberstar/grgit), written by none other than Andrew Oberstar.

The simplest way to authenticate is:

build.gradle:

    System.setProperty('org.ajoberstar.grgit.auth.username', project.credentials.githubUser)
    System.setProperty('org.ajoberstar.grgit.auth.password', project.credentials.githubPass)

You can read more about grgit's authentication mechanism [here](http://ajoberstar.org/grgit/docs/groovydoc/index.html?org/ajoberstar/grgit/auth/AuthConfig.html).

### Adding your own buildinfo providers
build.gradle:

    class MyOwnBIProvider implements org.patuf.gradle.buildinfo.BuildinfoProvider {
      ...
    }

    buildinfo {
      buildinfoProvider new MyOwnBIProvider()
    }

## Questions, Bugs, Ideas and Features

Please use the repo's issues for all questions, bug reports, neat ideas and feature requests.

## Contributing

Contributions are very welcome and are accepted through pull requests.

## License

The plugin is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
