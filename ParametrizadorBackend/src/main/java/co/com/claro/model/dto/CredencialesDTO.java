package co.com.claro.model.dto;

import java.io.Serializable;

public class CredencialesDTO implements Serializable{
	
	private String userName;
	private String passWord;
	private String domain;
	private String ip;
	private String port;
	private String commonName;
	private String domainGroup;
	private String organization;
	
	 public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String user) {
	        this.userName = user;
	    }
	    
	    public String getPassWord() {
	        return passWord;
	    }

	    public void setPassWord(String pass) {
	        this.passWord = pass;
	    }
	    
	    public String getDomain() {
	        return domain;
	    }

	    public void setDomain(String dom) {
	        this.domain = dom;
	    }
	    
	    public String getIp() {
	        return ip;
	    }

	    public void setIp(String ip) {
	        this.ip = ip;
	    }
	    
	    public String getPort() {
	        return port;
	    }

	    public void setPort(String port) {
	        this.port = port;
	    }
	    
	    public String getCommonName() {
	        return commonName;
	    }

	    public void setCommonName(String commonName) {
	        this.commonName = commonName;
	    }
	    
	    public String getDomainGroup() {
	        return domainGroup;
	    }

	    public void setDomainGroup(String domainGroup) {
	        this.domainGroup = domainGroup;
	    }
	    
	    public String getOrganization() {
	        return organization;
	    }

	    public void setOrganization(String organization) {
	        this.organization = organization;
	    }

}
