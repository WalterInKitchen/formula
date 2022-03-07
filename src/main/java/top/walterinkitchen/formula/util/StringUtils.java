package top.walterinkitchen.formula.util;

/**
 * string utils
 *
 * @author walter
 * @date 2022/3/7
 **/
public class StringUtils {
    /**
     * check if string is null or blank
     *
     * @param text string
     * @return true/false
     */
    public static boolean isEmpty(String text) {
        return text == null || text.equals("");
    }
}
