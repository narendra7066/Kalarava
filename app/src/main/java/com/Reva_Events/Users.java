package com.Reva_Events;

public class Users {
    String name,mail,paswd,cnfrpaswd,mob;

    public Users(String n,String m,String p,String c,String mo)
    {
        name=n;
        mail=m;
        paswd=p;
        cnfrpaswd=c;
        mob=mo;
    }
    public void setName(String n)
    {
        this.name=n;
    }
    public void setMail(String n)
    {
        this.mail=n;
    }
    public void setPaswd(String n)
    {
        this.paswd=n;
    }
    public void setMobile(String n)
    {
        this.mob=n;
    }


    public String getName()
    {
        return this.name;
    }

    public String getMail()
    {
        return this.mail;
    }

    public String getPaswd()
    {
        return this.paswd;
    }

    public String getMob()
    {
        return this.mob;
    }
}
