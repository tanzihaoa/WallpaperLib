package com.tzh.wallpaperlib.util;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Properties;

public final class RomUtils {
    private static final String[] ROM_HUAWEI = new String[]{"huawei"};
    private static final String[] ROM_VIVO = new String[]{"vivo"};
    private static final String[] ROM_XIAOMI = new String[]{"xiaomi"};
    private static final String[] ROM_OPPO = new String[]{"oppo"};
    private static final String[] ROM_LEECO = new String[]{"leeco", "letv"};
    private static final String[] ROM_360 = new String[]{"360", "qiku"};
    private static final String[] ROM_ZTE = new String[]{"zte"};
    private static final String[] ROM_ONEPLUS = new String[]{"oneplus"};
    private static final String[] ROM_NUBIA = new String[]{"nubia"};
    private static final String[] ROM_COOLPAD = new String[]{"coolpad", "yulong"};
    private static final String[] ROM_LG = new String[]{"lg", "lge"};
    private static final String[] ROM_GOOGLE = new String[]{"google"};
    private static final String[] ROM_SAMSUNG = new String[]{"samsung"};
    private static final String[] ROM_MEIZU = new String[]{"meizu"};
    private static final String[] ROM_LENOVO = new String[]{"lenovo"};
    private static final String[] ROM_SMARTISAN = new String[]{"smartisan", "deltainno"};
    private static final String[] ROM_HTC = new String[]{"htc"};
    private static final String[] ROM_SONY = new String[]{"sony"};
    private static final String[] ROM_GIONEE = new String[]{"gionee", "amigo"};
    private static final String[] ROM_MOTOROLA = new String[]{"motorola"};
    private static final String VERSION_PROPERTY_HUAWEI = "ro.build.version.emui";
    private static final String VERSION_PROPERTY_VIVO = "ro.vivo.os.build.display.id";
    private static final String VERSION_PROPERTY_XIAOMI = "ro.build.version.incremental";
    private static final String VERSION_PROPERTY_OPPO = "ro.build.version.opporom";
    private static final String VERSION_PROPERTY_LEECO = "ro.letv.release.version";
    private static final String VERSION_PROPERTY_360 = "ro.build.uiversion";
    private static final String VERSION_PROPERTY_ZTE = "ro.build.MiFavor_version";
    private static final String VERSION_PROPERTY_ONEPLUS = "ro.rom.version";
    private static final String VERSION_PROPERTY_NUBIA = "ro.build.rom.id";
    private static final String UNKNOWN = "unknown";
    private static RomInfo bean = null;

    private RomUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isHuawei() {
        return ROM_HUAWEI[0].equals(getRomInfo().name);
    }

    public static boolean isVivo() {
        return ROM_VIVO[0].equals(getRomInfo().name);
    }

    public static boolean isXiaomi() {
        return ROM_XIAOMI[0].equals(getRomInfo().name);
    }

    public static boolean isOppo() {
        return ROM_OPPO[0].equals(getRomInfo().name);
    }

    public static boolean isLeeco() {
        return ROM_LEECO[0].equals(getRomInfo().name);
    }

    public static boolean is360() {
        return ROM_360[0].equals(getRomInfo().name);
    }

    public static boolean isZte() {
        return ROM_ZTE[0].equals(getRomInfo().name);
    }

    public static boolean isOneplus() {
        return ROM_ONEPLUS[0].equals(getRomInfo().name);
    }

    public static boolean isNubia() {
        return ROM_NUBIA[0].equals(getRomInfo().name);
    }

    public static boolean isCoolpad() {
        return ROM_COOLPAD[0].equals(getRomInfo().name);
    }

    public static boolean isLg() {
        return ROM_LG[0].equals(getRomInfo().name);
    }

    public static boolean isGoogle() {
        return ROM_GOOGLE[0].equals(getRomInfo().name);
    }

    public static boolean isSamsung() {
        return ROM_SAMSUNG[0].equals(getRomInfo().name);
    }

    public static boolean isMeizu() {
        return ROM_MEIZU[0].equals(getRomInfo().name);
    }

    public static boolean isLenovo() {
        return ROM_LENOVO[0].equals(getRomInfo().name);
    }

    public static boolean isSmartisan() {
        return ROM_SMARTISAN[0].equals(getRomInfo().name);
    }

    public static boolean isHtc() {
        return ROM_HTC[0].equals(getRomInfo().name);
    }

    public static boolean isSony() {
        return ROM_SONY[0].equals(getRomInfo().name);
    }

    public static boolean isGionee() {
        return ROM_GIONEE[0].equals(getRomInfo().name);
    }

    public static boolean isMotorola() {
        return ROM_MOTOROLA[0].equals(getRomInfo().name);
    }

    public static RomInfo getRomInfo() {
        if (bean != null) {
            return bean;
        } else {
            bean = new RomInfo();
            String brand = getBrand();
            String manufacturer = getManufacturer();
            if (isRightRom(brand, manufacturer, ROM_HUAWEI)) {
                bean.name = ROM_HUAWEI[0];
                String version = getRomVersion("ro.build.version.emui");
                String[] temp = version.split("_");
                if (temp.length > 1) {
                    bean.version = temp[1];
                } else {
                    bean.version = version;
                }

                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_VIVO)) {
                bean.name = ROM_VIVO[0];
                bean.version = getRomVersion("ro.vivo.os.build.display.id");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_XIAOMI)) {
                bean.name = ROM_XIAOMI[0];
                bean.version = getRomVersion("ro.build.version.incremental");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_OPPO)) {
                bean.name = ROM_OPPO[0];
                bean.version = getRomVersion("ro.build.version.opporom");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_LEECO)) {
                bean.name = ROM_LEECO[0];
                bean.version = getRomVersion("ro.letv.release.version");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_360)) {
                bean.name = ROM_360[0];
                bean.version = getRomVersion("ro.build.uiversion");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_ZTE)) {
                bean.name = ROM_ZTE[0];
                bean.version = getRomVersion("ro.build.MiFavor_version");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_ONEPLUS)) {
                bean.name = ROM_ONEPLUS[0];
                bean.version = getRomVersion("ro.rom.version");
                return bean;
            } else if (isRightRom(brand, manufacturer, ROM_NUBIA)) {
                bean.name = ROM_NUBIA[0];
                bean.version = getRomVersion("ro.build.rom.id");
                return bean;
            } else {
                if (isRightRom(brand, manufacturer, ROM_COOLPAD)) {
                    bean.name = ROM_COOLPAD[0];
                } else if (isRightRom(brand, manufacturer, ROM_LG)) {
                    bean.name = ROM_LG[0];
                } else if (isRightRom(brand, manufacturer, ROM_GOOGLE)) {
                    bean.name = ROM_GOOGLE[0];
                } else if (isRightRom(brand, manufacturer, ROM_SAMSUNG)) {
                    bean.name = ROM_SAMSUNG[0];
                } else if (isRightRom(brand, manufacturer, ROM_MEIZU)) {
                    bean.name = ROM_MEIZU[0];
                } else if (isRightRom(brand, manufacturer, ROM_LENOVO)) {
                    bean.name = ROM_LENOVO[0];
                } else if (isRightRom(brand, manufacturer, ROM_SMARTISAN)) {
                    bean.name = ROM_SMARTISAN[0];
                } else if (isRightRom(brand, manufacturer, ROM_HTC)) {
                    bean.name = ROM_HTC[0];
                } else if (isRightRom(brand, manufacturer, ROM_SONY)) {
                    bean.name = ROM_SONY[0];
                } else if (isRightRom(brand, manufacturer, ROM_GIONEE)) {
                    bean.name = ROM_GIONEE[0];
                } else if (isRightRom(brand, manufacturer, ROM_MOTOROLA)) {
                    bean.name = ROM_MOTOROLA[0];
                } else {
                    bean.name = manufacturer;
                }

                bean.version = getRomVersion("");
                return bean;
            }
        }
    }

    private static boolean isRightRom(String brand, String manufacturer, String... names) {
        String[] var3 = names;
        int var4 = names.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String name = var3[var5];
            if (brand.contains(name) || manufacturer.contains(name)) {
                return true;
            }
        }

        return false;
    }

    private static String getManufacturer() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(manufacturer)) {
                return manufacturer.toLowerCase();
            }
        } catch (Throwable var1) {
        }

        return "unknown";
    }

    private static String getBrand() {
        try {
            String brand = Build.BRAND;
            if (!TextUtils.isEmpty(brand)) {
                return brand.toLowerCase();
            }
        } catch (Throwable var1) {
        }

        return "unknown";
    }

    private static String getRomVersion(String propertyName) {
        String ret = "";
        if (!TextUtils.isEmpty(propertyName)) {
            ret = getSystemProperty(propertyName);
        }

        if (TextUtils.isEmpty(ret) || ret.equals("unknown")) {
            try {
                String display = Build.DISPLAY;
                if (!TextUtils.isEmpty(display)) {
                    ret = display.toLowerCase();
                }
            } catch (Throwable var3) {
            }
        }

        return TextUtils.isEmpty(ret) ? "unknown" : ret;
    }

    private static String getSystemProperty(String name) {
        String prop = getSystemPropertyByShell(name);
        if (!TextUtils.isEmpty(prop)) {
            return prop;
        } else {
            prop = getSystemPropertyByStream(name);
            if (!TextUtils.isEmpty(prop)) {
                return prop;
            } else {
                return Build.VERSION.SDK_INT < 28 ? getSystemPropertyByReflect(name) : prop;
            }
        }
    }

    private static String getSystemPropertyByShell(String propName) {
        BufferedReader input = null;

        String var5;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            String ret = input.readLine();
            if (ret == null) {
                return "";
            }

            var5 = ret;
        } catch (IOException var16) {
            return "";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var15) {
                }
            }

        }

        return var5;
    }

    private static String getSystemPropertyByStream(String key) {
        try {
            Properties prop = new Properties();
            FileInputStream is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            prop.load(is);
            return prop.getProperty(key, "");
        } catch (Exception var3) {
            return "";
        }
    }

    private static String getSystemPropertyByReflect(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String)getMethod.invoke(clz, key, "");
        } catch (Exception var3) {
            return "";
        }
    }

    public static class RomInfo {
        private String name;
        private String version;

        public RomInfo() {
        }

        public String getName() {
            return this.name;
        }

        public String getVersion() {
            return this.version;
        }

        public String toString() {
            return "RomInfo{name=" + this.name + ", version=" + this.version + "}";
        }
    }
}
