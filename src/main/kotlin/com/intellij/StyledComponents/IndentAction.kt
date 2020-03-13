package com.intellij.styledComponents

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.pom.Navigatable
import com.sun.istack.NotNull


class IndentAction: AnAction() {

    fun debugPopup(@NotNull event: AnActionEvent, text: String) {
        val currentProject: Project? = event.getProject()
        val dlgMsg = StringBuffer(event.getPresentation().getText().toString() + " Selected!")

        // If an element is selected in the editor, add info about it.
        // If an element is selected in the editor, add info about it.
        val nav: Navigatable? = event.getData(CommonDataKeys.NAVIGATABLE)
        if (nav != null) {
            dlgMsg.append(java.lang.String.format("\nSelected Element: %s", nav.toString()))
        }
        Messages.showMessageDialog(currentProject, text, dlgMsg.toString(), Messages.getInformationIcon())
    }

    private fun isIndentApplicable(@NotNull event: AnActionEvent): Boolean {
        return event.getData(PlatformDataKeys.EDITOR) != null
                && event.getData(PlatformDataKeys.EDITOR)!!.selectionModel.hasSelection()
                && event.getData(PlatformDataKeys.EDITOR)!!.document.isWritable
                && event.getData(PlatformDataKeys.PSI_FILE)!!.language.displayName === "TypeScript"
    }

    override fun update(@NotNull event: AnActionEvent) {
        val project: Project? = event.project
        event.presentation.isEnabledAndVisible = project != null
                && isIndentApplicable(event)
    }


    override fun actionPerformed(@NotNull event: AnActionEvent) {
        debugPopup(event, event.getData(PlatformDataKeys.PSI_ELEMENT)!!.language.displayName)

    }

}
