/*!A gradle plugin that integrates xmake seamlessly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (C) 2020-present, TBOOX Open Source Group.
 *
 * @author      ruki
 * @file        XMakePlugin.groovy
 *
 */
package org.tboox.gradle

import org.gradle.api.*

class XMakePlugin implements Plugin<Project> {

    // tag
    private final String TAG = "plugin"

    @Override
    void apply(Project project) {

        // check application/library plugin
        if (project.plugins.findPlugin("com.android.application") == null
                && project.plugins.findPlugin("com.android.library") == null) {
            throw new ProjectConfigurationException("Need android application/library plugin to be applied first", new Throwable())
        }

        // create xmake plugin extension
        XMakePluginExtension extension = project.extensions.create('xmake', XMakePluginExtension)

        // init logger
        XMakeLogger logger = new XMakeLogger(extension)

        // check project file exists (jni/xmake.lua)
        if (!new XMakeTaskContext(extension, project).projectFile.isFile()) {
            return
        }

        // trace
        logger.i(TAG, "activated for project: " + project.name)

        // register task: xmakeConfigureForXXX
        registerXMakeConfigureTasks(project, extension, logger)
    }

    private registerXMakeConfigureTasks(Project project, XMakePluginExtension extension, XMakeLogger logger) {

        /*def task1 = */project.tasks.register("xmakeConfigureForArm64", XMakeConfigureTask, new Action<XMakeConfigureTask>() {
            @Override
            void execute(XMakeConfigureTask task) {
                task.taskContext = new XMakeTaskContext(extension, project, logger, "arm64-v8a")
            }
        })
        /*
        task1.configure { Task task ->
            task.dependsOn()
        }*/
        project.tasks.register("xmakeConfigureForArmv7", XMakeConfigureTask, new Action<XMakeConfigureTask>() {
            @Override
            void execute(XMakeConfigureTask task) {
                task.taskContext = new XMakeTaskContext(extension, project, logger, "armeabi-v7a")
            }
        })
        project.tasks.register("xmakeConfigureForArm", XMakeConfigureTask, new Action<XMakeConfigureTask>() {
            @Override
            void execute(XMakeConfigureTask task) {
                task.taskContext = new XMakeTaskContext(extension, project, logger, "armeabi")
            }
        })
        project.tasks.register("xmakeConfigureForX64", XMakeConfigureTask, new Action<XMakeConfigureTask>() {
            @Override
            void execute(XMakeConfigureTask task) {
                task.taskContext = new XMakeTaskContext(extension, project, logger, "x64")
            }
        })
        project.tasks.register("xmakeConfigureForX86", XMakeConfigureTask, new Action<XMakeConfigureTask>() {
            @Override
            void execute(XMakeConfigureTask task) {
                task.taskContext = new XMakeTaskContext(extension, project, logger, "x86")
            }
        })
    }
}
