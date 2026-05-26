package com.xilei.blog.utils;

/**
 * 输入清理工具类
 * 功能：对用户输入进行XSS防护清理
 */
public class SanitizeUtils {

    /**
     * 清理HTML内容，防止XSS攻击
     * 移除所有HTML标签，只保留纯文本
     */
    public static String sanitizeHtml(String input) {
        if (input == null) return null;
        // 移除HTML标签
        String cleaned = input.replaceAll("<[^>]+>", "");
        // 转义特殊字符
        cleaned = cleaned.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
        return cleaned.trim();
    }

    /**
     * 清理纯文本输入
     * 去除首尾空白，合并多个空格
     */
    public static String sanitizeText(String input) {
        if (input == null) return null;
        return input.replaceAll("\\s+", " ").trim();
    }
}
