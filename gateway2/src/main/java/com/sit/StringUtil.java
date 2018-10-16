package com.sit;

import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 字符串处理工具集
 * 全部为静态方法
 *
 */
public final class StringUtil
{
    private static Logger logger = Logger.getLogger(StringUtil.class);
    /**
     * 将字符串转为WML编码,用于wml页面显示
     * 根据unicode编码规则Blocks.txt：E000..F8FF; Private Use Area
     * @param str
     * @return String
     */
    public static String encodeWML(String str)
    {
        if(str == null)
        {
            return "";
        }
        // 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);
            switch(c)
            {
                case '\u00FF':
                case '\u0024':
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\t':
                    sb.append("  ");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\n':
                    sb.append("<br/>");
                    break;
                default:
                    if(c >= '\u0000' && c <= '\u001F')
                        break;
                    if(c >= '\uE000' && c <= '\uF8FF')
                        break;
                    if(c >= '\uFFF0' && c <= '\uFFFF')
                        break;
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 转换&#123;这种编码为正常字符<br/>
     * 有些手机会将中文转换成&#123;这种编码,这个函数主要用来转换成正常字符.
     * @param str
     * @return String
     */
    public static String decodeNetUnicode(String str)
    {
        if(str == null)
            return null;
        String pStr = "&#(\\d+);";
        Pattern p = Pattern.compile(pStr);
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            String mcStr = m.group(1);
            int charValue = StringUtil.convertInt(mcStr, -1);
            String s = charValue > 0 ? (char) charValue + "" : "";
            m.appendReplacement(sb, Matcher.quoteReplacement(s));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 过滤SQL字符串,防止SQL inject
     * @param sql
     * @return String
     */
    public static String encodeSQL(String sql)
    {
        if (sql == null)
        {
            return "";
        }
        // 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sql.length(); ++i)
        {
            char c = sql.charAt(i);
            switch(c)
            {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\'':
                    sb.append("\'\'");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 删除在wml下不能正确处理的字符
     * 根据unicode编码规则Blocks.txt：E000..F8FF; Private Use Area
     * @param str	要处理的字符串
     * @return		结果
     */
    public static String removeInvalidWML(String str){
        if(str == null)
            return null;
        //* 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);
            if(c >= '\u0000' && c <= '\u001F')
                continue;
            if(c >= '\uE000' && c <= '\uF8FF')
                continue;
            if(c >= '\uFFF0' && c <= '\uFFFF')
                continue;
            switch(c)
            {
                case '\u00FF':
                case '\u0024':
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\t':
                    sb.append("  ");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '^':
                case '`':
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 格式化日期
     *
     * @param dateStr
     *            输入的日期字符串
     * @param inputFormat
     *            输入日期格式
     * @param format
     *            输出日期格式
     * @return String 格式化后的日期,如果不能格式化则输出原日期字符串
     */
    public static String formatDate(String dateStr, String inputFormat, String format)
    {
        String resultStr = dateStr;
        try
        {
            Date date = new SimpleDateFormat(inputFormat).parse(dateStr);
            resultStr = formatDate(date,format);
        }
        catch (ParseException e)
        {
        }
        return resultStr;
    }
    /**
     * 格式化日期
     * 输入日期格式只支持yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param dateStr 输入的日期字符串
     * @param format 输出日期格式
     * @return String 格式化后的日期,如果不能格式化则输出原日期字符串
     */
    public static String formatDate(String dateStr, String format)
    {
        String resultStr = dateStr;
        String inputFormat = "yyyy-MM-dd HH:mm:ss";
        if(dateStr == null)
        {
            return "";
        }
        if(dateStr.matches("\\d{1,4}\\-\\d{1,2}\\-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}"))
        {
            inputFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        else if(dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}:\\d{1,2}"))
        {
            inputFormat = "yyyy-MM-dd HH:mm:ss";
        }
        else if(dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}:\\d{1,2}"))
        {
            inputFormat = "yyyy-MM-dd HH:mm";
        }
        else if(dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}"))
        {
            inputFormat = "yyyy-MM-dd HH";
        }
        else if(dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}"))
        {
            inputFormat = "yyyy-MM-dd";
        }
        else if(dateStr.matches("\\d{1,4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}"))
        {
            inputFormat = "yyyy/MM/dd HH:mm:ss.SSS";
        }
        else if(dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}:\\d{1,2}"))
        {
            inputFormat = "yyyy/MM/dd HH:mm:ss";
        }
        else if(dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}:\\d{1,2}"))
        {
            inputFormat = "yyyy/MM/dd HH:mm";
        }
        else if(dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}"))
        {
            inputFormat = "yyyy/MM/dd HH";
        }
        else if(dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}"))
        {
            inputFormat = "yyyy/MM/dd";
        }
        resultStr = formatDate(dateStr,inputFormat,format);
        return resultStr;
    }

    /**
     * 格式化日期
     * @param date 输入日期
     * @param format 输出日期格式
     * @return String
     */
    public static String formatDate(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取字符型参数，若输入字符串为null，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return 字符串参数
     */
    public static String convertString(String str, String defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        else
        {
            return str;
        }
    }

    /**
     * 获取int参数，若输入字符串为null或不能转为int，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return int参数
     */
    public static int convertInt(String str, int defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Integer.parseInt(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取long型参数，若输入字符串为null或不能转为long，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return long参数
     */
    public static long convertLong(String str, long defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Long.parseLong(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取double型参数，若输入字符串为null或不能转为double，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return double型参数
     */
    public static double convertDouble(String str, double defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Double.parseDouble(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取short型参数，若输入字符串为null或不能转为short，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return short型参数
     */
    public static short convertShort(String str, short defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Short.parseShort(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取float型参数，若输入字符串为null或不能转为float，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return float型参数
     */
    public static float convertFloat(String str, float defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Float.parseFloat(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取boolean型参数，若输入字符串为null或不能转为boolean，则返回设定的默认值
     * @param str 输入字符串
     * @param defaults 默认值
     * @return boolean型参数
     */
    public static boolean convertBoolean(String str, boolean defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Boolean.parseBoolean(str);
        }
        catch(Exception e)
        {
            return defaults;
        }
    }

    /**
     * 分割字符串
     * @param line			原始字符串
     * @param seperator		分隔符
     * @return				分割结果
     */
    public static String[] split(String line, String seperator)
    {
        if(line == null || seperator == null || seperator.length() == 0)
            return null;
        ArrayList<String> list = new ArrayList<String>();
        int pos1 = 0;
        int pos2;
        for(; ; )
        {
            pos2 = line.indexOf(seperator, pos1);
            if(pos2 < 0)
            {
                list.add(line.substring(pos1));
                break;
            }
            list.add(line.substring(pos1, pos2));
            pos1 = pos2 + seperator.length();
        }
        // 去掉末尾的空串，和String.split行为保持一致
        for(int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i)
        {
            list.remove(i);
        }
        return list.toArray(new String[0]);
    }

    /**
     * 分割字符串，并转换为int
     * @param line		原始字符串
     * @param seperator	分隔符
     * @param def		默认值
     * @return			分割结果
     */
    public static int[] splitInt(String line, String seperator, int def)
    {
        String[] ss = split(line, seperator);
        int[] r = new int[ss.length];
        for(int i = 0; i < r.length; ++i)
        {
            r[i] = convertInt(ss[i], def);
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    public static String join(String separator, Collection c)
    {
        if(c.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        Iterator i = c.iterator();
        sb.append(i.next());
        while(i.hasNext()){
            sb.append(separator);
            sb.append(i.next());
        }
        return sb.toString();
    }

    public static String join(String separator, String[] s)
    {
        return joinArray(separator,s);
    }

    public static String joinArray(String separator, Object[] s)
    {
        if(s == null || s.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for(int i = 1; i < s.length; ++i){
            if(s[i] != null)
            {
                sb.append(separator);
                sb.append(s[i].toString());
            }
        }
        return sb.toString();
    }

    public static String joinArray(String separator, int[] s)
    {
        if(s == null || s.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for(int i = 1; i < s.length; ++i){
            sb.append(separator);
            sb.append(s[i]);
        }
        return sb.toString();
    }

    public static String join(String separator, Object...c)
    {
        return joinArray(separator, c);
    }

    /**
     * 字符串全量替换
     * @param s			原始字符串
     * @param src		要替换的字符串
     * @param dest		替换目标
     * @return			结果
     */
    public static String replaceAll(String s, String src, String dest)
    {
        if(s == null || src == null || dest == null || src.length() == 0)
            return s;
        int pos = s.indexOf(src);			// 查找第一个替换的位置
        if(pos < 0)
            return s;
        int capacity = dest.length() > src.length() ? s.length() * 2: s.length();
        StringBuilder sb = new StringBuilder(capacity);
        int writen = 0;
        for(; pos >= 0; )
        {
            sb.append(s, writen, pos);		// append 原字符串不需替换部分
            sb.append(dest);				// append 新字符串
            writen = pos + src.length();	// 忽略原字符串需要替换部分
            pos = s.indexOf(src, writen);	// 查找下一个替换位置
        }
        sb.append(s, writen, s.length());	// 替换剩下的原字符串
        return sb.toString();
    }

    /**
     * 只替换第一个
     * @param s
     * @param src
     * @param dest
     * @return
     */
    public static String replaceFirst(String s, String src, String dest)
    {
        if(s == null || src == null || dest == null || src.length() == 0)
            return s;
        int pos = s.indexOf(src);
        if(pos < 0)
        {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() - src.length() + dest.length());

        sb.append(s, 0, pos);
        sb.append(dest);
        sb.append(s, pos + src.length(), s.length());
        return sb.toString();
    }

    /**
     * Returns <tt>true</tt> if s is null or <code>s.trim().length()==0<code>.
     *
     * @see java.lang.String#isEmpty()
     * @author isaacdong
     */
    public static boolean isEmpty(String s) {
        if (s == null)
            return true;
        return s.trim().isEmpty();
    }

    /**
     *@see java.lang.String#trim()
     */
    public static String trim(String s) {
        if (s == null)
            return null;
        return s.trim();
    }

    public static String removeAll(String s, String src)
    {
        return replaceAll(s, src, "");
    }


    /**
     * 以某一长度缩写字符串（1个中文或全角字符算2个长度单位，英文或半角算一个长度单位）.
     * 如果要显示n个汉字的长度，则maxlen= 2* n
     * @param src utf-8字符串
     * @param maxlen 缩写后字符串的最长长度（1个中文或全角字符算2个单位长度）
     * @param replacement 替换的字符串，该串长度会计算到maxlen中
     * @return String
     */
    public static String abbreviate(String src, int maxlen, String replacement){

        if(src==null)  return "";

        if ( replacement == null ) {
            replacement = "";
        }

        StringBuffer dest = new StringBuffer();                         //初始值设定为源串

        try{
            maxlen = maxlen - computeDisplayLen(replacement);

            if ( maxlen < 0) {
                return src;
            }

            int i = 0;
            for(; i < src.length() && maxlen > 0; ++i)
            {
                char c = src.charAt(i);
                if(c >= '\u0000' && c <= '\u00FF') {
                    maxlen = maxlen - 1;
                } else {
                    maxlen = maxlen - 2;
                }
                if ( maxlen >= 0 ) {
                    dest.append(c);
                }
            }

            //没有取完 src 所有字符时，才需要加后缀 ...
            if(i<src.length()-1){
                dest.append( replacement );
            }
            return dest.toString();
        }catch(Throwable e){
            logger.error(e.getMessage());
        }
        return src;
    }

    /**
     * @see  abbreviate(String src, int maxlen, String replacement)
     * @param src
     * @param maxlen
     * @return
     */
    public static String abbreviate(String src, int maxlen)
    {
        return abbreviate(src, maxlen, "");
    }

    /**
     * 将字符串截短,功能与abbreviate()类似
     * 全角字符算一个字,半角字符算半个字,这样做的目的是为了显示的时候排版整齐,因为全角字占的位置要比半角字小
     * @param str
     * @param maxLen
     * @return String
     */
    public static String toShort(String str, int maxLen, String replacement)
    {
        if(str == null)
        {
            return "";
        }
        if(str.length() <= maxLen)
        {
            return str;
        }
        StringBuilder dest = new StringBuilder();
        double len = 0;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if(c >= '\u0000' && c <= '\u00FF')
            {//半角字算半个字
                len += 0.5;
            }
            else
            {
                len += 1;
            }
            if(len > maxLen)
                return dest.toString() + replacement;
            else
                dest.append(c);
        }
        return dest.toString();
    }

    public static String toShort(String str, int maxLen)
    {
        return toShort(str, maxLen, "...");
    }

    /**
     * 计算字符串的显示长度，半角算１个长度，全角算两个长度
     * @param s
     * @return
     */
    public static int computeDisplayLen( String s ) {
        int len = 0;
        if ( s == null ) {
            return len;
        }

        for(int i = 0; i < s.length(); ++i)
        {
            char c = s.charAt(i);
            if(c >= '\u0000' && c <= '\u00FF') {
                len = len + 1;
            } else {
                len = len + 2;
            }
        }
        return len;
    }

    /**
     * 字符串是否为空
     *
     * @param str
     *            字符串
     * @return 返回类型 boolean 为空则为true，否则为false
     *
     */
    public static boolean bEmpty(String str) {
        if (str == null || "".equals(str) ||"".equals(str.trim())||str.equals("null")||str.equals("undefined"))
            return true;
        return false;
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

    public static void main(String[] argv)
    {
        //logger.info(joinArray(",",new int[]{1,2}));
    }
}

