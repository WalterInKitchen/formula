package top.walterinkitchen.formula.util;

import java.util.Collection;

/**
 * Collection utils
 *
 * @author walter
 * @date 2022/3/13
 **/
public class CollectionUtils {
    /**
     * check if the collection is empty
     *
     * @param collection collection
     * @return true if null or size is zero
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
