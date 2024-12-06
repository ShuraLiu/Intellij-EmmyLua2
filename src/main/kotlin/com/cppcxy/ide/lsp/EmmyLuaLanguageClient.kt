package com.cppcxy.ide.lsp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServersRegistry
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import org.eclipse.lsp4j.ProgressParams
import org.eclipse.lsp4j.WorkDoneProgressBegin
import org.eclipse.lsp4j.WorkDoneProgressEnd
import org.eclipse.lsp4j.WorkDoneProgressReport
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification

class EmmyLuaLanguageClient(project: Project?) : LanguageClientImpl(project) {

    private var loadingWorkspaceFinished = false
    private var checkFinished = false
    private var initializing = false

    private val token = "Initializing..."

    @JsonNotification("emmy/setServerStatus")
    fun notifyEmmyServerStatus(params: EmmyServerStatusParams) {
        if (params.loading && !this.initializing) {
            this.initializing = true
            this.loadingWorkspaceFinished = false

            val progressParams = ProgressParams()
            progressParams.setToken(this.token)

            val begin = WorkDoneProgressBegin()
            begin.title = this.token
            begin.message = params.message
            begin.percentage = 0
            progressParams.value = Either.forLeft(begin)

            notifyProgress(progressParams)

        } else if (!params.loading && this.initializing) {
            this.loadingWorkspaceFinished = true

            if (this.checkFinished)
            {
                this.initializing = false
                val progressParams = ProgressParams()
                progressParams.setToken(this.token)
                val end = WorkDoneProgressEnd()
                end.message = params.message
                progressParams.value = Either.forLeft(end)
            }
        }

    }

    @JsonNotification("emmy/progressReport")
    fun notifyEmmyProgressReport(params: EmmyProgressReportParams) {
        if (this.initializing)
        {
            if (params.text == "Check finished!")
            {
                this.checkFinished = true

                if (this.loadingWorkspaceFinished)
                {
                    this.initializing = false
                    val progressParams = ProgressParams()
                    progressParams.setToken(this.token)
                    val end = WorkDoneProgressEnd()
                    end.message = params.text
                    progressParams.value = Either.forLeft(end)
                    notifyProgress(progressParams)
                }
            }
            else
            {
                val progressParams = ProgressParams()
                progressParams.setToken(this.token)
                val report = WorkDoneProgressReport()
                report.message = params.text
                report.percentage = params.percent.toInt() * 100
                progressParams.value = Either.forLeft(report)
                notifyProgress(progressParams)
            }
        }
    }

    override fun dispose() {
        super.dispose()

        loadingWorkspaceFinished = false
        checkFinished = false
        initializing = false
    }
}