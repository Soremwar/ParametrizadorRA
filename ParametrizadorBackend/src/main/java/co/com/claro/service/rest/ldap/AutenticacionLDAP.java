package co.com.claro.service.rest.ldap;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;


public class AutenticacionLDAP {
	
	 public boolean login(String username, String password, String ip, String port, String cN, String dC, String oU) {
		 
		 
		        /* AUTENTICACION EN EL LDAP:
		         * (Se toma como base una conexion estable y 100% funcional de un LDAP existente)
		         * Para autenticar en el ldap se debe armar la cadena de conexion con los siguientes parametros:
		         * IP, PUERTO, CN, DC, OU
		         * En caso de que no exista un parametro como por ejemplo OU, la base de datos lo llena con un simbolo asterisco (*)
		         * el servicio convierte el asterisco en una cadena vacia para que no interfiera con la creacion de la url
		         * 
		         * 
		         * LA SIGUIENTE URL ES 100% FUNCIONAL: ldap://ldap.forumsys.com:389/cn=read-only-admin,dc=example,dc=com  
		         *                                                
		         * user: riemann
		         * pass: password
		         * IP: ldap.forumsys.com
		         * PUERTTO: 389
		         * CN: cn=read-only-admin
		         * DC: dc=example,dc=com
		         * OU: uid
		         * */
		         
		         //Creacion de la url con los parametros en DB	  
		 
		         String ldapUrl = "";
		         String credentialUrl = "";
		 
		         if(cN.equals("")) {
		        	 ldapUrl = "ldap://" + ip + ":" + port + "/" + dC;	
		         }else {		        	 
		        	 ldapUrl = "ldap://" + ip + ":" + port + "/" + cN + "," + dC;			        	 
		         }
		         
		         if(oU.equals("")) {
		        	 credentialUrl = username + "," + dC;
		         }else {
		        	 credentialUrl = oU + "=" + username + "," + dC;
		         }
		                               
                 Logger.getLogger(AutenticacionLDAP.class.getName()).log(Level.INFO, ldapUrl);
                 Logger.getLogger(AutenticacionLDAP.class.getName()).log(Level.INFO, credentialUrl);

		          Hashtable env = new Hashtable();
		          
		          env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		          env.put(Context.PROVIDER_URL, ldapUrl);
		          env.put(Context.SECURITY_AUTHENTICATION, "simple");
		          
		          //BUSQUEDA DE UNA CREDENCIAL DE USUARIO, OU + USUARIO + DC
		          
		          //EJEMPLO 100% FUNCIONAL CON EL LDAP DE PRUEBA: uid=riemann,dc=example,dc=com
		          //OU: uid, username: riemann, DC:,dc=example,dc=com
		          
		          env.put(Context.SECURITY_PRINCIPAL, credentialUrl);
		          env.put(Context.SECURITY_CREDENTIALS, password);
		          
		          try {
		              DirContext ctx = new InitialDirContext(env);
		              return true;
		          } catch (NamingException ex) {
		              Logger.getLogger(AutenticacionLDAP.class.getName()).log(Level.INFO, "No se pudo registrar en el LDAP", ex);
		          }

		          return false;
		   }
	 
}
