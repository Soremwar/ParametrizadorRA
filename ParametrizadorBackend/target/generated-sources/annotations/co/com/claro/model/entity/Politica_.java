package co.com.claro.model.entity;

import co.com.claro.model.entity.Conciliacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-04-12T15:38:01")
@StaticMetamodel(Politica.class)
public class Politica_ { 

    public static volatile SingularAttribute<Politica, String> descripcion;
    public static volatile SingularAttribute<Politica, String> objetivo;
    public static volatile SingularAttribute<Politica, Integer> codigo;
    public static volatile SingularAttribute<Politica, Date> fechaCreacion;
    public static volatile SingularAttribute<Politica, Date> fechaActualizacion;
    public static volatile CollectionAttribute<Politica, Conciliacion> tblConciliacionCollection;
    public static volatile SingularAttribute<Politica, String> usuario;
    public static volatile SingularAttribute<Politica, String> nombre;

}