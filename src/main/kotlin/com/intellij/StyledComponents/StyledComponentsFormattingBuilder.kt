package com.intellij.styledComponents

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import org.jetbrains.plugins.less.LESSLanguage
import java.util.Collections.emptyList

class StyledComponentsFormattingBuilder : FormattingModelBuilder {

    override fun createModel(element: PsiElement?, settings: CodeStyleSettings?): FormattingModel {
        val containingFile = element!!.containingFile.viewProvider.getPsi(LESSLanguage.INSTANCE)

        return FormattingModelProvider
                .createFormattingModelForPsiFile(
                        containingFile,
                        StyledComponentBlock(element.node, settings!!),
                        settings
                )
    }

    private companion object class StyledComponentBlock : AbstractBlock {

        private val scNode: ASTNode
        private val scStyleSettings: CodeStyleSettings

        constructor(node: ASTNode, settings: CodeStyleSettings) : super(node, null, null) {
            scNode = node
            scStyleSettings = settings
        }

        override fun isLeaf(): Boolean {
            return scNode.firstChildNode == null
        }

        override fun getSpacing(p0: Block?, p1: Block): Spacing? {
            return null
        }

        override fun buildChildren(): MutableList<Block> {
            if (isLeaf) {
                return emptyList()
            }
            val childBlocks = ArrayList<Block>()
            for (childNode in scNode.getChildren(null)) {
                if (childNode.text.trim().isEmpty()) {
                    continue
                }
                childBlocks.add(StyledComponentBlock(childNode, scStyleSettings))
            }

            return childBlocks
        }

        override fun getIndent(): Indent? {
            if (myNode.getText().trim().isEmpty()) {
                return null
            }

            val indent: Indent?

            indent = Indent.getContinuationIndent()

            return indent
        }

    }

}
