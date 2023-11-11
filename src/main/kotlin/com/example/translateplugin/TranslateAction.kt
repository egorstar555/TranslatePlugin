package com.example.translateplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.io.HttpRequests
import org.json.JSONArray
import java.net.URLEncoder

class TranslateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)
        val selectedText = editor?.let { StringUtil.trim(it.selectionModel.selectedText) }

        if (selectedText != null) {
            val translatedText = translate(selectedText)
            Messages.showMessageDialog(translatedText, "Перевод", Messages.getInformationIcon())
        } else {
            Messages.showMessageDialog("Пожалуйста, выделите текст для перевода", "Ошибка", Messages.getErrorIcon())
        }
    }

    private fun translate(text: String): String? {
        val encodedText = URLEncoder.encode(text, "UTF-8")
        val url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl=en&dt=t&q=$encodedText"
        val response = HttpRequests.request(url).readString()
        val jsonArray = JSONArray(response)
        val translation = jsonArray.getJSONArray(0).getJSONArray(0).getString(0)
        return translation
    }
}