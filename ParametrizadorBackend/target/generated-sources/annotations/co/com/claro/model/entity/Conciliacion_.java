package co.com.claro.model.entity;

import co.com.claro.model.entity.Politica;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-04-30T08:51:07")
@StaticMetamodel(Conciliacion.class)
public class Conciliacion_ { 

    public static volatile SingularAttribute<Conciliacion, String> descripcion;
    public static volatile SingularAttribute<Conciliacion, String> camposTablaDestino;
    public static volatile SingularAttribute<Conciliacion, Date> fechaActualizacion;
    public static volatile SingularAttribute<Conciliacion, Date> fechaCreacion;
    public static volatile SingularAttribute<Conciliacion, String> usuario;
    public static volatile SingularAttribute<Conciliacion, Integer> id;
    public static volatile SingularAttribute<Conciliacion, String> nombre;
    public static volatile SingularAttribute<Conciliacion, String> tablaDestino;
    public static volatile SingularAttribute<Conciliacion, Politica> politica;

}