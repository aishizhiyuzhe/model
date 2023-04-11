package com.ming.common.content;


import com.ming.common.permission.UserRole;

import java.util.Date;

public class ReqInfoContent {
    private static ThreadLocal<ReqInfo> threadLocal=new ThreadLocal();

    public static void addReq(ReqInfo reqInfo){
        threadLocal.set(reqInfo);
    }

    public static ReqInfo getReq(){
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }

    public  class ReqInfo{
        String username;
        Date loginTime;
        UserRole role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Date getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }
}
