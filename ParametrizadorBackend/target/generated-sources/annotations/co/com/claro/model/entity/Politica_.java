package co.com.claro.model.entity;

import co.com.claro.model.entity.Conciliacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-22T11:48:35")
@StaticMetamodel(Politica.class)
public class Politica_ { 

    public static volatile SingularAttribute<Politica, String> descripcion;
    public static volatile SingularAttribute<Politica, String> objetivo;
    public static volatile SingularAttribute<Politica, String> nombrePolitica;
    public static volatile SingularAttribute<Politica, Integer> codPolitica;
    public static volatile SingularAttribute<Politica, Date> fechaCreacion;
    public static volatile SingularAttribute<Politica, Date> fechaActualizacion;
    public static volatile CollectionAttribute<Politica, Conciliacion> tblConciliacionCollection;
    public static volatile SingularAttribute<Politica, String> usuario;

}