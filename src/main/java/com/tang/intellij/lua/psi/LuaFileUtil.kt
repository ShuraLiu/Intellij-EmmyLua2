/*
 * Copyright (c) 2017. tangzx(love.tangzx@qq.com)
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

package com.tang.intellij.lua.psi

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.ProjectAndLibrariesScope
import java.io.File

/**
 *
 * Created by tangzx on 2017/1/4.
 */
object LuaFileUtil {

    private val pluginVirtualDirectory: VirtualFile?
        get() {
            val descriptor = PluginManagerCore.getPlugin(PluginId.getId("com.cppcxy.Intellij-EmmyLuaForX7"))
            if (descriptor != null) {
                return VirtualFileManager.getInstance().findFileByNioPath(descriptor.pluginPath)
            }

            return null
        }

    fun findFile(project: Project, shortUrl: String?): VirtualFile? {
        var fixedShortUrl = shortUrl ?: return null

        // Check if the path is absolute
        if (File(fixedShortUrl).isAbsolute) {
            val virtualFile = VfsUtil.findFileByIoFile(File(fixedShortUrl), true)
            if (virtualFile != null && virtualFile.exists()) {
                return virtualFile
            }
            return null
        }

        // "./x.lua" => "x.lua"
        if (fixedShortUrl.startsWith("./") || fixedShortUrl.startsWith(".\\")) {
            fixedShortUrl = fixedShortUrl.substring(2)
        }
        // Check if the fixedShortUrl already has an extension
        val hasExtension = fixedShortUrl.contains(".")
        if (hasExtension) {
            val virtualFile = VfsUtil.findRelativeFile(fixedShortUrl, project.baseDir)
            if (virtualFile != null && virtualFile.exists()) {
                return virtualFile
            }

            var perfect: VirtualFile? = null
            val names = fixedShortUrl.split('/')
            val fileName = names.lastOrNull()
            if (fileName != null) {
                ApplicationManager.getApplication().runReadAction {
                    var perfectMatch = Int.MAX_VALUE
                    val files = FilenameIndex.getVirtualFilesByName(fileName, ProjectAndLibrariesScope(project))
                    for (file in files) {
                        val path = file.canonicalPath
                        if (path != null && perfectMatch > path.length && path.endsWith(fileName)) {
                            perfect = file
                            perfectMatch = path.length
                        }
                    }
                }
            }
            return perfect
        }
        else {
            val extensions = LuaFileManager.extensions
            for (extension in extensions) {
                val fileName = if (extension.isEmpty()) fixedShortUrl else "$fixedShortUrl$extension"
                val virtualFile = VfsUtil.findRelativeFile(fileName, project.baseDir)
                if (virtualFile != null && virtualFile.exists()) {
                    return virtualFile
                }
            }

            var perfect: VirtualFile? = null
            val names = fixedShortUrl.split('/')
            val fileName = names.lastOrNull()
            if (fileName != null) {
                ApplicationManager.getApplication().runReadAction {
                    var perfectMatch = Int.MAX_VALUE
                    for (extName in extensions) {
                        val files = FilenameIndex.getVirtualFilesByName("$fileName$extName", ProjectAndLibrariesScope(project))
                        for (file in files) {
                            val path = file.canonicalPath
                            if (path != null && perfectMatch > path.length && path.endsWith("$fixedShortUrl$extName")) {
                                perfect = file
                                perfectMatch = path.length
                            }
                        }

                        if (perfect != null)
                            break
                    }
                }
            }
            return perfect
        }
        return null
    }

    fun getPluginVirtualFile(path: String): String? {
        val directory = pluginVirtualDirectory
        if (directory != null) {
            var fullPath = directory.path + "/classes/" + path
            if (File(fullPath).exists())
                return fullPath
            fullPath = directory.path + "/" + path
            if (File(fullPath).exists())
                return fullPath
        }
        return null
    }
}
