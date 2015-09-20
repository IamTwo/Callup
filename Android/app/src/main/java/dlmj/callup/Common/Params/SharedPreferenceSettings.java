package dlmj.callup.Common.Params;

/**
 * Created by Two on 15/9/8.
 */
public enum SharedPreferenceSettings {
    SESSION_TOKEN("sessionToken", ""),
    ACCOUNT_INFO("accountInfo", ""),
    LEVEL_VALUE("levelValue", -1);

    private final String mId;
    private final Object mDefaultValue;

    private SharedPreferenceSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    /**
     * Method that returns the unique identifier of the setting.
     * @return the mId.
     */
    public String getId() {
        return this.mId;
    }

    /**
     * Method that returns the default value of the setting.
     * @return Object The default value of the setting.
     */
    public Object getDefaultValue() {
        return this.mDefaultValue;
    }
}
