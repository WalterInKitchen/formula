package top.walterinkitchen.formula.token;

/**
 * Section
 *
 * @author walter
 * @date 2022/3/12
 **/
public interface Section {
    /**
     * is the section open
     *
     * @return true/false
     */
    boolean isOpen();

    /**
     * is the section close
     *
     * @return true/false
     */
    boolean isClose();
}
