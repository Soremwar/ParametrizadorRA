package co.com.claro.model.entity;

import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.Politica;
import co.com.claro.model.entity.QueryAprobacion;
import co.com.claro.model.entity.WsTransformacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-08-19T08:53:35")
@StaticMetamodel(Conciliacion.class)
public class Conciliacion_ { 

    public static volatile SingularAttribute<Conciliacion, String> descripcion;
    public static volatile CollectionAttribute<Conciliacion, WsTransformacion> transformaciones;
    public static volatile CollectionAttribute<Conciliacion, QueryAprobacion> queriesAprobacion;
    public static volatile SingularAttribute<Conciliacion, String> nombre;
    public static volatile SingularAttribute<Conciliacion, Politica> politica;
    public static volatile CollectionAttribute<Conciliacion, EjecucionProceso> ejecucionesProceso;
    public static volatile SingularAttribute<Conciliacion, String> camposTablaDestino;
    public static volatile SingularAttribute<Conciliacion, Date> fechaActualizacion;
    public static volatile SingularAttribute<Conciliacion, Date> fechaCreacion;
    public static volatile SingularAttribute<Conciliacion, String> usuario;
    public static volatile SingularAttribute<Conciliacion, Integer> id;
    public static volatile ListAttribute<Conciliacion, Escenario> escenarios;
    public static volatile SingularAttribute<Conciliacion, String> tablaDestino;

}