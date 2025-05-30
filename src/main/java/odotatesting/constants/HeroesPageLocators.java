package odotatesting.constants;

public class HeroesPageLocators {

    public static final String PROFESSIONAL_TAB = "//span[normalize-space()='Professional']";
    public static final String PUBLIC_TAB = "//span[normalize-space()='Public']";
    public static final String TURBO_TAB = "//span[normalize-space()='Turbo']";
    public static final String MATCHES_COUNT_IN_SEVEN_DAYS = "//span[@class='subtitle']";
    public static final String ALL_HERO_IMAGES = "//tbody//img[1]";
    public static final String ROWS_LOCATOR = "xpath=/html//table[1]/tbody[1]/tr";
    public static final String HERO_NAME_COLUMN_LOCATOR = "//td[1]//a";
    public static final String HERO_TABLE_ROWS_XPATH = "xpath=/html//table[1]/tbody[1]/tr";
    public static final String PRO_PICK_PLUS_BAN_PERCENTAGE_COLUMN = "//table/tbody/tr/td[2]//span";
    public static final String PRO_PICK_PLUS_BAN_COUNT_COLUMN = "//table/tbody/tr/td[2]//small";
    public static final String PRO_PICK_PERCENTAGE_COLUMN = "//table/tbody/tr/td[3]//span";
    public static final String PRO_PICK_COUNT_COLUMN = "//table/tbody/tr/td[3]//small";
    public static final String PRO_BAN_PERCENTAGE_COLUMN = "//table/tbody/tr/td[4]//span";
    public static final String PRO_BAN_COUNT_COLUMN = "//table/tbody/tr/td[4]//small";
    public static final String PRO_WIN_PERCENTAGE_COLUMN = "//table/tbody/tr/td[5]//span";
    public static final String PRO_WIN_COUNT_COLUMN = "//table/tbody/tr/td[5]//small"; //locators are missing when Count values are 0.
}
