package cn.edu.bucm.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapCheck {

    private static LdapContext ctx = null;

    private static Control[] connCtls = null;

    private static String URL = "ldap://202.204.37.4:389/";

    private static String PRINCIPAL = "uid=ldapuser,dc=bucm,dc=edu,dc=cn";

    private static String PASSWORD = "ldap123$%^";

    public static void main(String[] args) {
        String username = "infotest1";
        String password = "a1234567";
        System.out.println(authenticate("dc=bucm,dc=edu,dc=cn", username, password));
        closeCtx();
    }

    public static Map<String, Object> authenticate(String baseDN, String usr, String pwd) {
        Map<String, Object> result = new HashMap<>();
        if (pwd == null || pwd == "") {
            result.put("loginResult", Boolean.valueOf(false));
            return result;
        }
        getCtx(baseDN);
        String userDN = getUserDN(baseDN, usr);
        if ("".equals(userDN) || userDN == null) {
            result.put("loginResult", Boolean.valueOf(false));
            return result;
        }
        try {
            String userName = getUserName(ctx, usr);
            result.put("username", userName);
            ctx.addToEnvironment("java.naming.security.principal", userDN);
            ctx.addToEnvironment("java.naming.security.credentials", pwd);
            ctx.reconnect(connCtls);
            result.put("loginResult", Boolean.valueOf(true));
            return result;
        } catch (AuthenticationException e) {
            result.put("loginResult", Boolean.valueOf(false));
            return result;
        } catch (NamingException e) {
            result.put("loginResult", Boolean.valueOf(false));
            return result;
        }
    }

    public static void getCtx(String BASEDN) {
        Hashtable<Object, Object> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", URL + BASEDN);
        env.put("java.naming.security.authentication", "simple");
        env.put("java.naming.security.principal", PRINCIPAL);
        env.put("java.naming.security.credentials", PASSWORD);
        try {
            ctx = new InitialLdapContext(env, connCtls);
        } catch (AuthenticationException e) {
            e.toString();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static void closeCtx() {
        try {
            if (ctx != null)
                ctx.close();
        } catch (NamingException namingException) {
        }
    }

    public static String getUserName(LdapContext ctx, String uid) {
        String result = "";
        try {
            Attributes attrs = ctx.getAttributes("uid=" + uid);
            NamingEnumeration<? extends Attribute> ane = attrs.getAll();
            while (ane.hasMore()) {
                Attribute attr = ane.next();
                String attrType = attr.getID();
                if (attrType.equals("cn")) {
                    NamingEnumeration<?> values = attr.getAll();
                    while (values.hasMore()) {
                        Object oneVal = values.nextElement();
                        if (oneVal instanceof String) {
                            result = oneVal.toString();
                        }
                    }
                }
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
        try {
            result = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getUserDN(String baseDN, String uid) {
        String userDN = "";
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(2);
            NamingEnumeration<SearchResult> en = ctx.search("", "uid=" + uid, constraints);
            if (en == null)
                System.out.println("Have no NamingEnumeration.");
            if (!en.hasMoreElements())
                System.out.println("Have no element.");
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    userDN = userDN + si.getName();
                    userDN = userDN + "," + baseDN;
                    continue;
                }
                System.out.println(obj);
            }
        } catch (NamingException e) {
            System.out.println("Exception in search()-----{}" + e);
        }
        return userDN;
    }
}