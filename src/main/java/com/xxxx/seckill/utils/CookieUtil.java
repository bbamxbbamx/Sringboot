package com.xxxx.seckill.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class CookieUtil {
    /**
     * 得到Cokie的值，不编码
     * */
    public static String getCookieValue(HttpServletRequest request,String cookieName){
        return getCookieValue(request,cookieName,false);
    }
    public static String getCookieValue(HttpServletRequest request,String cookieName,boolean isDecoder){
        Cookie[] cookieList=request.getCookies();
        if(cookieList==null||cookieName==null){
            return null;
        }
        String retValue=null;
        try{
            for (int i=0;i<cookieList.length;i++){
                if(cookieList[i].getName().equals(cookieName)){
                    if(isDecoder){
                        retValue= URLDecoder.decode(cookieList[i].getValue(),"UTF-8");
                    }else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return retValue;
    }

    /**   设置Cookie的值 不设置生效时间默认浏览器关闭及失效，也不编码      */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue ){
        setCookie(request,response,cookieName,cookieValue,-1);//如果maxAge为负数，则表示该Cookie仅在本浏览器窗口以及本窗口打开的子窗口内有效，关闭窗口后该Cookie即失效
    }
    /**   设置cookie的值 在指定时间生效，但不编码    */
    public static  void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
                                  String cookieValue,int cookieMaxage){
        setCookie(request,response,cookieName,cookieValue,cookieMaxage,false);
    }
    /**   设置cookie的值不设置生效时间，但编码   */
    public static  void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
                                  String cookieValue,boolean isEncode){
        setCookie(request,response,cookieName,cookieValue,-1,isEncode);
    }

    /**  设置Cookie的值 在指定时间内生效，编码参数*/
    public static  void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
                                  String cookieValue,int cookieMaxage,boolean isEncode){
        doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,isEncode);
    }
    /**   设置Cookie的值 在指定时间内生效，编码参数（指定编码）*/
    public static  void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
                                  String cookieValue,int cookieMaxage,String encodeString){
        doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,encodeString);
    }
    /**  删除cookie，带cookie域名*/
    public static void  deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        doSetCookie(request,response,cookieName,"",-1,false);
    }
    /**  设置Cookie的值 在指定时间内生效
     *   cookieMaxage  cookie 生效最大的秒数*/
    private static final  void  doSetCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
                                            String cookieValue,int cookieMaxage,boolean isEncode){//false
        try {
            if(cookieValue==null){
                cookieValue ="";
            }else if(isEncode){
                cookieValue = URLEncoder.encode(cookieValue,"UTF-8");
            }
            Cookie cookie =new Cookie(cookieName,cookieValue);
            if(cookieMaxage>0)
                cookie.setMaxAge(cookieMaxage);
            if(null!=request){
                String domainName= getDomainName(request);
                System.out.println(domainName);
                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**      设置cookie的值，并使其在指定时间内生效
     *       cookieMaxage  cookie 生效最大的秒数 */
    private static final  void  doSetCookie(HttpServletRequest request,HttpServletResponse response,
                                            String cookieName, String cookieValue,int cookieMaxage,String encodeString){
        try {
            if(cookieValue==null){
                cookieValue ="";
            }else{
                cookieValue =URLEncoder.encode(cookieValue,encodeString);
            }
            Cookie cookie =new Cookie(cookieName,cookieValue);
            if(cookieMaxage>0)
                cookie.setMaxAge(cookieMaxage);
            if(null!=request){//设置域名的cookie
                String domainName= getDomainName(request);
                System.out.println(domainName);
                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**   得到cookie的域名  */
    private static  final String getDomainName(HttpServletRequest request){
        String domainName=null;
        String serverName=request.getRequestURI().toString();
        if(serverName==null||serverName.equals("")){
            domainName="";
        }else {
//            将url地下转化为小写
            serverName =serverName.toLowerCase();
//            如果Url地址是以http://开头 将http://截取
            if(serverName.startsWith("http://")){
                serverName=serverName.substring(7);
            }
            int end =serverName.length();
            //判断Url地址是否包含”/"
            if(serverName.contains("/")){
                //得到第一个“/"出现的位置
                end=serverName.indexOf("/");
            }
            //截取
            serverName =serverName.substring(0,end);
            //根据"."进行分割
            final String[] domains =serverName.split("\\.");
            int len =domains.length;
            if(len>3){
                //www.xxx.com.cn
                domainName=domains[len-3]+"."+domains[len-2]+"."+domains[len-1];
            } else if (len<=3&&len>1) {
            //www.com or xxx.cn
                domainName =domains[len-2]+"."+domains[len-1];
            }else {
                domainName=serverName;
            }
        }
        if (domainName!=null&&domainName.indexOf(":")>0){
            String []ary=domainName.split("\\:");
            domainName=ary[0];
        }
        return domainName;
    }
}
