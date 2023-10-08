package dev.client.utils.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.crypto.Cipher;
import java.util.Map;


public class PoliciesUtil {
    public static void bypassKeyRestriction()
    {
        try {
            if (Cipher.getMaxAllowedKeyLength("AES") < 256) {
                Class<?> aClass = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor<?> con = aClass.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = aClass.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);
                aClass = Class.forName("javax.crypto.CryptoPermissions");
                con = aClass.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = aClass.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);
                aClass = Class.forName("javax.crypto.JceSecurityManager");
                f = aClass.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
