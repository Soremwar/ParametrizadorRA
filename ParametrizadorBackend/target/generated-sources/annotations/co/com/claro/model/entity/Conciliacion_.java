package co.com.claro.model.entity;

import co.com.claro.model.entity.Politica;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-26T10:09:18")
@StaticMetamodel(Conciliacion.class)
public class Conciliacion_ { 

    public static volatile SingularAttribute<Conciliacion, String> descripcion;
    public static volatile SingularAttribute<Conciliacion, Integer> codConciliacion;
    public static volatile SingularAttribute<Conciliacion, String> camposTablaDestino;
    public static volatile SingularAttribute<Conciliacion, Politica> codPolitica;
    public static volatile SingularAttribute<Conciliacion, Date> fechaCreacion;
    public static volatile SingularAttribute<Conciliacion, Date> fechaActualizacion;
    public static volatile SingularAttribute<Conciliacion, String> usuario;
    public static volatile SingularAttribute<Conciliacion, String> nombreConciliacion;
    public static volatile SingularAttribute<Conciliacion, String> tablaDestino;

}