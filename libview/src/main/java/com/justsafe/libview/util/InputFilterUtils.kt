package com.justsafe.libview.util

import android.text.InputFilter
import android.widget.EditText
import java.util.regex.Pattern

object InputFilterUtils {
    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    fun setEditTextInhibitInputSpaChat(editText: EditText) {
        val filter_space =
            InputFilter { source, start, end, dest, dstart, dend -> if (source == " ") "" else null }
        val filter_speChat =
            InputFilter { charSequence, i, i1, spanned, i2, i3 ->
                val speChat = "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？]"
                val pattern = Pattern.compile(speChat)
                val matcher = pattern.matcher(charSequence.toString())
                if (matcher.find() || containsEmoji(charSequence.toString())) "" else null
            }
        editText.filters = arrayOf(filter_space, filter_speChat)
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source -
     * @return -
     */
    fun containsEmoji(source: String): Boolean {
        val len = source.length
        for (i in 0 until len) {
            val codePoint = source[i]
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return false
            }
        }
        return true
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return -
     */
    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA || codePoint.toInt() == 0xD || codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0xD7FF || codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFFFD
    }
}