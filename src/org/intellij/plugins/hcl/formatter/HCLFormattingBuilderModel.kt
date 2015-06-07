/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package org.intellij.plugins.hcl.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.intellij.plugins.hcl.HCLElementTypes.*
import org.intellij.plugins.hcl.HCLLanguage


public class HCLFormattingBuilderModel : FormattingModelBuilder {
  override fun createModel(element: PsiElement?, settings: CodeStyleSettings?): FormattingModel {
    assert(element != null)
    assert(settings != null)
    element!!;
    settings!!;
    val builder = createSpacingBuilder(settings)
    val block = HCLBlock(null, element.getNode(), null, null, builder, Indent.getNoneIndent(), settings);
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings)
  }

  companion object {
    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
//      val hclSettings = settings.getCustomSettings(javaClass<HCLCodeStyleSettings>())
      val commonSettings = settings.getCommonSettings(HCLLanguage)

      val spacesBeforeComma = if (commonSettings.SPACE_BEFORE_COMMA) 1 else 0
      val spacesAroundAssignment = if (commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS) 1 else 0

      return SpacingBuilder(settings, HCLLanguage)
          .before(EQUALS).spacing(spacesAroundAssignment, spacesAroundAssignment, 0, false, 0)
          .after(EQUALS).spacing(spacesAroundAssignment, spacesAroundAssignment, 0, false, 0)
          .withinPair(L_BRACKET, R_BRACKET).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS) //, true
          .withinPair(L_CURLY, R_CURLY).spaceIf(commonSettings.SPACE_WITHIN_BRACES) //, true
          .before(COMMA).spacing(spacesBeforeComma, spacesBeforeComma, 0, false, 0)
          .after(COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA)
    }
  }

  override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? {
    return null
  }
}
